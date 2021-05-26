package com.example.breaths;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDBHandler extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "breathSDB.db";

    private static final int DATABASE_VERSION = 1;




    //Table User
    public static final String TABLE_USER= "TABLE_USER";
    public static final String COLUMN_USER_ID = "COLUMN_USER_ID";
    public static final String COLUMN_USER_NAME = "COLUMN_USER_NAME";
    public static final String COLUMN_LOCATION = "COLUMN_LOCATION";
    //Table condition
    public static final String TABLE_CONDITIONS= "TABLE_CONDITIONS";
    public static final String COLUMN_CONDITION_ID = "COLUMN_CONDITION_ID";
    public static final String COLUMN_CONDITION = "COLUMN_CONDITION";
    //Table Pollutants
    public static final String TABLE_POLLUTANTS = "TABLE_POLLUTANTS";
    public static final String COLUMN_POLLUTANTS_ID = "COLUMN_POLLUTANTS_ID";
    public static final String COLUMN_POLLUTANT  = "COLUMN_POLLUTANT";
    public static final String COLUMN_POLLUTANTS_LOW= "COLUMN_POLLUTANTS_LOW";
    public static final String COLUMN_POLLUTANTS_MEDIUM= "COLUMN_POLLUTANTS_MEDIUM";
    public static final String COLUMN_POLLUTANTS_HIGH= "COLUMN_POLLUTANTS_HIGH";
    public static final String COLUMN_POLLUTANTS_DANGER= "COLUMN_POLLUTANTS_DANGER";
    //Table Text
    public static final String TABLE_TEXT= "TABLE_TEXT";
    public static final String COLUMN_INDEX = "COLUMN_INDEX";
    public static final String COLUMN_TEXT = "COLUMN_TEXT";
    public static final String COLUMN_TEXT_ID = "COLUMN_TEXT_ID";


    public MyDBHandler(Context context, Object o, Object o1, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    //method to add data to the db
    public void addUserInfo(Data data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, data.getUserName());
        values.put(COLUMN_LOCATION, data.getLocationGPS());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONDITIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLLUTANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXT);

        onCreate(db);
    }


   




}
