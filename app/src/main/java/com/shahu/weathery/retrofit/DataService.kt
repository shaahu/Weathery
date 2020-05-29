package com.shahu.weathery.retrofit

import com.google.gson.JsonObject
import com.shahu.weathery.model.citysearch.CitySearchResultResponse
import com.shahu.weathery.model.onecall.OneCallResponse
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

    @GET("/data/2.5/onecall")
    fun oneCallForCity(@Query("lat") latitude: String?,
                       @Query("lon") longitude: String?,
                       @Query("exclude") exclude: String?,
                       @Query("APPID") appid: String?): Call<OneCallResponse>


    @GET("/weathery/search_city/{name}")
    fun searchCity(@Path("name") name: String?): Call<CitySearchResultResponse>

}