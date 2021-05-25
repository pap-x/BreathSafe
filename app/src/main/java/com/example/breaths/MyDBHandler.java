package com.example.breaths;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "breathSDB.db";


    //Table User
    public static final String TABLE_USER= "userTable";
    public static final String COLUMN_USER_ID = "idUser";
    public static final String COLUMN_USER_NAME = "userName";
    public static final String COLUMN_LOCATION = "location";
    //Table condition
    public static final String TABLE_CONDITIONS= "conditionsTable";
    public static final String COLUMN_CONDITION_ID = "idCondition";
    public static final String COLUMN_CONDITION = "condition";
    //Table Pollutants
    public static final String TABLE_POLLUTANTS = "pollutantsTable";
    public static final String COLUMN_POLLUTANTS_ID = "idPollutants";
    public static final String COLUMN_POLLUTANT  = "pollutant";
    public static final String COLUMN_POLLUTANTS_LOW= "low";
    public static final String COLUMN_POLLUTANTS_MEDIUM= "medium";
    public static final String COLUMN_POLLUTANTS_HIGH= "high";
    public static final String COLUMN_POLLUTANTS_DANGER= "danger";
    //Table Text
    public static final String TABLE_TEXT= "textTable";
    public static final String COLUMN_INDEX = "indexText";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_TEXT_ID = "idText";




    //Constructor
    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " +
                TABLE_USER + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY," +
                COLUMN_USER_NAME + " TEXT,"+
                COLUMN_LOCATION + " TEXT," +
                COLUMN_CONDITION_ID + " INTEGER" + ")";
         db.execSQL(CREATE_TABLE_USER);
        //Create Table condition


        String CREATE_TABLE_CONDITIONS = "CREATE TABLE IF NOT EXISTS " +
                TABLE_CONDITIONS + "(" +
                COLUMN_CONDITION_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CONDITION + " TEXT" + ")";
         db.execSQL(CREATE_TABLE_CONDITIONS);
        //Create
        String CREATE_TABLE_POLLUTANTS = "CREATE TABLE IF NOT EXISTS " +
                TABLE_POLLUTANTS + "(" +
                COLUMN_POLLUTANTS_ID + " INTEGER PRIMARY KEY," +
                COLUMN_POLLUTANT + " TEXT," +
                COLUMN_POLLUTANTS_LOW + " INTEGER," +
                COLUMN_POLLUTANTS_MEDIUM + " INTEGER," +
                COLUMN_POLLUTANTS_HIGH + " INTEGER," +
                COLUMN_POLLUTANTS_DANGER + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_POLLUTANTS);
        //Create
        String CREATE_TABLE_TEXT = "CREATE TABLE  IF NOT EXISTS " +
                TABLE_TEXT + "(" +
                COLUMN_CONDITION_ID + " TEXT," +
                COLUMN_POLLUTANTS_ID + " INTEGER,"+
                COLUMN_INDEX + " INTEGER," +
                COLUMN_TEXT + " TEXT," +
                COLUMN_TEXT_ID + " INTEGER PRIMARY KEY" +")";
          db.execSQL(CREATE_TABLE_TEXT);
        //Create




    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONDITIONS);
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLLUTANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXT);

        onCreate(db);
    }

    //Μέθοδος για προσθήκη ενός προϊόντος στη ΒΔ
    public void addUserInfo(Data data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, data.getUserName());
        values.put(COLUMN_LOCATION, data.getLocationGPS());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        db.close();
    }



   




}
