package com.shahu.weathery.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.shahu.weathery.Model.CardModel;

import java.io.IOException;
import java.io.InputStream;

import static com.shahu.weathery.Common.Constants.DAY;
import static com.shahu.weathery.Common.Constants.NIGHT;
import static com.shahu.weathery.Common.Constants.WEATHER_ICONS_DAY;
import static com.shahu.weathery.Common.Constants.WEATHER_ICONS_NIGHT;

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

    public static Drawable getDescriptionImageDrawable(CardModel cardModel, Context context) throws IOException {
        mContext = context;
        final String main = cardModel.getWeatherItem().getMain();
        final String desc = cardModel.getWeatherItem().getDescription();
        String time = cardModel.getDayNight();
        Log.d("skull", "getDescriptionImageDrawable: " + time);
        switch (main) {
            case CLEAR_MAIN:
                return getClearDescription(desc, time);
            case CLOUDS_MAIN:
                return getCloudDescription(desc, time);

        }

        return getImageStream("clear", time);
    }

    private static Drawable getClearDescription(String desc, String time) throws IOException {
        return getImageStream("fair", time);
    }

    private static Drawable getCloudDescription(String desc, String time) throws IOException {
        final String FEW_CLOUDS = "few clouds";
        final String SCATTERED = "scattered clouds";
        final String BROKEN = "broken clouds";
        final String OVERCAST = "overcast clouds";
        switch (desc) {
            case FEW_CLOUDS:
                return getImageStream("clear", time);

        }
        return null;
    }

    private static Drawable getImageStream(String fileName, String time) throws IOException {
        InputStream ims = null;
        if (time.equals(DAY))
            ims = mContext.getAssets().open(WEATHER_ICONS_DAY + fileName + ".png");
        if (time.equals(NIGHT))
            ims = mContext.getAssets().open(WEATHER_ICONS_NIGHT + fileName + ".png");
        return Drawable.createFromStream(ims, null);
    }
}
