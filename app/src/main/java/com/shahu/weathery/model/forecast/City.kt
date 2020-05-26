package com.shahu.weathery.model.forecast

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class City {
    @SerializedName("country")
    var country: String? = null

    @SerializedName("coord")
    var coord: Coord? = null

    @SerializedName("sunrise")
    var sunrise = 0

    @SerializedName("timezone")
    var timezone = 0

    @SerializedName("sunset")
    var sunset = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id = 0

    override fun toString(): String {
        return "City{" +
                "country = '" + country + '\'' +
                ",coord = '" + coord + '\'' +
                ",sunrise = '" + sunrise + '\'' +
                ",timezone = '" + timezone + '\'' +
                ",sunset = '" + sunset + '\'' +
                ",name = '" + name + '\'' +
                ",id = '" + id + '\'' +
                "}"
    }
}