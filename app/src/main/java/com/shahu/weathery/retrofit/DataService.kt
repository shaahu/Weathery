package com.shahu.weathery.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Shahu Ronghe on 18, April, 2020
 * in OpulenzaExports
 */
interface DataService {
    @GET("/data/2.5/weather")
    fun getWeatherByLocation(@Query("lat") latitude: String?,
                             @Query("lon") longitude: String?,
                             @Query("APPID") appid: String?): Call<JsonObject>

    @GET("/data/2.5/weather")
    fun getWeatherByCityId(@Query("id") cityId: String?,
                           @Query("APPID") appid: String?): Call<JsonObject>

    @GET("/data/2.5/forecast")
    fun getForecast(@Query("id") cityId: String?,
                    @Query("APPID") appid: String?): Call<JsonObject>


    @GET("/weathery/search_city/{name}")
    fun searchCity(@Path("name") name: String?): Call<String>

}