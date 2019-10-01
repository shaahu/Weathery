package com.shahu.weathery.Helper;

import android.util.Log;

import com.shahu.weathery.Model.WeatherItem;
import com.shahu.weathery.R;

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

    public static int getDescriptionImageDrawable(WeatherItem weatherItem) {
        Log.e("weather", "getDescriptionImageDrawable: "+weatherItem.toString() );
        final String main = weatherItem.getMain();
        final String desc = weatherItem.getDescription();
        switch (main) {

            case CLEAR_MAIN:
                return getClearDescription(desc);
            case CLOUDS_MAIN:
                return getCloudDescription(desc);

        }

        return R.drawable.overcast;
    }

    private static int getClearDescription(String desc) {
        return R.drawable.autumnsvg;
    }

    private static int getCloudDescription(String desc) {
        final String FEW_CLOUDS = "few clouds";
        final String SCATTERED = "scattered clouds";
        final String BROKEN = "broken clouds";
        final String OVERCAST = "overcast clouds";
        switch (desc){
            case FEW_CLOUDS:
                return R.drawable.few_clouds;
            case SCATTERED:
                return R.drawable.scattered_clouds;
            case BROKEN:
                return R.drawable.broken_cloud;
            case OVERCAST:
                return R.drawable.overcast;
        }
        return 0;
    }
}
