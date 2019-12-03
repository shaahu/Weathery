package com.shahu.weathery.helper;

import android.annotation.SuppressLint;

import com.shahu.weathery.model.OpenWeatherMainResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.shahu.weathery.common.Constants.DAY;
import static com.shahu.weathery.common.Constants.NIGHT;

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
public class ValuesConverter {

    private static final String TAG = "ValuesConverter";

    @SuppressLint("DefaultLocale")
    public static String convertTemperatureToCelsius(String temp) {
        //fahrenhiet = (celsius * 9.0/5.0) + 32.0;
        double celsius, kelvin;
        kelvin = Double.parseDouble(temp);
        celsius = kelvin - 273.0;
        return String.format("%.0f", celsius);
    }

    public static DateTime convertUnixTime(long epoch, int secShift) {
        DateTime dateTime = new DateTime((long) epoch * 1000);
        dateTime = dateTime.withZone(DateTimeZone.UTC);
        dateTime = dateTime.plusSeconds(secShift);
        return dateTime;
    }

    public static String getDayNight(OpenWeatherMainResponse openWeatherMainResponse) {
        int secShift = openWeatherMainResponse.getTimezone();
        int sunrise = convertUnixTime(openWeatherMainResponse.getSys().getSunrise(), secShift).getHourOfDay();
        int sunset = convertUnixTime(openWeatherMainResponse.getSys().getSunset(), secShift).getHourOfDay();
        int current = convertUnixTime(openWeatherMainResponse.getDt(), secShift).getHourOfDay();
        if (current > sunrise && current < sunset)
            return DAY;
        return NIGHT;
    }

    public static String getCountryImage(String countryCode) {
        try {
            int flagOffset = 0x1F1E6;
            int asciiOffset = 0x41;
            int firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset;
            int secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset;

            return new String(Character.toChars(firstChar))
                    + new String(Character.toChars(secondChar));
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTimeForCity(long time, int secondsShift) {
        String newTime = null;
        DateTime dateTime = convertUnixTime(time,secondsShift);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEEE, HH:mm");
        newTime =  dateTimeFormatter.print(dateTime);
        return newTime;
    }
}
