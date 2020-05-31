package com.shahu.weathery.helper

import com.shahu.weathery.model.CardModel

/**
 * Created by Shahu Ronghe on 10, March, 2020
 * in Weathery
 */
internal object ObjectExtractor {
    private const val EMPTY = ""
    fun extractMain(cardModel: CardModel?): String {
        if (cardModel == null) {
            return EMPTY
        }
        if (cardModel.weatherItem == null) {
            return EMPTY
        }
        return (if (cardModel.weatherItem!!.main == null) {
            EMPTY
        } else cardModel.weatherItem!!.main) as String
    }

    fun extractDescription(cardModel: CardModel?): String {
        if (cardModel == null) {
            return EMPTY
        }
        if (cardModel.weatherItem == null) {
            return EMPTY
        }
        return (if (cardModel.weatherItem!!.main == null) {
            EMPTY
        } else cardModel.weatherItem!!.description) as String
    }

    fun extractTime(cardModel: CardModel?): String {
        if (cardModel == null) {
            return EMPTY
        }
        return (if (cardModel.dayNight == null) {
            EMPTY
        } else cardModel.dayNight) as String
    }
}