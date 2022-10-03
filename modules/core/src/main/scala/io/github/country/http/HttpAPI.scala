package io.github.country.http

import cats.effect.IO
import io.circe.syntax.EncoderOps
import io.github.country.algebras.CountryInfoAlgebra
import io.github.country.http.codec.Codecs.countryInfoResponseEncoder
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._

class HttpAPI(algebra: CountryInfoAlgebra[IO]) {
  val countryInfoEndpoint = HttpRoutes.of[IO]{
    case GET -> Root / "status" :? CountryQueryParamMatcher(iso2)=>
      algebra.countryInfoByISO(iso2).flatMap(info=>Ok(info.asJson))
  }
}
