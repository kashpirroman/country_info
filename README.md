# Abstract Description
Country info project is pet project with using Scala and Typelevel ecosystem.

# Run App anywhere
Ensure that api keys in `application.yaml` are set and run.


``` 
sbt run
```

# Get Country info endpoint
Request:
```curl
curl --location --request GET 'http://localhost:8080/status?country=RUS'
```
Response:
```json
{
  "country" : "Russia",
  "capital" : "Moscow",
  "temperature" : 20.76,
  "currency" : "RUB",
  "currencyRate" : 67.222
}
```
