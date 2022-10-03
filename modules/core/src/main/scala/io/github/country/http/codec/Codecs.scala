package io.github.country.http.codec

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder}
import io.github.country.domain._


object Codecs extends CoercibleCodecs {

  import io.circe.refined._

  implicit val coordinatesDecoder: Decoder[Coordinates] = Decoder.instance {
    cursor =>
      for {
        latitude <- cursor.downArray.get[Latitude]("lat")
        longitude <- cursor.downArray.get[Longitude]("lon")
      } yield Coordinates(latitude, longitude)
  }


  implicit val countryInfoResponseEncoder: Encoder[CountryInfoResponse] = deriveEncoder


}
