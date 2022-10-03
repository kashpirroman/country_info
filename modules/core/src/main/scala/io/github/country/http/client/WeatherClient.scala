package io.github.country.http.client

import cats.effect.kernel.{Async, Sync}
import cats.implicits._
import io.circe.Json
import io.github.country.config.WeatherApiKey
import io.github.country.domain.{Capital, Coordinates, Temperature}
import io.github.country.errors.{FetchCapitalCoordinatesError, FetchWeatherError}
import io.github.country.http.client.RootPaths.`openweathermap.org`
import io.github.country.http.codec.Codecs._
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.{Request, Uri}

trait WeatherClient[F[_]] {
  def capitalCoordinatesByName(capital: Capital): F[Coordinates]

  def temperatureByCoordinates(coordinates: Coordinates): F[Temperature]
}

object WeatherClient {
  def make[F[_] : JsonDecoder : Async](client: Client[F], apiKey: WeatherApiKey): F[WeatherClient[F]] =
    Sync[F].fromEither(Uri.fromString(`openweathermap.org`)).flatMap { uri =>
      new WeatherClient[F] {
        override def capitalCoordinatesByName(capital: Capital): F[Coordinates] = {
          //http://api.openweathermap.org/geo/1.0/direct?q=Moscow&appid=c72b7759b7e7322ae2db94ebe20279bf
          client.run(Request[F](uri = (uri / "geo" / "1.0" / "direct")
            .withQueryParam("q", capital.name)
            .withQueryParam("appid", apiKey.key))
          ).use(resp =>
            resp.as[Coordinates].handleErrorWith(_ => FetchCapitalCoordinatesError(capital).raiseError[F, Coordinates])
          )
        }

        override def temperatureByCoordinates(coordinates: Coordinates): F[Temperature] =
          client.run(Request[F](uri = (uri / "data" / "2.5" / "weather")
            .withQueryParam("lat", coordinates.latitude.value)
            .withQueryParam("lon", coordinates.latitude.value)
            .withQueryParam("appid", apiKey.key)
            .withQueryParam("units", "metric"))
          ).use(resp =>
            resp.as[Json].flatMap(json =>
              json.hcursor.downField("main")
                .get[Temperature]("temp").fold(
                _ => FetchWeatherError(json, coordinates).raiseError[F, Temperature], _.pure[F]
              ))

          )
      }.pure[F]
    }
}
