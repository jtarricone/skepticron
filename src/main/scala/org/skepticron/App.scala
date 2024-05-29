package org.skepticron

import cats.effect._
import cats.effect.std._
import cats.implicits._
import com.monovore.decline._

import org.skepticron._

object App {
  private object SharedOpts {
  }

  case class About()
  object About {
    val opts = Opts.subcommand("about", "About Skepticron")(
      Opts(About())
    )

    def run[F[_]: Async](): F[Unit] = println("TODO").pure[F]

  }

  case class Train(corpus: String, placeholder: Option[String]) // TODO
  object Train {
    private val corpusOpts: Opts[String] =
      Opts.option[String]("corpus", "Filepath of the corpus to be used for training", "c")

    private val placeholderOpts: Opts[Option[String]] =
      Opts.option[String]("placeholder", "A placeholder for more interesting parameters to come", "p").orNone

    val opts:Opts[Train] = Opts.subcommand("train", "Train a model instance") {
      (corpusOpts, placeholderOpts).mapN(Train.apply)
    }

    def run[F[_]: Async](train: Train): F[Unit] = {
      val r = for {
        _ <- Resource.eval(Sync[F].delay(println(s"Reading data from file at ${train.corpus}")))
        trainingText <- Resource.eval(Utils.readFile[F](train.corpus))
        _ <- Resource.eval(Sync[F].delay(println(s"Read ${trainingText.length} lines")))
        _ <- Resource.eval(Sync[F].delay(println(trainingText).pure[F]))
      } yield ()
      r.use(_ => Sync[F].unit)
    }
  }

  case class Generate(length: Long)
  object Generate {
    private val lengthOpts: Opts[Long] = Opts.option[Long]("length", "Length of generated output", "l")

    val opts: Opts[Generate] = Opts.subcommand("generate", "Generate output from a model instance") {
      (lengthOpts).map(Generate.apply(_))
    }

    def run[F[_]: Async](length: Long): F[Unit] = println("TODO").pure[F]
  }

  case class Mascot()

  object Mascot {
    val opts = Opts.subcommand("mascot", "*skeptical glare*")(
      Opts(Mascot())
    )

    def run[F[_] : Async](): F[Unit] = Art.printMascot[F]
  }
}
