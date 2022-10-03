package io.github.country

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all.MatchesRegex
import eu.timepit.refined.types.all.{PosDouble, PosFloat}
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype


package object domain {

  type ISO2Rule = MatchesRegex["[A-Z]{2}"]
  type ISO2Value = String Refined ISO2Rule

  case class CountryInfoResponse(
                                  country: Country,
                                  capital: Capital,
                                  temperature: Temperature,
                                  currency: Currency,
                                  currencyRate: CurrencyRate
                                )

  @newtype case class Country(name: NonEmptyString)

  @newtype case class Capital(name: NonEmptyString)

  @newtype case class CurrencyRate(rate: PosFloat)

  @newtype case class Temperature(value: Float)

  @newtype case class Currency(name: NonEmptyString)


  @newtype case class ISO2(value: ISO2Value)

  case class Coordinates(latitude: Latitude, longitude: Longitude)

  @newtype case class Latitude(value: PosDouble)

  @newtype case class Longitude(value: PosDouble)

}
