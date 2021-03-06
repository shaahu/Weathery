package com.shahu.weathery.helper

import android.annotation.SuppressLint
import com.shahu.weathery.common.Constants
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
object ValuesConverter {
    private const val TAG = "ValuesConverter"

    @JvmStatic
    @SuppressLint("DefaultLocale")
    fun convertTemperatureToCelsius(temp: String): String {
        //fahrenhiet = (celsius * 9.0/5.0) + 32.0;
        val celsius: Double
        val kelvin: Double = temp.toDouble()
        celsius = kelvin - 273.0
        return String.format("%.0f", celsius)
    }

    private fun convertUnixTime(epoch: Long, secShift: Int): DateTime {
        var dateTime = DateTime(epoch * 1000)
        dateTime = dateTime.withZone(DateTimeZone.UTC)
        dateTime = dateTime.plusSeconds(secShift)
        return dateTime
    }

    fun getDayNight(timezone: Int, sunriseTime: Int, sunsetTime: Int, dtIncoming: Int): String {
        val sunrise = sunriseTime.toLong().let { convertUnixTime(it, timezone).hourOfDay }
        val sunset = sunsetTime.toLong().let { convertUnixTime(it, timezone).hourOfDay }
        val current = convertUnixTime(dtIncoming.toLong(), timezone).hourOfDay
        return if (current in (sunrise.plus(1)) until sunset) Constants.DAY else Constants.NIGHT
    }

    @JvmStatic
    fun getCountryImage(countryCode: String?): String? {
        return try {
            val flagOffset = 0x1F1E6
            val asciiOffset = 0x41
            val firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset
            val secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset
            (String(Character.toChars(firstChar))
                    + String(Character.toChars(secondChar)))
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun getTimeForCity(time: Long, secondsShift: Int): String {
        val dateTime = convertUnixTime(time, secondsShift)
        val dateTimeFormatter = DateTimeFormat.forPattern("E, HH:mm")
        return dateTimeFormatter.print(dateTime)
    }

    @JvmStatic
    fun getDateForCity(currentTime: Long, timezone: Int): String {
        val dateTime = convertUnixTime(currentTime, timezone)
        val dateTimeFormatter = DateTimeFormat.forPattern("EEEE, dd MMM")
        return dateTimeFormatter.print(dateTime)
    }

    @JvmStatic
    fun getTimeOnlyForCity(currentTime: Long, timezone: Int): String {
        val dateTime = convertUnixTime(currentTime, timezone)
        val dateTimeFormatter = DateTimeFormat.forPattern("HH:mm")
        return dateTimeFormatter.print(dateTime)
    }

    fun getHourOfTheDayByEpoch(time: Long, timezone: Int): String? {
        val dateTime = convertUnixTime(time, timezone)
        val dateTimeFormatter = DateTimeFormat.forPattern("HH")
        return dateTimeFormatter.print(dateTime)
    }

    fun getDayOfTheWeek(time: Int, timezone: Int): String? {
        val dateTime = convertUnixTime(time.toLong(), timezone)
        val dateTimeFormatter = DateTimeFormat.forPattern("EEEE")
        return dateTimeFormatter.print(dateTime)
    }
}