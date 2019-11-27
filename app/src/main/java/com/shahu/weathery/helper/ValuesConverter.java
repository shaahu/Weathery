package com.shahu.weathery.helper;

import android.annotation.SuppressLint;
import android.util.Log;

import com.shahu.weathery.model.OpenWeatherMainResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String convertUnixTime(long epoch) {
        Date date = new java.util.Date(epoch * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }

    public static String getDayNight(OpenWeatherMainResponse openWeatherMainResponse) {
        long sunrise = openWeatherMainResponse.getSys().getSunrise();
        long sunset = openWeatherMainResponse.getSys().getSunset();
        long current = openWeatherMainResponse.getDt();
        Log.d(TAG, "getDayNight: ford " + sunrise + " " + sunset + " " + current);

        if (current < sunset && current > sunrise) {
            return DAY;
        }
        if (current > sunset) {
            return NIGHT;
        }
        return DAY;
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
}
