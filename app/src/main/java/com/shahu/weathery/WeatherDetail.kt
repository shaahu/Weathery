package com.shahu.weathery

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shahu.weathery.common.Constants
import com.shahu.weathery.helper.ValuesConverter.convertTemperatureToCelsius
import com.shahu.weathery.model.CardModel
import com.shahu.weathery.model.onecall.OneCallResponse
import com.shahu.weathery.retrofit.DataService
import com.shahu.weathery.retrofit.RetrofitInstance
import kotlinx.android.synthetic.main.activity_weather_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherDetail : AppCompatActivity() {
    var mLat: String? = null
    var mLon: String? = null
    private lateinit var mCityData: CardModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        mCityData = intent.getParcelableExtra(Constants.CITY_CARD_MODEL)
        mLat = mCityData.lat
        mLon = mCityData.lon
        val service: DataService = RetrofitInstance(applicationContext).retrofitInstance.create(DataService::class.java)
        val call = service.oneCallForCity(mLat, mLon, "minutely", Constants.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<OneCallResponse> {
            override fun onFailure(call: Call<OneCallResponse>, t: Throwable) {
                Log.e(TAG, "on response failure", t)
            }

            override fun onResponse(call: Call<OneCallResponse>, response: Response<OneCallResponse>) {
                Log.d(TAG, response.body().toString())
                if (response.code() == 200) {
                    populateAllData(response.body())
                }
            }
        })
    }

    private fun populateAllData(oneCallResponse: OneCallResponse?) {
        main_city_name.text = mCityData.name
        description.text = mCityData.description
        temperature.text = mCityData.temperature?.let { convertTemperatureToCelsius(it) } + "°C"
        current_max.text = mCityData.max?.let { convertTemperatureToCelsius(it) }
        current_min.text = mCityData.min?.let { convertTemperatureToCelsius(it) }
    }


    companion object {
        private const val TAG = "WeatherDetail"
    }
}