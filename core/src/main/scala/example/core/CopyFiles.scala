package example.core

import cats.effect._
import cats.syntax.all._
import java.io._
import scala.collection.immutable.List
import scala.concurrent.duration._

object CopyFiles extends IOApp {
  def inputStream[F[_]: Sync](f: File): Resource[F, FileInputStream] =
    Resource.make {
      Sync[F].blocking(new FileInputStream(f)) // build
    } { inStream =>
      Sync[F].delay(println("🔄 [RELEASE] Input stream released"))
      Sync[F]
        .blocking(inStream.close())
        .handleErrorWith(_ => Sync[F].unit) // release
    }

  def outputStream[F[_]: Sync](f: File): Resource[F, FileOutputStream] =
    Resource.make {
      Sync[F].blocking(new FileOutputStream(f)) // build
    } { outStream =>
      Sync[F].delay(println("🔄 [RELEASE] Output stream released"))
      Sync[F].blocking(outStream.close()).handleErrorWith(_ => Sync[F].unit) // release
    }

  def inputOutputStreams[F[_]: Sync](in: File, out: File): Resource[F, (InputStream, OutputStream)] =
    for {
      inStream  <- inputStream(in)
      outStream <- outputStream(out)
    } yield (inStream, outStream)

  def transfer[F[_]: Async](in: InputStream, out: OutputStream, buffer: Array[Byte], acc: Long): F[Long] =
    for {
      amount <- Async[F].blocking(in.read(buffer, 0, buffer.length))
      _ <- Async[F].delay(println("Transfer started"))
      count  <- if(amount > -1) Async[F].blocking(out.write(buffer, 0, amount)) >> transfer(in, out, buffer, acc + amount)
                else Async[F].pure(acc) // End of read stream reached (by java.io.InputStream contract), nothing to write
    } yield count // Returns the actual amount of bytes transferred

  def copy(origin: File, destination: File): IO[Long] = {
    inputOutputStreams[IO](origin, destination).use { case (in, out) =>
      transfer[IO](in, out, new Array[Byte](1024 * 10), 0L)
    }
  }

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO.raiseWhen(args.length < 2)(new IllegalArgumentException("Need origin and destination files"))
      orig = new File(args(0))
      dest = new File(args(1))
      _ <- IO.raiseWhen(orig == dest)(new IllegalArgumentException("Origin file and destination file are the same"))
      _ <- IO.raiseWhen(!orig.canRead())(new IllegalArgumentException("Can not read from origin file"))
      _ <- IO.raiseWhen(!dest.canWrite())(new IllegalArgumentException("Can not write to destination file"))
      count <- copy(orig, dest)
      _     <- IO.println(s"$count bytes copied from ${orig.getPath} to ${dest.getPath}")
    } yield ExitCode.Success
}
