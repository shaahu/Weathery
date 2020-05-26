package com.shahu.weathery.model.common

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Main {
    @SerializedName("temp")
    var temp = 0.0

    @SerializedName("temp_min")
    var tempMin = 0.0

    @SerializedName("humidity")
    var humidity = 0

    @SerializedName("pressure")
    var pressure = 0

    @SerializedName("temp_max")
    var tempMax = 0.0

    override fun toString(): String {
        return "Main{" +
                "temp = '" + temp + '\'' +
                ",temp_min = '" + tempMin + '\'' +
                ",humidity = '" + humidity + '\'' +
                ",pressure = '" + pressure + '\'' +
                ",temp_max = '" + tempMax + '\'' +
                "}"
    }
}