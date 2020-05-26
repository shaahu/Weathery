package com.shahu.weathery.model.forecast

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Coord {
    @SerializedName("lon")
    var lon = 0.0

    @SerializedName("lat")
    var lat = 0.0

    override fun toString(): String {
        return "Coord{" +
                "lon = '" + lon + '\'' +
                ",lat = '" + lat + '\'' +
                "}"
    }
}