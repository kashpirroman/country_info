package io.github.country

import cats.implicits._
import eu.timepit.refined.refineV
import io.estatico.newtype.ops._
import io.github.country.domain.{ISO2, ISO2Rule, ISO2Value}
import org.http4s.dsl.io.QueryParamDecoderMatcher
import org.http4s.{ParseFailure, QueryParamDecoder}

package object http {
  implicit val iso2QueryParamDecoder: QueryParamDecoder[ISO2] = QueryParamDecoder[String]
    .emap((str: String) => {
      refineV[ISO2Rule](str)
        .bimap(e => ParseFailure.apply(e, ""), (isoV: ISO2Value) => isoV.coerce[ISO2])
    })

  object CountryQueryParamMatcher extends QueryParamDecoderMatcher[ISO2]("country")
}
