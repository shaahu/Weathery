package com.shahu.weathery.model.forecast

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Main {
    @SerializedName("temp")
    var temp: String? = null

    @SerializedName("temp_min")
    var tempMin = 0.0

    @SerializedName("grnd_level")
    var grndLevel = 0.0

    @SerializedName("temp_kf")
    var tempKf = 0.0

    @SerializedName("humidity")
    var humidity = 0

    @SerializedName("pressure")
    var pressure = 0

    @SerializedName("sea_level")
    var seaLevel = 0.0

    @SerializedName("temp_max")
    var tempMax = 0.0

    @SerializedName("feels_like")
    var feelsLike = 0.0

    override fun toString(): String {
        return "Main{" +
                "temp = '" + temp + '\'' +
                ",temp_min = '" + tempMin + '\'' +
                ",grnd_level = '" + grndLevel + '\'' +
                ",temp_kf = '" + tempKf + '\'' +
                ",humidity = '" + humidity + '\'' +
                ",pressure = '" + pressure + '\'' +
                ",sea_level = '" + seaLevel + '\'' +
                ",temp_max = '" + tempMax + '\'' +
                "}"
    }
}