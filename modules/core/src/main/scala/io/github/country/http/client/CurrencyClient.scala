package io.github.country.http.client

import cats.effect.Async
import cats.effect.kernel.Sync
import io.github.country.config.CurrencyApiKey
import io.github.country.domain.{Currency, CurrencyRate}
import io.github.country.http.client.RootPaths.`openexchangerates.org`
import org.http4s.{Request, Uri}
import org.http4s.client.Client
import cats.implicits._
import io.circe.Json
import io.github.country.errors.FetchCurrencyRateError
import io.github.country.http.codec.Codecs._
import io.circe.refined._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.JsonDecoder

trait CurrencyClient[F[_]] {
  def currencyRateByName(currency: Currency): F[CurrencyRate]
}

object CurrencyClient {

  def make[F[_] : JsonDecoder : Async](client: Client[F], apiKey: CurrencyApiKey): F[CurrencyClient[F]] =
    Sync[F].fromEither(Uri.fromString(`openexchangerates.org`)).flatMap {
      uri =>
        new CurrencyClient[F] {
          override def currencyRateByName(currency: Currency): F[CurrencyRate] =
            client
              .run(Request[F](uri = (uri / "api" / "latest.json")
                .withQueryParam("app_id", apiKey.key)))
              .use {
                resp =>
                  resp.as[Json].flatMap { json =>
                    json.hcursor
                      .downField("rates")
                      .get[CurrencyRate](currency.name.value)
                      .fold(_ => FetchCurrencyRateError(json, currency).raiseError[F, CurrencyRate],
                        _.pure[F])
                  }
              }
        }.pure[F]

    }
}