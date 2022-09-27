ThisBuild / organization := "pureframes"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1-RC2"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = project
  .in(file("."))
  .aggregate(core.js, core.jvm)

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .settings(
    name := "css-core",
    libraryDependencies ++= Seq(
      Munit % Test
    ),
    scalacOptions := Seq(
      "-Xcheck-macros",
      "-Yretain-trees"
    )
  )

lazy val Munit = "org.scalameta" %% "munit" % "0.7.29"

// addCommandAlias("ptest", "; plugin / publishLocal; tests / clean; tests / test")
