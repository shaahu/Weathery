package com.shahu.weathery.model.main

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Sys {
    @SerializedName("country")
    var country: String? = null

    @SerializedName("sunrise")
    var sunrise = 0

    @SerializedName("sunset")
    var sunset = 0

    @SerializedName("id")
    var id = 0

    @SerializedName("type")
    var type = 0

    @SerializedName("message")
    var message = 0.0

    override fun toString(): String {
        return "Sys{" +
                "country = '" + country + '\'' +
                ",sunrise = '" + sunrise + '\'' +
                ",sunset = '" + sunset + '\'' +
                ",id = '" + id + '\'' +
                ",type = '" + type + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }
}