package com.example.breaths;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //private TextView textHello;

    DatabaseAccess databaseAccess;
    User user = null;


    class Pollutants {
        public String day, aqi, pm2_5, pm10, o3, co, so2, no2;
        Pollutants(String day, String aqi, String pm2_5, String pm10, String o3, String co, String so2, String no2) {
            this.day = day;
            this.aqi = aqi;
            this.pm2_5 = pm2_5;
            this.pm10 = pm10;
            this.o3 = o3;
            this.co = co;
            this.so2 = so2;
            this.no2 = no2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the name from database or go to setting activity

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        //get the user from the database
        user = databaseAccess.getUser();
        databaseAccess.close();

        if (user != null) {
            ((TextView) findViewById(R.id.text_hello)).setText("Hello " + user.userName + "!");

            //spilt the location string to lat and long
            //String[] result = user.locationGPS.split("[,]", 0);

            getData("20", "40");

        }
        else {
            Intent myIntent = new Intent(MainActivity.this, Settings.class);
            MainActivity.this.startActivity(myIntent);
        }

        final ImageButton button = findViewById(R.id.settingsButton);

        //When clicking on Settings button go to Settings
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                Intent myIntent = new Intent(MainActivity.this, Settings.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

    }



    //API call to get data
    public void getData(String lat, String lon) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openweathermap.org/data/2.5/air_pollution?lat="+lat+"&lon="+lon+"&appid=2bcdd94a20ae1c5acd2f35b063bb3a0f";
        String forecast_url = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat="+lat+"&lon="+lon+"&appid=2bcdd94a20ae1c5acd2f35b063bb3a0f";

        //*****************GET THE CURRENT VALUES***********************
        // Request a JSON response for the current values from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject details = response.getJSONArray("list").getJSONObject(0);

                        //Get day of the week
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
                        Date date = new java.util.Date(Long.valueOf(details.getInt("dt"))*1000);
                        String day = dateFormat.format(date );
                        System.out.println(day);

                        Pollutants data = new Pollutants(day,
                                details.getJSONObject("main").getString("aqi"),
                                details.getJSONObject("components").getString("pm2_5"),
                                details.getJSONObject("components").getString("pm10"),
                                details.getJSONObject("components").getString("o3"),
                                details.getJSONObject("components").getString("co"),
                                details.getJSONObject("components").getString("so2"),
                                details.getJSONObject("components").getString("no2"));

                        //sending the data to be visualized
                        visualizeUp(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                    Log.i("error", String.valueOf(error));
                }
            }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        //***********************GET THE FORECAST VALUES**************************
        Pollutants[] forecast = new Pollutants[5];
        // Request a JSON response for the current values from the provided URL.
        JsonObjectRequest stringRequestF = new JsonObjectRequest(Request.Method.GET, forecast_url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        for (int i = 0; i<5; i++) {
                            JSONObject details = response.getJSONArray("list").getJSONObject(132+(i*24));   //calculate date numbers by adding 24 hours per day

                            //Get day of the week
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
                            Date date = new java.util.Date(Long.valueOf(details.getInt("dt"))*1000);
                            String day = dateFormat.format(date );
                            System.out.println(day);

                            forecast[i] = new Pollutants(day,
                                    details.getJSONObject("main").getString("aqi"),
                                    details.getJSONObject("components").getString("pm2_5"),
                                    details.getJSONObject("components").getString("pm10"),
                                    details.getJSONObject("components").getString("o3"),
                                    details.getJSONObject("components").getString("co"),
                                    details.getJSONObject("components").getString("so2"),
                                    details.getJSONObject("components").getString("no2"));
                        }
                        //Call visualize function to view the results
                        visualizeForecast(forecast);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                    Log.i("error", String.valueOf(error));
                }
            }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequestF);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent myIntent = new Intent(MainActivity.this, Settings.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    void visualizeUp(Pollutants data) {

        //Change the air today string along with the background color
        String air_today_text = "";
        String color = "#BFE355";
        switch (data.aqi) {
            case "1":
                air_today_text = "The air now is CLEAN (AQI: "+data.aqi+")";
                color = "#BFE355";
                break;
            case "2":
            case "3":
                air_today_text = "The air now is MODERATE (AQI: "+data.aqi+")";
                color = "#FFD580";
                break;
            case "4":
            case "5":
                air_today_text = "The air now is DANGEROUS (AQI: "+data.aqi+")";
                color = "#F38181";
                break;
        }
        TextView air_today = (TextView)findViewById(R.id.air_today);
        air_today.setText(air_today_text);
        air_today.setBackgroundColor(Color.parseColor(color));

        //Insert the increased pollutants value string
        Float[] pollutants = new Float[6];
        pollutants[0] = Float.parseFloat(data.pm2_5);
        pollutants[1] = Float.parseFloat(data.pm10);
        pollutants[2] = Float.parseFloat(data.o3);
        pollutants[3] = Float.parseFloat(data.co);
        pollutants[4] = Float.parseFloat(data.so2);
        pollutants[5] = Float.parseFloat(data.no2);

        String[] pollutant_names = new String[6];
        pollutant_names[0] = "PM2.5";
        pollutant_names[1] = "PM10";
        pollutant_names[2] = "Ozone";
        pollutant_names[3] = "Carbon Monoxide";
        pollutant_names[4] = "Sulfur Dioxide";
        pollutant_names[5] = "Nitrogen Dioxide";

        Integer[] pollutant_indexes = new Integer[6];

        //Compare pollutant values from API with baselines from the DB and update the text

        //get the pollutants table
        databaseAccess.open();
        ArrayList<Pollutant> pollutant_data  = databaseAccess.getAllPollutantObjects();
        String values_string = "";
        for (int i=0; i<pollutants.length; i++) {
            if (pollutants[i]>pollutant_data.get(i).columnPollutantDanger) {
                //add increased values text
                values_string = values_string + "Dangerous amounts of "+pollutant_names[i]+" ("+pollutants[i]+"μg/m3) \n";
                pollutant_indexes[i] = 5;
            }
            else if (pollutants[i]>pollutant_data.get(i).columnPollutantHigh) {
                //add increased values text
                values_string = values_string + "High amounts of "+pollutant_names[i]+" ("+pollutants[i]+"μg/m3) \n";
                pollutant_indexes[i] = 4;
            }
            else if (pollutants[i]>pollutant_data.get(i).columnPollutantMedium) {
                //add increased values text
                values_string = values_string + "Moderate amounts of "+pollutant_names[i]+" ("+pollutants[i]+"μg/m3) \n";
                pollutant_indexes[i] = 3;
            }
            else if (pollutants[i]>pollutant_data.get(i).columnPollutantLow) {
                //add increased values text
                values_string = values_string + "Increased amounts of "+pollutant_names[i]+" ("+pollutants[i]+"μg/m3) \n";
                pollutant_indexes[i] = 2;
            }
            else {
                pollutant_indexes[i] = 1;
            }
        }

        //Insert increased values text
        ((TextView) findViewById(R.id.increased_values)).setText(values_string);

        String advice_text = "";
        //If user is set, get the condition - else set condition as 0
        String user_condition = user!=null ? Integer.toString(user.condId):"0";

        if (Integer.parseInt(data.aqi)>2) {
            databaseAccess.open();
            /*
            For each pollutant, if it has a pollution index >2, get the proper text from the database.
            If the condition of the user is affected by the pollutant AND the index value is the max index
            of all the pollutants, display the text.
             */
            int maxIndex = 0;
            ArrayList<Text> text_data;
            for (int i = 0; i < pollutant_indexes.length; i++) {
                if (pollutant_indexes[i] > 2) {
                    text_data = databaseAccess.getTextByPollutantAndIndex(i+1, pollutant_indexes[i]);
                    String[] conditions = text_data.get(0).columnConditionIdList.split(",");
                    if (Arrays.stream(conditions).anyMatch(user_condition::equals) && pollutant_indexes[i] > maxIndex) {
                        advice_text = text_data.get(0).columnText;
                    }
                }
            }
        }
        else if (data.aqi=="2" && user_condition!="0") {
            advice_text = "If you are unusually sensitive consider reducing heavy outdoor exertion";
        }

        ((TextView) findViewById(R.id.text_main)).setText(advice_text);


    }

    void visualizeForecast(Pollutants[] forecast) {
        if (forecast[0].day!= null) {
            for (int i = 1; i<5; i++) {
                //Put the weekday
                String textID = "forecast_day" + i;
                int resID = getResources().getIdentifier(textID, "id", getPackageName());
                TextView textViewToChange = (TextView) findViewById(resID);
                textViewToChange.setText(forecast[i].day);

                //Change the air quality
                textID = "forecast_value" + i;
                resID = getResources().getIdentifier(textID, "id", getPackageName());
                textViewToChange = (TextView) findViewById(resID);
                switch (forecast[i].aqi) {
                    case "1" :
                        textViewToChange.setText("CLEAN");
                        textViewToChange.setBackgroundColor(Color.parseColor("#BFE355"));
                        break;
                    case "2" :
                    case "3" :
                        textViewToChange.setText("MODERATE");
                        textViewToChange.setBackgroundColor(Color.parseColor("#FFD580"));
                        break;
                    case "4" :
                    case "5" :
                        textViewToChange.setText("DANGEROUS");
                        textViewToChange.setBackgroundColor(Color.parseColor("#F38181"));
                        break;
                }
            }
        }
    }

    //On click name

    //On click on Forecast Layout got to forecast page
    public void goToForecast(View view) {
        Intent myIntent = new Intent(MainActivity.this, Forecast.class);
        MainActivity.this.startActivity(myIntent);
    }

}