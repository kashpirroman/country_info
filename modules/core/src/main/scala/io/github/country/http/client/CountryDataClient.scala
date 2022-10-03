package io.github.country.http.client


import io.github.country.domain._
import org.http4s.client.Client
import org.http4s._
import RootPaths._
import cats.effect.Async
import cats.effect.kernel.Sync
import cats.implicits._
import io.circe.{Decoder, Json}
import io.github.country.errors.FetchCountryError
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import io.github.country.http.codec.Codecs._
import io.circe.refined._
import org.http4s.circe.JsonDecoder

trait CountryDataClient[F[_]] {
  def countryNameByISO(iso: ISO2): F[Country]

  def countryCapitalByISO(iso: ISO2): F[Capital]

  def countryCurrencyByISO(iso: ISO2): F[Currency]

}

object CountryDataClient {
  def make[F[_] : JsonDecoder : Async](client: Client[F]): F[CountryDataClient[F]] = Sync[F].fromEither(Uri.fromString(`country.io`)).flatMap {
    uri =>
      new CountryDataClient[F] {
        private def fetchDataByISO[A: Decoder](filename: String)(iso: ISO2): F[A] =
          client.run(Request[F](uri = uri / filename)).use {
            resp =>
              resp.as[Json].flatMap {
                json =>
                  json
                    .hcursor
                    .get[A](iso.value.value)
                    .fold(_ => FetchCountryError(json, iso, filename).raiseError[F, A], _.pure[F])
              }
          }

        override def countryNameByISO(iso: ISO2): F[Country] = fetchDataByISO[Country]("names.json")(iso)

        override def countryCapitalByISO(iso: ISO2): F[Capital] = fetchDataByISO[Capital]("capital.json")(iso)

        override def countryCurrencyByISO(iso: ISO2): F[Currency] = fetchDataByISO[Currency]("currency.json")(iso)

      }.pure[F]
  }

}
