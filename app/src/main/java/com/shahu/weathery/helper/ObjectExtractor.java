package com.shahu.weathery.helper;

import com.shahu.weathery.model.CardModel;

/**
 * Created by Shahu Ronghe on 10, March, 2020
 * in Weathery
 */
class ObjectExtractor {
    final static String EMPTY = "";

    public static String extractMain(CardModel cardModel) {
        if (cardModel == null) {
            return EMPTY;
        }
        if (cardModel.getWeatherItem() == null) {
            return EMPTY;
        }
        if (cardModel.getWeatherItem().getMain() == null) {
            return EMPTY;
        }
        return cardModel.getWeatherItem().getMain();
    }

    public static String extractDescription(CardModel cardModel) {
        if (cardModel == null) {
            return EMPTY;
        }
        if (cardModel.getWeatherItem() == null) {
            return EMPTY;
        }
        if (cardModel.getWeatherItem().getMain() == null) {
            return EMPTY;
        }
        return cardModel.getWeatherItem().getDescription();
    }

    public static String extractTime(CardModel cardModel) {
        if (cardModel == null) {
            return EMPTY;
        }
        if (cardModel.getDayNight() == null) {
            return EMPTY;
        }
        return cardModel.getDayNight();
    }
}
