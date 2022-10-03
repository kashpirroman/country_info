package io.github.country

import cats.effect.{ExitCode, IO, IOApp}
import io.github.country.config.ConfigReader
import io.github.country.http.client.{CountryDataClient, CurrencyClient, WeatherClient}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder
import io.github.country.algebras.CountryInfoAlgebra
import io.github.country.http.HttpAPI

//todo:1. add logger 2. add iso3 support

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    BlazeClientBuilder[IO]
      .resource
      .evalMap(
        client =>
          for {
            configs <- ConfigReader.localConfigs[IO].read
            countryDataClient <- CountryDataClient.make[IO](client)
            currencyClient <- CurrencyClient.make[IO](client, configs.currency)
            weatherClient <- WeatherClient.make[IO](client, configs.weather)
            algebras = CountryInfoAlgebra.make[IO](countryDataClient, currencyClient, weatherClient)
          } yield algebras
      ).flatMap(algebra =>
      BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(new HttpAPI(algebra).countryInfoEndpoint.orNotFound)
        .resource
    ).use(_ => IO.never).as(ExitCode.Success)
  }
}
