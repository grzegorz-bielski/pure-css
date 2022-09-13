ThisBuild / organization := "pureframes"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.0"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `pure-css` = project
  .in(file("."))

lazy val plugin = project
  .settings(
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )