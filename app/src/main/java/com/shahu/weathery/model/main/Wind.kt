package com.shahu.weathery.model.main

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Wind {
    @SerializedName("deg")
    var deg = 0

    @SerializedName("speed")
    var speed = 0.0

    override fun toString(): String {
        return "Wind{" +
                "deg = '" + deg + '\'' +
                ",speed = '" + speed + '\'' +
                "}"
    }
}