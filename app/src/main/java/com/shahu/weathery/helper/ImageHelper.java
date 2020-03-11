package com.shahu.weathery.helper;

import com.shahu.weathery.model.CardModel;

import static com.shahu.weathery.common.Constants.ADKRAFT_BASE_URL;
import static com.shahu.weathery.common.Constants.ADKRAFT_WEATHER_IMAGES;
import static com.shahu.weathery.common.Constants.DAY;
import static com.shahu.weathery.common.Constants.NIGHT;
import static com.shahu.weathery.common.Constants.WEATHER_ICONS_DAY;
import static com.shahu.weathery.common.Constants.WEATHER_ICONS_NIGHT;

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
    private static final String FILE_NOT_FOUND_EXP = "na";
    private static String mTime;

    public static String getDescriptionImageDrawable(CardModel cardModel) {
        final String main = ObjectExtractor.extractMain(cardModel);
        final String desc = ObjectExtractor.extractDescription(cardModel);
        mTime = ObjectExtractor.extractTime(cardModel);
        switch (main) {
            case FILE_NOT_FOUND_EXP:
                return buildImagePath("default_weather_icon");
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

    private static String getAtmosphereDescription(String main) {
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

    private static String getRainDescription(String desc) {
        final String LIGHT_RAIN = "light rain";
        final String LIGHT_INTENSITY_SHOWER_RAIN = "light intensity shower rain\t";
        switch (desc) {
            case LIGHT_INTENSITY_SHOWER_RAIN:
            case LIGHT_RAIN:
                return buildImagePath("light_rain");

        }
        return buildImagePath("freezing_rain");
    }

    private static String getDrizzleDescription() {
        return buildImagePath("drizzle");
    }

    private static String getThunderstormDescription(String desc) {
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

    private static String getSnowDescription(String desc) {
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

    private static String getClearDescription() {
        return buildImagePath("clear");
    }

    private static String getCloudDescription(String desc) {
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

    private static String buildImagePath(String fileName) {
        String path = ADKRAFT_BASE_URL + ADKRAFT_WEATHER_IMAGES + "/day/clear";
        if (mTime.equals(DAY))
            path = ADKRAFT_BASE_URL + ADKRAFT_WEATHER_IMAGES + WEATHER_ICONS_DAY + fileName + ".png";
        if (mTime.equals(NIGHT))
            path = ADKRAFT_BASE_URL + ADKRAFT_WEATHER_IMAGES + WEATHER_ICONS_NIGHT + fileName + ".png";
        return path;
    }
}
