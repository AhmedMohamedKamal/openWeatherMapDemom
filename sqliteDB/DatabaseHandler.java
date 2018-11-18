package com.demo.ahmed.weather.sqliteDB;

/**
 * Created by AhmedKamal on 17/11/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.demo.ahmed.weather.constants.ConstantsHolder;
import com.demo.ahmed.weather.models.WeatherItem;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "WeatherMap";
    //  table name
    private static final String TABLE_WEATHER = "weather";

    // Weather Table Columns names
    private static final String KEY_TEMP_MIN = "temp_min";
    private static final String KEY_TEMP_MAX = "temp_max";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_WEATHER = "CREATE TABLE " + TABLE_WEATHER + "("
                + KEY_NAME + " DATETIME," + KEY_TEMP_MIN + " TEXT,"
                + KEY_TEMP_MAX + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_WEATHER);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        // Create tables again
        onCreate(db);
    }

    // Adding new Weather Item
    public void addWeatherRow(WeatherItem weatherItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, weatherItem.getname());
        values.put(KEY_TEMP_MIN, weatherItem.getTemp_min());
        values.put(KEY_TEMP_MAX, weatherItem.getTemp_max());

        // Inserting Row
        db.insert(TABLE_WEATHER, null, values);
        db.close(); // Closing database connection
        Log.d(ConstantsHolder.LogTag + "- DatabaseHandler", "now writing a weather item: " + values.toString());

    }

    public List<WeatherItem> getAllWeather() {
        Log.d(ConstantsHolder.LogTag + "- DatabaseHandler", "now reading the weather items");
        List<WeatherItem> weatherItems = new ArrayList<WeatherItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Log.d(ConstantsHolder.LogTag + "- DatabaseHandler", "now reading the weather item: " + cursor.getString(0) +
                        "-" + cursor.getString(1) + "-" + cursor.getString(2));
                WeatherItem weatherItem = new WeatherItem(cursor.getString(2),
                        cursor.getString(1), cursor.getString(0));
                // Adding weatherItem to list
                weatherItems.add(weatherItem);
            } while (cursor.moveToNext());
        }

        // return contact list
        return weatherItems;
    }


    public int getWeatherCount() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_WEATHER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_WEATHER);
        db.close();
    }

}
