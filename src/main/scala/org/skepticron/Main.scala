package org.skepticron

import cats.effect._
import com.monovore.decline._
import com.monovore.decline.effect._

object Main extends CommandIOApp(
  name = org.skepticron.BuildInfo.name,
  header = "Skepticron -- The Language Model for No-One",
  helpFlag = true,
  version = "0.01"
) {
  def main: Opts[IO[ExitCode]] = {
    (
      App.Train.opts orElse
      App.Generate.opts orElse
      App.About.opts orElse
      App.Mascot.opts
    ).map{
      case t: App.Train => App.Train.run[IO](t.corpus, t.placeholder).as(ExitCode.Success)
      case g: App.Generate => App.Generate.run[IO](g.length).as(ExitCode.Success)
      case _: App.About => App.About.run[IO]().as(ExitCode.Success)
      case _: App.Mascot => App.Mascot.run[IO]().as(ExitCode.Success)
      case _ => IO(ExitCode.Error)
    }
  }
}
