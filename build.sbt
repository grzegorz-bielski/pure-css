import org.scalajs.linker.interface.OutputPatterns

ThisBuild / organization := "pureframes"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1-RC4"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = project
  .in(file("."))
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
    )
  )
  .jsSettings(
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs"))
    }
  )
