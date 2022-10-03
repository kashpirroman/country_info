package io.github.country

import io.circe.Json
import io.github.country.domain.{Capital, Coordinates, Currency, ISO2}

import scala.util.control.NoStackTrace

package object errors {
  trait BusinessError extends NoStackTrace {
    def cause: String

    override def getMessage: String = cause

    override def toString: String = s"${this.getClass.getSimpleName}:$cause"
  }

  case class ConfigFetchError(message: String) extends BusinessError {
    override def cause: String = s"cannot parse configs:$message"
  }

  case class FetchCountryError(json: Json, iso2: ISO2, filename: String) extends BusinessError {
    override def cause: String = s"cannot find value for key $iso2  on $filename : $json"
  }

  case class FetchCurrencyRateError(json: Json, currency: Currency) extends BusinessError {
    override def cause: String = s"cannot find currency rate for $currency on json: $json"
  }

  case class FetchWeatherError(json: Json, coordinates: Coordinates) extends BusinessError {
    override def cause: String = s"cannot find weather info for $coordinates on json: $json"
  }

  case class FetchCapitalCoordinatesError(capital: Capital) extends BusinessError {
    override def cause: String = s"cannot find coordinates info for capital:$capital"
  }

}
