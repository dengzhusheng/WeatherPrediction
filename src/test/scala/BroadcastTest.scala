package com.dzs.weather.test

import org.scalatest.FunSuite
import com.dzs.weather.ConsoleBroadcast
import com.dzs.weather.Environment
import com.dzs.weather.Position
import com.dzs.weather.Weather
import com.dzs.weather.Condition._
import java.text.SimpleDateFormat

class BroadcastTests extends FunSuite {
    test("the Broadcast.format is correct") {
        val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        val date = df.parse("2015-12-23T05:02:12Z")

        val bc = new ConsoleBroadcast()
        val env = bc.format(Environment(
                            "SYD",
                            date,
                            Position(-33.86, 151.21, 39),
                            Weather(Rain, 12.5, 0, 0, 97, 0, 1004.3)
                           ))

        val expect = "SYD|-33.86,151.21,39.0|2015-12-23T05:02:12Z|Rain|12.5|1004.3|97.0"
        assert(env == expect)
    }
}
