package com.shahu.weathery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shahu.weathery.common.Constants
import com.shahu.weathery.model.CardModel

class WeatherDetail : AppCompatActivity() {
    var lat: String? = null
    var lon: String? = null
    var cityData: CardModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        cityData = intent.getParcelableExtra(Constants.CITY_CARD_MODEL)
    }


    companion object {
        private const val TAG = "WeatherDetail"
    }
}