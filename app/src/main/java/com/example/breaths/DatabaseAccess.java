package com.example.breaths;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
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
    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new MyDBHandler(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getQuotes() {
        List<String> list = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT COLUMN_USER_NAME FROM " + TABLE_USER +  "" , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }




    public String getHello() {
        String textHello = new String();

        Cursor cursor = database.rawQuery("SELECT COLUMN_USER_NAME FROM " + TABLE_USER +  " ORDER BY COLUMN_USER_ID DESC LIMIT 1" , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            textHello = (cursor.getString(0));
            cursor.moveToNext();
        }



        cursor.close();

        return textHello;
    }
}