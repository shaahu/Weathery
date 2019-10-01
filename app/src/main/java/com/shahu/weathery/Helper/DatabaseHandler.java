package com.shahu.weathery.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
public class DatabaseHandler {
    public static final String COLUMN_ID = "id";
    private static final String TABLE_NAME = "Cities";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COUNTRY = "country";
    private static final String TAG = "DatabaseHandler";

    private String[] allCities;
    private AssetDatabaseOpenHelper mAssetDatabaseOpenHelper;

    public DatabaseHandler(Context context) {
        mAssetDatabaseOpenHelper = new AssetDatabaseOpenHelper(context);
        loadAllCities();
    }

    public ArrayList<String> getAllCities() {
        return new ArrayList<>(Arrays.asList(allCities));
    }

    private String[] loadAllCities() {
        SQLiteDatabase database = mAssetDatabaseOpenHelper.openDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_NAME, COLUMN_COUNTRY}, null, null, null, null, null);
        allCities = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allCities[cursor.getPosition()] =
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)) + " - " + cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return allCities;
    }

    public String retrieveCityIdByName(String in) {
        String id = null;
        String[] split = in.split(" - ");
        String city = split[0];
        String country = split[1];
        SQLiteDatabase database = mAssetDatabaseOpenHelper.openDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_NAME + "=? AND " + COLUMN_COUNTRY + "=?",
                new String[]{city, country}, null,
                null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return id;
    }
}
