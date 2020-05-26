package com.shahu.weathery.model.common

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Clouds {
    @SerializedName("all")
    var all = 0

    override fun toString(): String {
        return "Clouds{" +
                "all = '" + all + '\'' +
                "}"
    }
}