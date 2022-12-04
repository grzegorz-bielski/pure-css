import org.scalajs.linker.interface.OutputPatterns

ThisBuild / organization := "eu.pureframes"
ThisBuild / scalaVersion := "3.2.1"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / description := "Library for embedding CSS in Scala sources"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = project
  .in(file("."))
  .settings(
    neverPublish
  )
  .aggregate(core.js, core.jvm)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .settings(
    name := "css-core",
    libraryDependencies ++= Seq(
      "org.scalameta" %%% "munit" % "0.7.29" % Test
    ),
    scalacOptions ++= Seq(
      "-Xcheck-macros",
      "-Yretain-trees" // TODO: is it still needed?
    ),
    publishSettings
  )
  .jsSettings(
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs"))
    }
  )

lazy val publishSettings = {
  import xerial.sbt.Sonatype._

  Seq(
    publishTo := sonatypePublishToBundle.value,
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    publishMavenStyle := true,
    licenses := Seq(
      "APL2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")
    ),
    sonatypeProjectHosting := Some(
      GitHubHosting("grzegorz-bielski", "pure-css", "pesiok@gmail.com")
    )
  )
}

lazy val neverPublish = Seq(
  publish / skip := true,
  publishLocal / skip := true
)
