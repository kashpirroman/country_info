import Dependencies.{CompilerPlugins, Libraries}

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Wunused:imports,patvars,privates,locals,explicits,synthetics",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)
lazy val root = (project in file("."))
  .settings(
    name := "country_info"
  )
  .aggregate(core)

lazy val core =
  (project in file("modules/core"))
    .settings(
      libraryDependencies ++= Seq(
        compilerPlugin(CompilerPlugins.`context-applied`),
        compilerPlugin(CompilerPlugins.`kind-projector`),
        compilerPlugin(CompilerPlugins.`better-monadic-for`),
        Libraries.catsCore,
        Libraries.newtype,
        Libraries.refinedCore,
        Libraries.circeParser,
        Libraries.circeRefined,
        Libraries.circeCore,
        Libraries.circeYaml,
        Libraries.circeGeneric,
        Libraries.catsEffect,
        Libraries.http4sClient,
        Libraries.http4sServer,
        Libraries.http4sDsl,
        Libraries.http4sCirce,
      )
    )