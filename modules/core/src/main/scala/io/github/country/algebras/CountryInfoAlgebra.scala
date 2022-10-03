package io.github.country.algebras

import cats.implicits._
import cats.effect.kernel.Sync
import io.github.country.domain._
import io.github.country.http.client.{CountryDataClient, CurrencyClient, WeatherClient}

trait CountryInfoAlgebra[F[_]] {
  def countryInfoByISO(iso: ISO2): F[CountryInfoResponse]
}

object CountryInfoAlgebra {
  def make[F[_] : Sync](countryClient: CountryDataClient[F],
                        currencyClient: CurrencyClient[F],
                        weatherClient: WeatherClient[F]): CountryInfoAlgebra[F] =
    (iso: ISO2) => for {
      country <- countryClient.countryNameByISO(iso)
      capital <- countryClient.countryCapitalByISO(iso)
      currency <- countryClient.countryCurrencyByISO(iso)
      currencyRate <- currencyClient.currencyRateByName(currency)
      coordinates <- weatherClient.capitalCoordinatesByName(capital)
      temperature <- weatherClient.temperatureByCoordinates(coordinates)
    } yield CountryInfoResponse(country, capital, temperature, currency, currencyRate)
}