package com.dzs.weather.test

import org.scalatest.FunSuite
import com.dzs.weather.ConsoleBroadcast
import com.dzs.weather.Environment
import com.dzs.weather.Position
import com.dzs.weather.Weather
import com.dzs.weather.BomWeatherStation
import com.dzs.weather.Condition._
import java.text.SimpleDateFormat

class WeatherStationTests extends FunSuite {
    test("the BomWeatherStation.delta is correct") {
        val weatherStation = new BomWeatherStation("Sydney NSW", "SYD",
                                        Position(-33.86, 151.20, 39.0),
                                        "./src/main/resources/sydney201604.csv")
        val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        val date = df.parse("2015-12-23T05:02:12Z")
        val today = Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Rain, 12.5, 2, 50, 97, 0, 1004)
                           )
        val yesterday = Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Sunny, 10.0, 3, 40, 90, 0, 1000)
                           )
        val delta = weatherStation.delta(today, yesterday)
        assert(delta.weather.temperature == 2.5 &&
               delta.weather.evaporation == -1.0 &&
               delta.weather.windSpeed == 10.0 &&
               delta.weather.humidity == 7.0 &&
               delta.weather.cloudAmount == 0 &&
               delta.weather.mslp == 4.0)
    }

    test("the BomWeatherStation.parse is correct") {
        val line = ",2016-04-1,16.6,25.6,0,5.4,10.2,NE,37,14:51,20.2,69,3,W,15,1019.1,24.9,58,1,ENE,28,1015.1"
        val weatherStation = new BomWeatherStation("Sydney NSW", "SYD",
                                        Position(-33.86, 151.20, 39.0),
                                        "./src/main/resources/sydney201604.csv")
        val env = weatherStation.parse(line)
        assert(env.iata == "SYD" &&
               env.weather.condition == Sunny &&
               env.weather.temperature == 16.6 &&
               env.weather.evaporation == 5.4 &&
               env.weather.windSpeed == 37.0 &&
               env.weather.humidity == 69.0 &&
               env.weather.cloudAmount == 3 &&
               env.weather.mslp == 1019.1)
    }
}
