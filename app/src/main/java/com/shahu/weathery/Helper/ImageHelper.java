package com.shahu.weathery.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

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
    private static String mTime;

    public static Drawable getDescriptionImageDrawable(CardModel cardModel, Context context) throws IOException {
        mContext = context;
        final String main = cardModel.getWeatherItem().getMain();
        final String desc = cardModel.getWeatherItem().getDescription();
        mTime = cardModel.getDayNight();
        switch (main) {
            case CLEAR_MAIN:
                return getClearDescription();
            case CLOUDS_MAIN:
                return getCloudDescription(desc);
            case SNOW_MAIN:
                return getSnowDescription(desc);
            case THUNDERSTORM_MAIN:
                return getThunderstormDescription(desc);
            case DRIZZLE_MAIN:
                return getDrizzleDescription();
            case RAIN_MAIN:
                return getRainDescription(desc);
            default:
                return getAtmosphereDescription(main);
        }

    }

    private static Drawable getAtmosphereDescription(String main) throws IOException {
        final String MIST = "Mist";
        final String SMOKE = "Smoke";
        final String HAZE = "Haze";
        final String DUST = "Dust";
        final String FOG = "Fog";
        final String SAND = "Sand";
        final String ASH = "Ash";
        final String TORNADO = "Tornado";
        switch (main) {
            case SMOKE:
                return buildImagePath("smaoky");
            case HAZE:
                return buildImagePath("haze");
            case DUST:
                return buildImagePath("dust");
            case FOG:
                return buildImagePath("foggy");
            case TORNADO:
                return buildImagePath("tornado");
            case MIST:
                return buildImagePath("fair");
        }
        return buildImagePath("default_weather_icon");
    }

    private static Drawable getRainDescription(String desc) throws IOException {
        final String LIGHT_RAIN = "light rain";
        final String LIGHT_INTENSITY_SHOWER_RAIN = "light intensity shower rain\t";
        switch (desc) {
            case LIGHT_INTENSITY_SHOWER_RAIN:
            case LIGHT_RAIN:
                return buildImagePath("light_rain");

        }
        return buildImagePath("freezing_rain");
    }

    private static Drawable getDrizzleDescription() throws IOException {
        return buildImagePath("drizzle");
    }

    private static Drawable getThunderstormDescription(String desc) throws IOException {
        final String THUNDERSTORM_LIGHT_RAIN = "thunderstorm with light rain";
        final String THUNDERSTORM_RAIN = "thunderstorm with rain";
        final String THUNDERSTORM_HEAVY_RAIN = "thunderstorm with heavy rain";
        final String LIGHT_THUNDERSTORM = "light thunderstorm";
        final String THUNDERSTORM = "thunderstorm";
        final String HEAVY_THUNDERSTORM = "heavy thunderstorm";
        final String RAGGED_THUNDERSTORM = "ragged_thunderstorm";
        final String THUNDERSTORM_WITH_LIGHT_DRIZZLE = "thunderstorm with light drizzle";
        final String THUNDERSTORM_WITH_DRIZZLE = "thunderstorm with drizzle";
        final String THUNDERSTORM_WITH_HEAVY_DRIZZLE = "thunderstorm with heavy drizzle";

        switch (desc) {
            case LIGHT_THUNDERSTORM:
            case THUNDERSTORM_LIGHT_RAIN:
                return buildImagePath("isolated_thunderstorms");
            case RAGGED_THUNDERSTORM:
                return buildImagePath("tornado");
            case THUNDERSTORM_WITH_DRIZZLE:
            case THUNDERSTORM_WITH_HEAVY_DRIZZLE:
            case THUNDERSTORM_WITH_LIGHT_DRIZZLE:
                return buildImagePath("freezing_drizzle");
        }
        return buildImagePath("thundershowers");
    }

    private static Drawable getSnowDescription(String desc) throws IOException {
        final String LIGHT_SNOW = "light snow";
        final String SNOW = "Snow";
        final String HEAVY_SNOW = "Heavy snow";
        final String SLEET = "Sleet";
        final String LIGHT_SHOWER_SLEET = "Light shower sleet";
        final String SHOWER_SLEET = "Shower sleet";
        final String LIGHT_RAIN_AND_SNOW = "Light rain and snow";
        final String RAIN_AND_SNOW = "Rain and snow";
        final String LIGHT_SHOWER_SNOW = "Light shower snow";
        final String SHOWER_SNOW = "Shower snow";
        final String HEAVY_SHOWER_SNOW = "Heavy shower snow";
        switch (desc) {
            case LIGHT_SNOW:
            case LIGHT_SHOWER_SNOW:
            case SHOWER_SNOW:
                return buildImagePath("light_snow_showers");
            case SLEET:
                return buildImagePath("sleet");
            case SHOWER_SLEET:
                return buildImagePath("mixed_rain_and_sleet");
            case LIGHT_SHOWER_SLEET:
                return buildImagePath("mixed_snow_and_sleet");
            case SNOW:
                return buildImagePath("snow");
            case HEAVY_SNOW:
            case HEAVY_SHOWER_SNOW:
                return buildImagePath("heavy_snow");
            case RAIN_AND_SNOW:
            case LIGHT_RAIN_AND_SNOW:
                return buildImagePath("mixed_rain_and_snow");

        }
        return buildImagePath("snow");
    }

    private static Drawable getClearDescription() throws IOException {
        return buildImagePath("clear");
    }

    private static Drawable getCloudDescription(String desc) throws IOException {
        final String FEW_CLOUDS = "few clouds";
        final String SCATTERED = "scattered clouds";
        final String BROKEN = "broken clouds";
        final String OVERCAST = "overcast clouds";
        switch (desc) {
            case FEW_CLOUDS:
                return buildImagePath("fair");
            case SCATTERED:
                return buildImagePath("cloudy");
            case BROKEN:
                return buildImagePath("partly_cloudy");
            case OVERCAST:
                return buildImagePath("mostly_cloudy");
        }
        return buildImagePath("cloudy");
    }

    private static Drawable buildImagePath(String fileName) throws IOException {
        InputStream ims = null;
        if (mTime.equals(DAY))
            ims = mContext.getAssets().open(WEATHER_ICONS_DAY + fileName + ".png");
        if (mTime.equals(NIGHT))
            ims = mContext.getAssets().open(WEATHER_ICONS_NIGHT + fileName + ".png");
        return Drawable.createFromStream(ims, null);
    }
}
