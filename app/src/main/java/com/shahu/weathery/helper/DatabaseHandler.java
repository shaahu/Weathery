package com.shahu.weathery.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shahu.weathery.model.CitySearchItem;

import java.util.ArrayList;

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
public class DatabaseHandler {
    public static final String COLUMN_ID = "id";
    private static final String TABLE_NAME = "Cities";
    private static final String COLUMN_CITY_NAME = "name";
    private static final String COLUMN_COUNTRY = "country";
    private static final String TAG = "DatabaseHandler";

    private ArrayList<CitySearchItem> allCities;
    private AssetDatabaseOpenHelper mAssetDatabaseOpenHelper;

    public DatabaseHandler(Context context) {
        mAssetDatabaseOpenHelper = new AssetDatabaseOpenHelper(context);
        allCities = new ArrayList<>();
        loadAllCities();
    }

    public ArrayList<CitySearchItem> getAllCities() {
        return allCities;
    }

    private void loadAllCities() {
        SQLiteDatabase database = mAssetDatabaseOpenHelper.openDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_CITY_NAME, COLUMN_COUNTRY}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allCities.add(
                        new CitySearchItem(
                                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY))));
                cursor.moveToNext();
            }
            cursor.close();
        }
    }
}
