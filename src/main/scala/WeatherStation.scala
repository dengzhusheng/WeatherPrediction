package com.dzs.weather

import scala.io.Source
import com.dzs.weather.Condition._
import java.text.SimpleDateFormat
import java.nio.charset.CodingErrorAction
import scala.io.Codec
import java.lang.NumberFormatException

/** Abstract encapsulation of the weather station
  *
  * @param name the weather station's name
  * @param iata IATA code for the city where weather station locates
  * @param position 3D coordinate of the weather station
  */
abstract class WeatherStation (
    val name: String,
    val iata: String,
    val position: Position
    ) {

    /** Measure the environment and process the data.
      *
      * @param f data handle method
      */
    def measure(f: (Environment, Environment) => Unit): Unit
}

/** Bureau of Meteorology Australia weather station
  *
  * BomWeatherStation class defines the weather station capable of parsing
  * measurement dataset from Bureau of Meteorology Australia (url: http://www.bom.gov.au/).
  * @param name the weather station's name
  * @param iata IATA code for the city where weather station locates
  * @param position 3D coordinate of the weather station
  * @param measureFile input dataset file measured by the weather station
  */
class BomWeatherStation (
    override val name: String,
    override val iata: String,
    override val position: Position,
    val measureFile: String
    ) extends WeatherStation(name, name, position) {

    /** Calculate the change of environment fields between two days.
      *
      * @param today the environment measurement on today
      * @param yesterday the environment measurement on today
      * @return the difference of the environment measurements
      */
    def delta(today: Environment, yesterday: Environment): Environment = {
            Environment (
                    yesterday.iata,
                    yesterday.date,
                    yesterday.position,
                    Weather(
                            yesterday.weather.condition,
                            today.weather.temperature - yesterday.weather.temperature,
                            today.weather.evaporation - yesterday.weather.evaporation,
                            today.weather.windSpeed - yesterday.weather.windSpeed,
                            today.weather.humidity - yesterday.weather.humidity,
                            today.weather.cloudAmount - yesterday.weather.cloudAmount,
                            today.weather.mslp - yesterday.weather.mslp
                            )
                    )
    }

    /** Measure the environment and process the data.
      *
      * @param f data handle method
      */
    def measure(f: (Environment, Environment) => Unit): Unit = {
        implicit val codec = Codec("UTF-8")
        codec.onMalformedInput(CodingErrorAction.REPLACE)
        codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
        val source = Source.fromFile(measureFile)
        var yesterday : Environment = null
        try {
            source.getLines().foreach(line => {
                  if (line.contains('"') == false && line.size > 0) {
                      try {
                          // parse each line, just skip those malformed lines
                          val today = parse(line)
                          if (yesterday != null) f(today, delta(today, yesterday))
                          yesterday = today
                      }catch {
                          case e: IllegalArgumentException => println(e.toString())
                          case _: Throwable => None // TODO: println
                      }
                  }
            })
        } finally {
            source.close()
        }
    }

    /** Convert a string to double, default value if failed.
      *
      * @param string string to be converted
      * @param default default value if failed
      * @return converted double value
      */
    def stringToDouble(string: String, default: Double): Double = {
        var value = default
        try {
            value = string.toDouble
        } catch {
            case ex: NumberFormatException => None
        }
        value
    }

    /** Parse the weather dataset released by Bureau of Meteorology Australia weather station.
      *
      * Sample input line format:
      * ,2016-04-1,16.6,25.6,0,5.4,10.2,NE,37,14:51,20.2,69,3,W,15,1019.1,24.9,58,1,ENE,28,1015.1
      * Fields separated by ',' where,
      * field 0 empty
      * field 1 date
      * field 2 temperature
      * field 4 rain
      * field 5 evaporation
      * field 8 wind speed
      * field 11 humidity
      * field 12 cloud amount
      * field 15 mean sea level pressure
      * @param line sting line of the weather dataset
      * @return environment measurement
      */
    def parse(line: String): Environment = {
        val fields = line.split(',')
        if (fields.length != 22) throw new IllegalArgumentException(s"invalid line: $line")

        val formatter = new SimpleDateFormat("yyyy-MM-dd");
        val date = formatter.parse(fields(1))

        val rain = stringToDouble(fields(4), 0)
        val condition = if (rain > 0) Rain else Sunny

        Environment(
                iata,
                date,
                position,
                Weather(condition,
                    stringToDouble(fields(2), 20), // temperature: Double
                    stringToDouble(fields(5), 1), // evaporation: Double
                    stringToDouble(fields(8), 20), // val windSpeed: Double
                    stringToDouble(fields(11), 50), // val humidity: Double
                    stringToDouble(fields(12), 1), // val cloudAmount: Double
                    stringToDouble(fields(15), 1000) // val mslp: Double // mean sea level pressure
         ))
    }
}

