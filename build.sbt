ThisBuild / organization := "pureframes"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.0"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = project
  .in(file("."))
  .aggregate(
    core,
    plugin
  )

lazy val tests = project
  .settings(
    libraryDependencies ++= Seq(
       Munit % Test,
       compilerPlugin("pureframes" %% "css-extractor-plugin" % version.value)
    )

  )
  .dependsOn(core)

lazy val plugin = project
  .settings(
    name := "css-extractor-plugin",
    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-compiler" % scalaVersion.value % "provided",
      Munit % Test
    )
  )

lazy val core = project
 .settings(
    name := "css-core",
    libraryDependencies += Munit % Test
  )


lazy val Munit = "org.scalameta" %% "munit" % "0.7.29"

addCommandAlias("ptest", "; plugin / publishLocal; tests / clean; tests / compile")