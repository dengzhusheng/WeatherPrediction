package com.dzs.weather.test

import org.scalatest.FunSuite
import com.dzs.weather.Environment
import com.dzs.weather.Position
import com.dzs.weather.Weather
import com.dzs.weather.WeatherPredictor
import com.dzs.weather.Condition._
import java.text.SimpleDateFormat

class PredictorTests extends FunSuite {
    test("the Predictor.predictCondition is correct") {
        val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        val date = df.parse("2015-12-23T05:02:12Z")

        // test 1, sunny & increase, return rain
        val sunnyDay = Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Sunny, 12.5, 0, 0, 97, 0, 1004.3)
                           )
        val deltaInc = Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Rain, 0, 0, 0, 0, -1, 1)
                           )
        val predictor = new WeatherPredictor()
        assert(predictor.predictCondition(sunnyDay, deltaInc) == Rain)

        // test 2, rain & drop, return sunny
        val rainDay = Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Rain, 12.5, 0, 0, 97, 0, 1004.3)
                           )
        val deltaDrop = Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Rain, 0, 0, 0, -1, -1, 1)
                           )
        assert(predictor.predictCondition(rainDay, deltaDrop) == Sunny)

        // test 3, rain & increase, return rain
        val deltaInc2 = Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Rain, 0, 0, 0, -1, 1, 1)
                           )
        assert(predictor.predictCondition(rainDay, deltaInc2) == Rain)
    }
/**
 * TODO:
    test("the Predictor.predictTemperature is correct") {
        
    }

    test("the Predictor.predictPressure is correct") {
        
    }

    test("the Predictor.predictPressure is correct") {
        
    }

    test("the Predictor.predictWeather is correct") {
        
    }
*/
}

