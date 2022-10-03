import sbt._

object Dependencies {
  case object Versions {

    val catsCore = "2.6.1"
    val catsEffect = "3.1.1"
    val newtype = "0.4.4"
    val refined = "0.9.28"
    val http4s = "0.23.7"
    val circe = "0.14.1"


    val betterMonadicFor = "0.3.1"
    val contextApplied = "0.1.4"
    val betterToString = "0.3.14"
    val kindProjector = "0.13.2"
    val fs2kafka = "2.3.0"

    val circeAdt = "0.0.7-SNAPSHOT"
    val decline = "2.2.0"
  }

  case object Libraries {
    def typelevel(artifact: String, version: String) = "org.typelevel" %% artifact % version

    def refined(artifact: String): ModuleID = "eu.timepit" %% artifact % Versions.refined

    def circe(artifact: String): ModuleID = "io.circe" %% artifact % Versions.circe

    def http4s(artifact: String): ModuleID = "org.http4s" %% artifact % Versions.http4s

    val catsCore = typelevel("cats-core", Versions.catsCore)
    val catsEffect = typelevel("cats-effect", Versions.catsEffect)

    val refinedCore = refined("refined")
    val refinedCats = refined("refined-cats")

    val circeCore = circe("circe-core")
    val circeOptics = circe("circe-optics")
    val circeGeneric = circe("circe-generic")
    val circeParser = circe("circe-parser")
    val circeYaml = circe("circe-yaml")
    val circeRefined = circe("circe-refined")
    val http4sDsl = http4s("http4s-dsl")
    val http4sClient = http4s("http4s-blaze-client")
    val http4sServer = http4s("http4s-blaze-server")
    val http4sCirce = http4s("http4s-circe")
    val newtype = "io.estatico" %% "newtype" % Versions.newtype
  }

  case object CompilerPlugins {
    val `kind-projector` = "org.typelevel" %% "kind-projector" % Versions.kindProjector cross CrossVersion.full
    val `context-applied` = "org.augustjune" %% "context-applied" % Versions.contextApplied
    val `better-monadic-for` = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
  }
}