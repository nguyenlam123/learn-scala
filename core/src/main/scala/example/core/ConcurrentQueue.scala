package example.core

import cats.effect._
import cats.syntax.all._
import cats.effect.syntax.all._
import scala.collection.immutable.Queue
import cats.effect.Deferred
import scala.List

trait ConcurrentQueue[F[_], A] {
  def offer(a: A): F[Unit]
  def take: F[A]
}

object ConcurrentQueue {
  private case class State[F[_], A](
    queue: Queue[A],
    capacity: Int,
    takers: Queue[Deferred[F,A]],
    offerers: Queue[(A, Deferred[F,Unit])]
  )

  def apply[F[_]: Async, A](capacity: Int): F[ConcurrentQueue[F, A]] = {
    if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive")
    
    Ref[F].of(State[F, A](Queue.empty, capacity, Queue.empty, Queue.empty)).map { state =>
      new ConcurrentQueue[F, A] {
        def offer(a: A): F[Unit] =
          Deferred[F, Unit].flatMap { offerer =>
            Async[F].uncancelable { poll =>
              state.modify {
                // Handoff to waiting consumer
                case State(queue, capacity, takers, offerers) if takers.nonEmpty =>
                  val (taker, rest) = takers.dequeue
                  State(queue, capacity, rest, offerers) -> taker.complete(a).void
                
                // Queue has space
                case State(queue, capacity, takers, offerers) if queue.size < capacity =>
                  State(queue.enqueue(a), capacity, takers, offerers) -> Async[F].unit
                
                // Full Queue. Producer must wait
                case State(queue, capacity, takers, offerers) =>
                  val cleanup = state.update(s => s.copy(offerers = s.offerers.filter(_._2 ne offerer)))
                  State(queue, capacity, takers, offerers.enqueue(a -> offerer)) -> 
                    poll(offerer.get).onCancel(cleanup)
              }.flatten
            }
          }

        def take: F[A] =
          Deferred[F, A].flatMap { taker =>
            Async[F].uncancelable { poll =>
              state.modify {
                case State(queue, capacity, takers, offerers) if queue.nonEmpty && offerers.isEmpty =>
                  val (item, rest) = queue.dequeue
                  State(rest, capacity, takers, offerers) -> Async[F].pure(item)

                case State(queue, capacity, takers, offerers) if queue.nonEmpty =>
                  val (item, rest) = queue.dequeue
                  val ((move, release), tail) = offerers.dequeue
                  State(rest.enqueue(move), capacity, takers, tail) -> release.complete(()).as(item)

                case State(queue, capacity, takers, offerers) if offerers.nonEmpty =>
                  val ((item, release), rest) = offerers.dequeue
                  State(queue, capacity, takers, rest) -> release.complete(()).as(item)
                  
                case State(queue, capacity, takers, offerers) =>
                  val cleanup = state.update(s => s.copy(takers = s.takers.filter(_ ne taker)))
                  State(queue, capacity, takers.enqueue(taker), offerers) -> 
                    poll(taker.get).onCancel(cleanup)
              }.flatten
            }
          }
      }
    }
  }

  def demo: IO[ExitCode] = {
    def producer(id: Int, queue: ConcurrentQueue[IO, Int], counter: Ref[IO, Int]): IO[Unit] =
      for {
        i <- counter.getAndUpdate(_ + 1)
        _ <- queue.offer(i)
        _ <- IO.whenA(i % 100000 == 0)(IO.println(s"Producer $id has reached $i items"))
        _ <- producer(id, queue, counter)
      } yield ()

    def consumer(id: Int, queue: ConcurrentQueue[IO, Int]): IO[Unit] =
      for {
        i <- queue.take
        _ <- IO.whenA(i % 100000 == 0)(IO.println(s"Consumer $id has reached $i items"))
        _ <- consumer(id, queue)
      } yield ()

    for {
      queue <- ConcurrentQueue[IO, Int](1000)
      counter <- Ref[IO].of(1)
      producers = List.range(1, 11).map(producer(_, queue, counter))  // 10 producers
      consumers = List.range(1, 11).map(consumer(_, queue))          // 10 consumers
      res <- (producers ++ consumers)
        .parSequence.as(ExitCode.Success)  // Run producers and consumers in parallel
        .handleErrorWith { t =>
          IO.println(s"Error caught: ${t.getMessage}").as(ExitCode.Error)
        }
    } yield res
  }
} 