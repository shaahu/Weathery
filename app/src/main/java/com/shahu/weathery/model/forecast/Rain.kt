package com.shahu.weathery.model.forecast

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Rain {
    @SerializedName("3h")
    var jsonMember3h = 0.0

    override fun toString(): String {
        return "Rain{" +
                "3h = '" + jsonMember3h + '\'' +
                "}"
    }
}