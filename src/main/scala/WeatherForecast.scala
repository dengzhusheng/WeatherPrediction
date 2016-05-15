package com.dzs.weather

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar

/** Main entrance for the weather prediction program */
object WeatherForecast {
    /** Weather stations of Australia */
    val datasetPath = "./src/main/resources/"
    val weatherStations = Array[WeatherStation](
            new BomWeatherStation("Sydney NSW", "SYD", Position(-33.86, 151.20, 39.0), datasetPath + "sydney201604.csv"),
            new BomWeatherStation("Canberra ACT", "CBR", Position(-35.30, 149.20, 577.05), datasetPath + "canberra201604.csv"),
            new BomWeatherStation("Melbourne VIC", "MEL", Position(-37.83, 144.98, 29.05), datasetPath + "melbourne201604.csv"),
            new BomWeatherStation("Adelaide SA", "ADL", Position(-34.94, 138.58, 34), datasetPath + "adelaide201604.csv"),
            new BomWeatherStation("Brisbane QLD", "BNE", Position(-27.48, 153.03, 8.13), datasetPath + "brisbane201604.csv"),
            new BomWeatherStation("Perth WA", "PER", Position(-31.91, 115.87, 24.9), datasetPath + "perth201604.csv"),
            new BomWeatherStation("Darwin NT", "DRW", Position(-12.43, 130.83, 15.2), datasetPath + "darwin201604.csv"),
            new BomWeatherStation("Hobart TAS", "HBA", Position(-42.88 , 147.30, 231.6), datasetPath + "hobart201604.csv")
            )
    val predictor = new WeatherPredictor()
    val broadcast = new FileBroadcast("./forecast.txt")
    /** val broadcast = new ConsoleBroadcast() */

    /** Main entrance for the weather prediction program */
    def main(args: Array[String]): Unit = {
        for (weatherStation <- weatherStations) {
            weatherStation.measure((today, delta) => 
                broadcast.broadcast(predictor.predictWeather(today, delta))
            )
        }

        broadcast.close()
    }
}
