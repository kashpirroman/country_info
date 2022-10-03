package io.github.country.http

import eu.timepit.refined.types.all.PosDouble
import eu.timepit.refined.types.string.NonEmptyString
import org.http4s.{QueryParamEncoder, Uri}

package object client {
  implicit class UriOps(uri: Uri) {
    def /(segment: NonEmptyString): Uri = uri / segment.value
  }


  implicit val nesQueryParamEncoder: QueryParamEncoder[NonEmptyString] = QueryParamEncoder[String].contramap(_.value)
  implicit val posDoubleQueryParamEncoder: QueryParamEncoder[PosDouble] = QueryParamEncoder[Double].contramap(_.value)


  object RootPaths {
    val `country.io` = "http://country.io"
    val `openexchangerates.org` = "https://openexchangerates.org"
    val `openweathermap.org` = "http://api.openweathermap.org"
  }
}
