package example.core

import cats.effect._
import cats.syntax.all._
import java.io._
import java.nio.file._
import scala.collection.immutable.List
import scala.jdk.CollectionConverters._
import scala.concurrent.duration._

object CopyFolders extends IOApp {
  def listFilesRecursively[F[_]: Sync](dir: File): F[List[File]] = {
    def listFiles(file: File): F[List[File]] = 
      for {
        isDir <- Sync[F].delay(file.isDirectory())
        contents <- if (isDir) {
          Sync[F].delay {
            val files = file.listFiles()
            if (files == null) Array.empty[File] else files
          }
        } else {
          Sync[F].pure(Array.empty[File])
        }
        children <- contents.toList.traverse(listFiles)
      } yield if (isDir) file :: children.flatten else List(file)
    
    listFiles(dir)
  }

  def getRelativePath(base: File, file: File): Path = {
    val basePath = base.toPath
    val filePath = file.toPath
    basePath.relativize(filePath)
  }

  def createDirectoryStructure[F[_]: Sync](sourceBase: File, destBase: File, files: List[File]): F[Unit] = {
    val directories = files.filter(_.isDirectory)
    directories.traverse_ { dir =>
      val relativePath = getRelativePath(sourceBase, dir)
      val targetDir = new File(destBase, relativePath.toString)
      Sync[F].delay(targetDir.mkdirs())
    }
  }

  def inputStream[F[_]: Sync](f: File): Resource[F, FileInputStream] =
    Resource.make {
      Sync[F].blocking(new FileInputStream(f)) // build
    } { inStream =>
      Sync[F].delay(println(s"🔄 [RELEASE] Input stream released for ${f.getName}"))
      Sync[F]
        .blocking(inStream.close())
        .handleErrorWith(_ => Sync[F].unit) // release
    }

  def outputStream[F[_]: Sync](f: File): Resource[F, FileOutputStream] =
    Resource.make {
      Sync[F].blocking(new FileOutputStream(f)) // build
    } { outStream =>
      Sync[F].delay(println(s"🔄 [RELEASE] Output stream released for ${f.getName}"))
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
      count  <- if(amount > -1) Async[F].blocking(out.write(buffer, 0, amount)) >> transfer(in, out, buffer, acc + amount)
                else Async[F].pure(acc)
    } yield count

  def copyFile(source: File, dest: File): IO[Long] = {
    inputOutputStreams[IO](source, dest).use { case (in, out) =>
      IO.println(s"📄 Copying file: ${source.getName}") >>
      transfer[IO](in, out, new Array[Byte](1024 * 10), 0L)
    }
  }

  // Main copy function that handles the entire folder
  def copyFolder(source: File, dest: File): IO[Unit] = 
    for {
      _ <- IO.println(s"📁 Starting to copy folder: ${source.getName}")
      files <- listFilesRecursively[IO](source)
      _ <- createDirectoryStructure[IO](source, dest, files)
      regularFiles = files.filter(!_.isDirectory)
      _ <- regularFiles.traverse_ { file =>
        val relativePath = getRelativePath(source, file)
        val targetFile = new File(dest, relativePath.toString)
        targetFile.getParentFile.mkdirs() // Ensure parent directories exist
        for {
          result <- copyFile(file, targetFile).attempt
          _ <- result match {
            case Right(bytes) => IO.println(s"✅ Successfully copied ${file.getName} ($bytes bytes)")
            case Left(error) => IO.println(s"❌ Failed to copy ${file.getName}: ${error.getMessage}")
          }
        } yield ()
      }
      _ <- IO.println(s"✨ Finished copying folder: ${source.getName}")
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO.raiseWhen(args.length < 2)(new IllegalArgumentException("Need source and destination folders"))
      source = new File(args(0))
      dest = new File(args(1))
      _ <- IO.raiseWhen(!source.exists())(new IllegalArgumentException("Source folder does not exist"))
      _ <- IO.raiseWhen(!source.isDirectory)(new IllegalArgumentException("Source must be a directory"))
      _ <- IO.raiseWhen(source == dest)(new IllegalArgumentException("Source and destination folders are the same"))
      _ <- IO.raiseWhen(!source.canRead())(new IllegalArgumentException("Cannot read from source folder"))
      _ <- IO.raiseWhen(!dest.canWrite())(new IllegalArgumentException("Cannot write to destination folder"))
      _ <- copyFolder(source, dest)
    } yield ExitCode.Success
} 