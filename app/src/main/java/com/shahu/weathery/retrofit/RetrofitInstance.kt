package com.shahu.weathery.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by Shahu Ronghe on 18, April, 2020
 * in OpulenzaExports
 */
object RetrofitInstance {
    private const val BASE_URL = "http://api.openweathermap.org/"
    private const val SHAHOO_API_BASE_URL = "https://api.shahoo.in/"

    val retrofitInstance: Retrofit
        get() {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

    val retrofitInstance2: Retrofit
        get() {
            return Retrofit.Builder()
                    .baseUrl(SHAHOO_API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
        }
}