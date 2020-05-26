package com.shahu.weathery.model.common

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class WeatherItem {
    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("main")
    var main: String? = null

    @SerializedName("id")
    var id = 0

    override fun toString(): String {
        return "WeatherItem{" +
                "icon = '" + icon + '\'' +
                ",description = '" + description + '\'' +
                ",main = '" + main + '\'' +
                ",id = '" + id + '\'' +
                "}"
    }
}