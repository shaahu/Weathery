package com.shahu.weathery.retrofit

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Shahu Ronghe on 18, April, 2020
 * in OpulenzaExports
 */
class RetrofitInstance(context: Context) {
    private  val BASE_URL = "http://api.openweathermap.org/"
    private  val SHAHOO_API_BASE_URL = "https://api.shahoo.in/"

    var cacheSize = 10 * 1024 * 1024 // 10 MB

    var cache: Cache = Cache(context.cacheDir, cacheSize.toLong())

    var okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .build()

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
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
}