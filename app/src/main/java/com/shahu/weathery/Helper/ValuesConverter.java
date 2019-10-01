package com.shahu.weathery.Helper;

import android.annotation.SuppressLint;

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
public class ValuesConverter {

    @SuppressLint("DefaultLocale")
    public static String convertTemperatureToCelsius(String temp) {
        //fahrenhiet = (celsius * 9.0/5.0) + 32.0;
        double celsius, kelvin;
        kelvin = Double.parseDouble(temp);
        celsius = kelvin - 273.0;
        return String.format("%.0f", celsius);
    }
}
