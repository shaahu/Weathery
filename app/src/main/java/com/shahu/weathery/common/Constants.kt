package com.shahu.weathery.common

/**
 * Created by Shahu Ronghe on 19, September, 2019
 * in Weathery
 */
object Constants {
    //URL's
    const val LOCATION_SHARED_PREFERENCE_NAME = "locations"
    const val OFFLINE_DATA_SHARED_PREFERENCE_NAME = "offline_data"
    const val OPEN_WEATHER_MAP_API_KEY = "fea2dca909cba74286a54e5b6e9abc6c"
    const val OPEN_WEATHER_MAP_BASE_URL = "http://api.openweathermap.org/data/2.5/"
    const val WEATHER_BY_NAME_STRING = "weather?q="
    const val WEATHER_BY_COORDS_STRING = "weather?"
    const val WEATHER_BY_CITY_ID = "weather?id="
    const val WEATHER_FORECAST_BY_CITY_ID = "forecast?id="

    //shahoo URL's
    const val API_BASE_URL = "https://api.shahoo.in/"
    const val API_GET_CITIES = "weathery/search_city/"
    const val WEATHER_ICONS = "data/weather_icons/"

    //Temperature units call
    const val METRIC_UNIT = "&units=metric"
    const val IMPERIAL_UNIT = "&units=imperial"

    //RequestTypes
    const val CURRENT_LOCATION_HTTP_REQUEST = "currentLocationWeatherRequest"
    const val WEATHER_BY_ID_HTTP_REQUEST = "weatherByCityId"
    const val WEATHER_FORECAST_BY_ID_HTTP_REQUEST = "weatherForecastByCityId"
    const val CITIES_DATA_FOR_SEARCH_LIST = "citiesData"

    //WeatherIcons
    const val WEATHER_ICONS_DAY = "day/"
    const val WEATHER_ICONS_NIGHT = "night/"

    //Time
    const val DAY = "day"
    const val NIGHT = "night"

    //onClick bundle constants
    const val CITY_CARD_MODEL = "the_selected_city"
}