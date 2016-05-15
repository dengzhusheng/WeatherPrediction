package com.dzs.weather

import java.util.Date
import java.text.SimpleDateFormat

/** Three dimensional coordinate
  *
  * @param latitude latitude of the position
  * @param longtitude longtitude of the position
  * @param altitude altitude of the position
  */
case class Position (
    val latitude: Double,
    val longtitude: Double,
    val alitude: Double
    )

/** Weather condition
  *
  * Enum type and values for weather condition
  */
object Condition extends Enumeration {
    type Condition = Value
    val Sunny, Rain, Snow = Value
}

import Condition._
/** Measurement of meteorology
  *
  * Weather model defined based on Bureau of Meteorology Australia
  * weather dataset
  */
case class Weather (
    val condition: Condition,
    val temperature: Double,
    val evaporation: Double,
    val windSpeed: Double,
    val humidity: Double,
    val cloudAmount: Double,
    val mslp: Double // mean sea level pressure
	)

/** Toy model of the environment measured in specific location
 *  
 *  @param iata IATA code for the location
 *  @param date date the environment measured
 *  @param position position of the location
 *  @param weather weather measurement
  */
case class Environment (
    val iata: String,
    val date: Date,
    val position: Position,
    val weather: Weather
    )
