package com.shahu.weathery.helper

import com.shahu.weathery.common.Constants
import com.shahu.weathery.model.CardModel

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
object ImageHelper {
    private const val THUNDERSTORM_MAIN = "Thunderstorm"
    private const val DRIZZLE_MAIN = "Drizzle"
    private const val RAIN_MAIN = "Rain"
    private const val SNOW_MAIN = "Snow"
    private const val CLEAR_MAIN = "Clear"
    private const val CLOUDS_MAIN = "Clouds"
    private const val FILE_NOT_FOUND_EXP = "na"
    private var mTime: String? = null
    @JvmStatic
    fun getDescriptionImageDrawable(cardModel: CardModel?): String {
        val main = ObjectExtractor.extractMain(cardModel)
        val desc = ObjectExtractor.extractDescription(cardModel)
        mTime = ObjectExtractor.extractTime(cardModel)
        return when (main) {
            FILE_NOT_FOUND_EXP -> buildImagePath("default_weather_icon")
            CLEAR_MAIN -> clearDescription
            CLOUDS_MAIN -> getCloudDescription(desc)
            SNOW_MAIN -> getSnowDescription(desc)
            THUNDERSTORM_MAIN -> getThunderstormDescription(desc)
            DRIZZLE_MAIN -> drizzleDescription
            RAIN_MAIN -> getRainDescription(desc)
            else -> getAtmosphereDescription(main)
        }
    }

    private fun getAtmosphereDescription(main: String): String {
        val MIST = "Mist"
        val SMOKE = "Smoke"
        val HAZE = "Haze"
        val DUST = "Dust"
        val FOG = "Fog"
        val SAND = "Sand"
        val ASH = "Ash"
        val TORNADO = "Tornado"
        when (main) {
            SMOKE -> return buildImagePath("smaoky")
            HAZE -> return buildImagePath("haze")
            DUST -> return buildImagePath("dust")
            FOG -> return buildImagePath("foggy")
            TORNADO -> return buildImagePath("tornado")
            MIST -> return buildImagePath("fair")
        }
        return buildImagePath("default_weather_icon")
    }

    private fun getRainDescription(desc: String): String {
        val LIGHT_RAIN = "light rain"
        val LIGHT_INTENSITY_SHOWER_RAIN = "light intensity shower rain\t"
        when (desc) {
            LIGHT_INTENSITY_SHOWER_RAIN, LIGHT_RAIN -> return buildImagePath("light_rain")
        }
        return buildImagePath("freezing_rain")
    }

    private val drizzleDescription: String
        private get() = buildImagePath("drizzle")

    private fun getThunderstormDescription(desc: String): String {
        val THUNDERSTORM_LIGHT_RAIN = "thunderstorm with light rain"
        val THUNDERSTORM_RAIN = "thunderstorm with rain"
        val THUNDERSTORM_HEAVY_RAIN = "thunderstorm with heavy rain"
        val LIGHT_THUNDERSTORM = "light thunderstorm"
        val THUNDERSTORM = "thunderstorm"
        val HEAVY_THUNDERSTORM = "heavy thunderstorm"
        val RAGGED_THUNDERSTORM = "ragged_thunderstorm"
        val THUNDERSTORM_WITH_LIGHT_DRIZZLE = "thunderstorm with light drizzle"
        val THUNDERSTORM_WITH_DRIZZLE = "thunderstorm with drizzle"
        val THUNDERSTORM_WITH_HEAVY_DRIZZLE = "thunderstorm with heavy drizzle"
        when (desc) {
            LIGHT_THUNDERSTORM, THUNDERSTORM_LIGHT_RAIN -> return buildImagePath("isolated_thunderstorms")
            RAGGED_THUNDERSTORM -> return buildImagePath("tornado")
            THUNDERSTORM_WITH_DRIZZLE, THUNDERSTORM_WITH_HEAVY_DRIZZLE, THUNDERSTORM_WITH_LIGHT_DRIZZLE -> return buildImagePath("freezing_drizzle")
        }
        return buildImagePath("thundershowers")
    }

    private fun getSnowDescription(desc: String): String {
        val LIGHT_SNOW = "light snow"
        val SNOW = "Snow"
        val HEAVY_SNOW = "Heavy snow"
        val SLEET = "Sleet"
        val LIGHT_SHOWER_SLEET = "Light shower sleet"
        val SHOWER_SLEET = "Shower sleet"
        val LIGHT_RAIN_AND_SNOW = "Light rain and snow"
        val RAIN_AND_SNOW = "Rain and snow"
        val LIGHT_SHOWER_SNOW = "Light shower snow"
        val SHOWER_SNOW = "Shower snow"
        val HEAVY_SHOWER_SNOW = "Heavy shower snow"
        when (desc) {
            LIGHT_SNOW, LIGHT_SHOWER_SNOW, SHOWER_SNOW -> return buildImagePath("light_snow_showers")
            SLEET -> return buildImagePath("sleet")
            SHOWER_SLEET -> return buildImagePath("mixed_rain_and_sleet")
            LIGHT_SHOWER_SLEET -> return buildImagePath("mixed_snow_and_sleet")
            SNOW -> return buildImagePath("snow")
            HEAVY_SNOW, HEAVY_SHOWER_SNOW -> return buildImagePath("heavy_snow")
            RAIN_AND_SNOW, LIGHT_RAIN_AND_SNOW -> return buildImagePath("mixed_rain_and_snow")
        }
        return buildImagePath("snow")
    }

    private val clearDescription: String
        private get() = buildImagePath("clear")

    private fun getCloudDescription(desc: String): String {
        val FEW_CLOUDS = "few clouds"
        val SCATTERED = "scattered clouds"
        val BROKEN = "broken clouds"
        val OVERCAST = "overcast clouds"
        when (desc) {
            FEW_CLOUDS -> return buildImagePath("fair")
            SCATTERED -> return buildImagePath("cloudy")
            BROKEN -> return buildImagePath("partly_cloudy")
            OVERCAST -> return buildImagePath("mostly_cloudy")
        }
        return buildImagePath("cloudy")
    }

    private fun buildImagePath(fileName: String): String {
        var path = Constants.API_BASE_URL + Constants.WEATHER_ICONS + "/day/clear"
        if (mTime == Constants.DAY) path = Constants.API_BASE_URL + Constants.WEATHER_ICONS + Constants.WEATHER_ICONS_DAY + fileName + ".png"
        if (mTime == Constants.NIGHT) path = Constants.API_BASE_URL + Constants.WEATHER_ICONS + Constants.WEATHER_ICONS_NIGHT + fileName + ".png"
        return path
    }
}