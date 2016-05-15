package com.dzs.weather

import java.io.PrintWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat

/** Base class for all broadcasts */
abstract class Broadcast {
    /** Abstract broadcast method.
      *
      * @param environment the environment measurement to be broadcast
      */
    def broadcast(environment: Environment): Unit

    /** Abstract broadcast resource close. */
    def close(): Unit

    /** Format environment measure to string.
      *
      * Format the input environment into a string according to the requirement
      * in "WeatherData.pdf", e.g.
      * SYD|-33.86,151.21,39|2015-12-23T05:02:12Z|Rain|+12.5|1004.3|97
      * @param environment the environment measurement
      * @return formated string
      */
    def format(environment: Environment): String = {
        val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        val date = df.format(environment.date)

        val sb = new StringBuilder()
        sb.append(environment.iata)
          .append('|').append(environment.position.latitude)
          .append(',').append(environment.position.longtitude)
          .append(',').append(environment.position.alitude)
          .append('|').append(date)
          .append('|').append(environment.weather.condition)
          .append('|').append(environment.weather.temperature)
          .append('|').append(environment.weather.mslp)
          .append('|').append(environment.weather.humidity)

        sb.toString()
    }
}

/** Broadcast to console */
class ConsoleBroadcast extends Broadcast {
    /** Broadcast environment to console.
      *
      * @param environment the environment measurement to be broadcast
      */
    def broadcast(environment: Environment): Unit = {
        println(format(environment))
    }
    
    def close(): Unit = None
}

/** Broadcast to a specific file
  *
  * @param file the output file name
  */
class FileBroadcast(file: String) extends Broadcast {
    // create if file not exist, otherwise overwrite
    val pw = new PrintWriter(new FileOutputStream(file, false))

    /** Broadcast environment to text file.
      *
      * @param environment the environment measurement to be broadcast
      */
    def broadcast(environment: Environment): Unit = {
        pw.println(format(environment))
    }

    /** Close text file. */
    def close(): Unit = {
        pw.close()
    }
}
