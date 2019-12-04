package com.shahu.weathery.common;

/**
 * Created by Shahu Ronghe on 19, September, 2019
 * in Weathery
 */
public final class Constants {
    final static String LOCATION_SHARED_PREFERENCE_NAME = "locations";
    final static String OPEN_WEATHER_MAP_API_KEY = "&APPID=fea2dca909cba74286a54e5b6e9abc6c";
    final static String OPEN_WEATHER_MAP_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    final static String WEATHER_BY_NAME_STRING = "weather?q=";
    final static String WEATHER_BY_COORDS_STRING = "weather?";
    final static String WEATHER_BY_CITY_ID = "weather?id=";
    final static String WEATHER_FORECAST_BY_CITY_ID = "forecast?id=";

    //Temperature units call
    public final static String METRIC_UNIT = "&units=metric";
    public final static String IMPERIAL_UNIT = "&units=imperial";


    //RequestTypes
    public final static String CURRENT_LOCATION_HTTP_REQUEST = "currentLocationWeatherRequest";
    public final static String WEATHER_BY_ID_HTTP_REQUEST = "weatherByCityId";
    public final static String WEATHER_FORECAST_BY_ID_HTTP_REQUEST = "weatherForecastByCityId";

    //WeatherIcons
    private static final String WEATHER_ICONS_ASSET_PATH = "w_icons/";
    public static final String WEATHER_ICONS_DAY = WEATHER_ICONS_ASSET_PATH+"day/";
    public static final String WEATHER_ICONS_NIGHT = WEATHER_ICONS_ASSET_PATH+"night/";

    //Time
    public static final String DAY = "day";
    public static final String NIGHT = "night";
}
