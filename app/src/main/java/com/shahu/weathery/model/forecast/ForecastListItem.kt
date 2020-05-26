package com.shahu.weathery.model.forecast

import com.google.gson.annotations.SerializedName
import com.shahu.weathery.model.common.WeatherItem
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class ForecastListItem {
    @SerializedName("dt")
    var dt: Long = 0

    @SerializedName("dt_txt")
    var dtTxt: String? = null

    @SerializedName("weather")
    var weather: List<WeatherItem>? = null

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("sys")
    var sys: Sys? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("rain")
    var rain: Rain? = null

    override fun toString(): String {
        return "ForecastListItem{" +
                "dt = '" + dt + '\'' +
                ",dt_txt = '" + dtTxt + '\'' +
                ",weather = '" + weather + '\'' +
                ",main = '" + main + '\'' +
                ",clouds = '" + clouds + '\'' +
                ",sys = '" + sys + '\'' +
                ",wind = '" + wind + '\'' +
                ",rain = '" + rain + '\'' +
                "}"
    }
}