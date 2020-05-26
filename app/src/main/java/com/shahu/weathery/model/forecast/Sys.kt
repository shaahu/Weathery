package com.shahu.weathery.model.forecast

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Sys {
    @SerializedName("pod")
    var pod: String? = null

    override fun toString(): String {
        return "Sys{" +
                "pod = '" + pod + '\'' +
                "}"
    }
}