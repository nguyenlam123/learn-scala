package example.core

import cats.effect._
import cats.syntax.all._
import cats.effect.syntax.all._
import scala.collection.immutable.Queue
import cats.effect.Deferred
import scala.List

object ConcurrentQueueDemo extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = ConcurrentQueue.demo
} 