val scalaV = "3.3.3"
val logbackClassicV = "1.5.3"

val catsV = "2.10.0"
val catsEffectV = "3.5.4"
val declineV = "2.4.1"
val fs2V = "3.10.2"
val circeV = "0.14.6"
val breezeV = "2.1.0"

// Projects
lazy val skepticron = project.in(file("."))
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(NativeImagePlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(
    name := "skepticron",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "org.skepticron",
    maintainer := "joe.tarricone@banno.com",
    scalacOptions -= "-Xfatal-warnings",
    Compile / mainClass := Some("org.skepticron.Main"),
    nativeImageOptions := {
      Seq(
        "--no-fallback",
        "--initialize-at-build-time=scala",
        "--enable-http",
        "--enable-https",
        "--report-unsupported-elements-at-runtime",
      ) ++
      sys.env
        .get("NATIVE_IMAGE_STATIC")
        .map(_.toBoolean)
        .filter(identity)
        .map(_ => "--static")
        .toSeq ++
      sys.env
        .get("NATIVE_IMAGE_MUSL")
        .map(path => s"-H:UseMuslC=$path")
        .toSeq
    },
    nativeImageInstalled := true
  )

// General Settings
lazy val commonSettings = Seq(
  scalaVersion := scalaV,
  crossScalaVersions := Seq(scalaVersion.value),


  libraryDependencies ++= Seq(
    "com.monovore"                %% "decline-effect"             % declineV,

    "org.typelevel"               %% "cats-core"                  % catsV,
    "org.typelevel"               %% "cats-effect"                % catsEffectV,

    "co.fs2"                      %% "fs2-core"                   % fs2V,
    "co.fs2"                      %% "fs2-io"                     % fs2V,

    "ch.qos.logback"              % "logback-classic"               % logbackClassicV,
    "io.circe"                    %% "circe-core"                 % circeV,
    "io.circe"                    %% "circe-generic"              % circeV,
    "io.circe"                    %% "circe-parser"               % circeV,
    "org.scalanlp"                %% "breeze"                     % breezeV
  )
)

// General Settings
inThisBuild(List(
  organization := "org",
  homepage := Some(url("https://github.com/jtarricone/skepticron")),
))
