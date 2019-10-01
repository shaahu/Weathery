package com.shahu.weathery.Helper;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
public class CitySearch implements Searchable {
    private String mTitle;

    public CitySearch(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public CitySearch setTitle(String title) {
        mTitle = title;
        return this;
    }
}
