package com.dzs.weather

import com.dzs.weather.Condition._
import java.util.Calendar

/** Weather predictor
 *  
 *  A toy model for weather prediction. Algorithms used here for weather conditions
 *  forecast are really basic and intuitive and just for fun, based on a simple statistics of
 *  historic environment measurement datasets from Bureau of Meteorology Australia
 */
class WeatherPredictor {
    /** Factor constants */
    val DateFactor = 0.05
    val EvaporationFactor = 0.3
    val WindSpeedFactor = 0.01
    val CloudAmountFactor = 0.3
    val TemperatureFactor = 0.3
    val HumidityFactor = 0.3

    /** Predict weather condition(e.g. sunny, rain etc.).
      *
      * Predict weather condition based on environment measurement
      * on today and difference from yesterday
      * @param today the environment measurement on today
      * @param delta environment measurement difference from yesterday
      * @return predict weather condition
      */
    def predictCondition(today: Environment, delta: Environment) : Condition = {
        /** For humidity, cloudAmount, and mslp measurements
          * if it's sunny and two of them increase => rain
          * if it's rain and  two of them increase drop => sunny
          */
        val drop = (delta.weather.humidity
                    * delta.weather.cloudAmount
                    * delta.weather.mslp) > 0

        val condition = {
            if (today.weather.condition == Sunny && drop == false) Rain
            else if (today.weather.condition == Rain && drop == true) Sunny
            else today.weather.condition
        }

        condition
    }

    /** Predict temperature
      *
      * Predict temperature based on environment measurement
      * on today and difference from yesterday
      * @param today the environment measurement on today
      * @param delta environment measurement difference from yesterday
      * @return predict temperature
      */
    def predictTemperature(today: Environment, delta: Environment) : Double = {
        /** Factors affect temperature
          * 1. Date: Jul. -> Dec., +0.05/day; Jan. -> Jun. -0.05/day
          * 2. Evaporation: -(delta*0.3)
          * 3. windSpeed: -(delta*0.01)
          * 4. cloud: -(delta* 0.3)
          */
        val cal = Calendar.getInstance()
        cal.setTime(today.date)
        val month = cal.get(Calendar.MONTH) // NOTE: month starts with 0 (Jan.)
        val datePrefix = if (month >= 0 && month < 6) -1 else 1

        val temperature = today.weather.temperature
                + datePrefix * DateFactor
                - delta.weather.evaporation * EvaporationFactor
                - delta.weather.windSpeed * WindSpeedFactor
                - delta.weather.cloudAmount * CloudAmountFactor

        temperature
    }

    /** Predict pressure
      *
      * Predict pressure based on environment measurement
      * on today and difference from yesterday
      * @param today the environment measurement on today
      * @param delta environment measurement difference from yesterday
      * @return predict pressure
      */
    def predictPressure(today: Environment, delta: Environment) : Double = {
        /** Factors affect pressure
          * 1. temperature: delta*0.3
          * 2. humidity: -(delta*0.3)
          * 3. evaporation: delta*0.3
          */
        val pressure = today.weather.mslp
                - delta.weather.temperature * TemperatureFactor
                - delta.weather.humidity * HumidityFactor
                - delta.weather.evaporation * EvaporationFactor

        pressure
    }

    /** Predict humidity
      *
      * Predict pressure based on environment measurement
      * on today and difference from yesterday
      * @param today the environment measurement on today
      * @param delta environment measurement difference from yesterday
      * @return predict humidity
      */
    def predictHumidity(today: Environment, delta: Environment) : Double = {
        /**
          * Factors affect humidity
          * 1. Date, Sep. -> Jan., +0.05/day; Feb. -> Aug. -0.05/day
          * 2. temperature: delta*0.3
          * 3. evaporation: delta*0.3
          */
        val cal = Calendar.getInstance()
        cal.setTime(today.date)
        val month = cal.get(Calendar.MONTH) // NOTE: month starts with 0 (Jan.)
        val datePrefix = if (month > 0 && month < 8) -1 else 1

        val humidity = today.weather.humidity
                + datePrefix * DateFactor
                - delta.weather.temperature * TemperatureFactor
                - delta.weather.evaporation * EvaporationFactor

        humidity
    }

    /** Predict weather
      *
      * Predict pressure based on environment measurement
      * on today and difference from yesterday
      * @param today the environment measurement on today
      * @param delta environment measurement difference from yesterday
      * @return predict weather
      */
    def predictWeather(today: Environment, delta: Environment) : Environment = {
        /** date of tomorrow */
        val cal = Calendar.getInstance()
        cal.setTime(today.date); 
        cal.add(Calendar.DATE, 1);

        /** predicted environment weather */
        Environment (
            today.iata,
            cal.getTime(),
            today.position,
            Weather(
                    predictCondition(today, delta),
                    predictTemperature(today, delta),
                    0, // Not used
                    0, // Not used
                    predictHumidity(today, delta),
                    0, // Not used
                    predictPressure(today, delta)
            )
        )
    }
}
