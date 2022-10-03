package io.github.country

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.estatico.newtype.macros.newtype
import io.github.country.http.codec.CoercibleCodecs

package object config {

  @newtype case class CurrencyApiKey(key: NonEmptyString)

  @newtype case class WeatherApiKey(key: NonEmptyString)

  case class CountryInfoConfig(currency: CurrencyApiKey,
                               weather: WeatherApiKey)

  object CountryInfoConfig extends CoercibleCodecs{
    import io.circe.refined._
    implicit val decoder:Decoder[CountryInfoConfig]= deriveDecoder
  }
}



