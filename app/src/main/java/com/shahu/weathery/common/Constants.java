package com.shahu.weathery.common;

/**
 * Created by Shahu Ronghe on 19, September, 2019
 * in Weathery
 */
public final class Constants {
    //URL's
    final static String LOCATION_SHARED_PREFERENCE_NAME = "locations";
    final static String OFFLINE_DATA_SHARED_PREFERENCE_NAME = "offline_data";
    final static String OPEN_WEATHER_MAP_API_KEY = "&APPID=fea2dca909cba74286a54e5b6e9abc6c";
    final static String OPEN_WEATHER_MAP_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    final static String WEATHER_BY_NAME_STRING = "weather?q=";
    final static String WEATHER_BY_COORDS_STRING = "weather?";
    final static String WEATHER_BY_CITY_ID = "weather?id=";
    final static String WEATHER_FORECAST_BY_CITY_ID = "forecast?id=";
    //AdKraft URL's
    public final static String API_BASE_URL = "https://api.shahoo.in/";
    final static String API_GET_CITIES = "weathery/search_city/";
    public final static String WEATHER_ICONS = "data/weather_icons/";

    //Temperature units call
    public final static String METRIC_UNIT = "&units=metric";
    public final static String IMPERIAL_UNIT = "&units=imperial";


    //RequestTypes
    public final static String CURRENT_LOCATION_HTTP_REQUEST = "currentLocationWeatherRequest";
    public final static String WEATHER_BY_ID_HTTP_REQUEST = "weatherByCityId";
    public final static String WEATHER_FORECAST_BY_ID_HTTP_REQUEST = "weatherForecastByCityId";
    public final static String CITIES_DATA_FOR_SEARCH_LIST = "citiesData";

    //WeatherIcons
    public static final String WEATHER_ICONS_DAY = "day/";
    public static final String WEATHER_ICONS_NIGHT = "night/";

    //Time
    public static final String DAY = "day";
    public static final String NIGHT = "night";

    //onClick bundle constants
    public static final String BUNDLE_NAME = "theBundle";
    public static final String BUNDLE_CITY_ID = "CITY_ID";
    public static final String BUNDLE_TIME = "TIME";
    public static final String BUNDLE_DAY_NIGHT = "DAY_NIGHT";
    public static final String BUNDLE_TEMPERATURE = "TEMPERATURE";
    public static final String BUNDLE_DESCRIPTION = "DESCRIPTION";
    public static final String BUNDLE_IMAGE_URL = "IMAGE_URL";
    public static final String BUNDLE_CITY_NAME = "CITY_NAME";
    public static final String BUNDLE_INTERNET_AVAILABILITY = "internetAvailable";
}