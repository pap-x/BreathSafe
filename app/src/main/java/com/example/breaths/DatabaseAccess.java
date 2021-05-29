package com.example.breaths;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public User getUser() {
        User user = null;

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_USER +  " ORDER BY COLUMN_USER_ID DESC LIMIT 1" , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = new User (cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)));
            cursor.moveToNext();
        }

        cursor.close();

        return user;
    }

    //retrieve table Pollutant data
    public ArrayList<Pollutant> getAllPollutantObjects()
    {

        String selectQuery = "SELECT * FROM TABLE_POLLUTANTS";
        //get the cursor you're going to use
        Cursor cursor = database.rawQuery(selectQuery, null);

        //this is optional - if you want to return one object
        //you don't need a list
        ArrayList<Pollutant> objectList = new ArrayList<Pollutant>();

        //you should always use the try catch statement incase
        //something goes wrong when trying to read the data
        try
        {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //the .getString(int x) method of the cursor returns the column
                    //of the table your query returned
                    Pollutant object;
                    object = new Pollutant(Integer.parseInt(cursor.getString(0)),
                            (cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            Integer.parseInt(cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)),
                            Integer.parseInt(cursor.getString(5)));
                    objectList.add(object);
                } while (cursor.moveToNext());
            }
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        finally
        {
            //release all your resources
            cursor.close();
            database.close();
        }
        return objectList;
    }


    //retrieve table text data
    public ArrayList<Text> getAllTextObjects()
    {
        String selectQuery = "SELECT * FROM TABLE_TEXT";
        //get the cursor you're going to use
        Cursor cursor = database.rawQuery(selectQuery, null);

        //this is optional - if you want to return one object
        //you don't need a list
        ArrayList<Text> objectList = new ArrayList<Text>();

        //you should always use the try catch statement incase
        //something goes wrong when trying to read the data
        try
        {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //the .getString(int x) method of the cursor returns the column
                    //of the table your query returned
                    Text object;
                    object = new Text(cursor.getString(0),
                            Integer.parseInt(cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            (cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)));
                    objectList.add(object);
                } while (cursor.moveToNext());
            }
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        finally
        {
            //release all your resources
            cursor.close();
            database.close();
        }
        return objectList;
    }

    public ArrayList<Text> getTextByPollutantAndIndex(int pollutant, int index)
    {
        String selectQuery = "SELECT * FROM TABLE_TEXT WHERE COLUMN_POLLUTANTS_ID="+pollutant+" AND COLUMN_INDEX="+index;
        //get the cursor you're going to use
        Cursor cursor = database.rawQuery(selectQuery, null);

        //this is optional - if you want to return one object
        //you don't need a list
        ArrayList<Text> objectList = new ArrayList<Text>();

        //you should always use the try catch statement incase
        //something goes wrong when trying to read the data
        try
        {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //the .getString(int x) method of the cursor returns the column
                    //of the table your query returned
                    Text object;
                    object = new Text(cursor.getString(0),
                            Integer.parseInt(cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            (cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)));
                    objectList.add(object);
                } while (cursor.moveToNext());
            }
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        finally
        {
            //release all your resources
            cursor.close();
            //database.close();
        }
        return objectList;
    }

}