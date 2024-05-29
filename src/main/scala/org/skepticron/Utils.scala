package org.skepticron

import cats.effect.Concurrent
import cats.implicits._
import fs2.*
import fs2.io.file.{Path, Files}


object Utils {
  // TODO: either replace or add streamier versions if it makes sense
  def readFile[F[_]: Files: Concurrent](filepath: String): F[Array[String]] = {
    val path = Path(filepath)
    Files[F]
      .readUtf8Lines(path)
      .compile
      .toVector
      .map(_.toArray)
  }

  def writeFile[F[_]: Files: Concurrent](filepath: String, lines: List[String]): F[Unit] = {
    val path = Path(filepath)
      Stream.emits(lines)
      .through(
        Files[F]
          .writeUtf8Lines(path)
      )
      .compile
      .drain
  }
}