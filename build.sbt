
lazy val root = (project in file(".")).
  settings(
    name := "WeatherPrediction",
    version := "1.0",
    scalaVersion := "2.10.6",
    mainClass in (Compile, run) := Some("com.dzs.weather.WeatherForecast"),

    libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )
