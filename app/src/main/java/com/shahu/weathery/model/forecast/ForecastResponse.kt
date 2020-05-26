package com.shahu.weathery.model.forecast

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class ForecastResponse {
    @SerializedName("city")
    var city: City? = null

    @SerializedName("cnt")
    var cnt = 0

    @SerializedName("cod")
    var cod: String? = null

    @SerializedName("message")
    var message = 0

    @SerializedName("list")
    var list: List<ForecastListItem>? = null

    override fun toString(): String {
        return "ForecastResponse{" +
                "city = '" + city + '\'' +
                ",cnt = '" + cnt + '\'' +
                ",cod = '" + cod + '\'' +
                ",message = '" + message + '\'' +
                ",list = '" + list + '\'' +
                "}"
    }
}