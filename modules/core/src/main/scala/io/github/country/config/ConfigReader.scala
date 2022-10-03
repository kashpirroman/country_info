package io.github.country.config

import cats.effect.kernel.Sync
import cats.implicits._
import io.circe.yaml.parser
import io.github.country.errors.ConfigFetchError

import java.io.InputStreamReader

trait ConfigReader[F[_]] {
  def read: F[CountryInfoConfig]
}

object ConfigReader {

  def localConfigs[F[_] : Sync]: ConfigReader[F] = new ConfigReader[F] {
    override def read: F[CountryInfoConfig] =
      F.delay(new InputStreamReader(getClass.getClassLoader.getResourceAsStream("application.yaml"))).flatMap(
        inputReader => parser.parse(inputReader).fold(e => ConfigFetchError(e.message).raiseError[F, CountryInfoConfig], json => json.as[CountryInfoConfig].fold(e => ConfigFetchError(e.message).raiseError[F, CountryInfoConfig], _.pure[F]))
      )
  }
}
