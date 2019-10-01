package com.shahu.weathery.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.shahu.weathery.Model.WeatherItem;

import java.io.IOException;
import java.io.InputStream;

import static com.shahu.weathery.Common.Constants.WEATHER_ICONS_DAY;

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
public class ImageHelper {

    private static final String THUNDERSTORM_MAIN = "Thunderstorm";
    private static final String DRIZZLE_MAIN = "Drizzle";
    private static final String RAIN_MAIN = "Rain";
    private static final String SNOW_MAIN = "Snow";
    private static final String CLEAR_MAIN = "Clear";
    private static final String CLOUDS_MAIN = "Clouds";
    private static Context mContext;

    public static Drawable getDescriptionImageDrawable(WeatherItem weatherItem, Context context) throws IOException {
        mContext = context;
        Log.e("weather", "getDescriptionImageDrawable: " + weatherItem.toString());
        final String main = weatherItem.getMain();
        final String desc = weatherItem.getDescription();
        switch (main) {

            case CLEAR_MAIN:
                return getClearDescription(desc);
            case CLOUDS_MAIN:
                return getCloudDescription(desc);

        }

        return getImageStream("clear");
    }

    private static Drawable getClearDescription(String desc) throws IOException {
        return getImageStream("fair");
    }

    private static Drawable getCloudDescription(String desc) throws IOException {
        final String FEW_CLOUDS = "few clouds";
        final String SCATTERED = "scattered clouds";
        final String BROKEN = "broken clouds";
        final String OVERCAST = "overcast clouds";
        switch (desc) {
            case FEW_CLOUDS:
                return getImageStream("clear");

        }
        return null;
    }

    private static Drawable getImageStream(String fileName) throws IOException {
        InputStream ims = mContext.getAssets().open(WEATHER_ICONS_DAY + fileName + ".png");
        return Drawable.createFromStream(ims, null);
    }
}
