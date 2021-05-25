package com.example.breaths;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Forecast extends AppCompatActivity {

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
        setContentView(R.layout.activity_forecast);

        final ImageButton home_button = findViewById(R.id.homeButton);
        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Forecast.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Forecast.this.startActivity(myIntent);
            }
        });

        getData("20.0000", "40.0000");

    }

    public void expandableButton1(View view) {
        ExpandableLinearLayout expandableLayout1 = (ExpandableLinearLayout) findViewById(R.id.content1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        ExpandableLinearLayout expandableLayout2 = (ExpandableLinearLayout) findViewById(R.id.content2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        ExpandableLinearLayout expandableLayout3 = (ExpandableLinearLayout) findViewById(R.id.content3);
        expandableLayout3.toggle(); // toggle expand and collapse
    }

    public void expandableButton4(View view) {
        ExpandableLinearLayout expandableLayout4 = (ExpandableLinearLayout) findViewById(R.id.content4);
        expandableLayout4.toggle(); // toggle expand and collapse
    }

    //API call to get data
    public void getData(String lat, String lon) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String forecast_url = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat="+lat+"&lon="+lon+"&appid=2bcdd94a20ae1c5acd2f35b063bb3a0f";

        Pollutants[] forecast = new Pollutants[5];
        //***********************GET THE FORECAST VALUES**************************
        // Request a JSON response for the current values from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, forecast_url, null,
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
                            visualize(forecast);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Forecast.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                Log.i("error", String.valueOf(error));
            }
        }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void visualize(Pollutants[] forecast) {
        if (forecast[0].day!= null) {
            for (int i = 1; i<5; i++) {
                //Put the weekday
                String titleID = "accordion_title" + i;
                int resID = getResources().getIdentifier(titleID, "id", getPackageName());
                TextView textViewToChange = (TextView) findViewById(resID);
                textViewToChange.setText(forecast[i].day);

                //Change the color
                String headerID = "accordion_header" + i;
                resID = getResources().getIdentifier(headerID, "id", getPackageName());
                final RelativeLayout layoutToChange = (RelativeLayout) findViewById(resID);
                System.out.println(forecast[i].aqi);
                switch (forecast[i].aqi) {
                    case "1" :
                        layoutToChange.setBackgroundColor(Color.parseColor("#BFE355"));
                        break;
                    case "2" :
                    case "3" :
                        layoutToChange.setBackgroundColor(Color.parseColor("#FFD580"));
                        break;
                    case "4" :
                    case "5" :
                        layoutToChange.setBackgroundColor(Color.parseColor("#F38181"));
                        break;
                }
                //Put the PM2.5
                String pm2_5ID = "pm2_5_" + i;
                resID = getResources().getIdentifier(pm2_5ID, "id", getPackageName());
                textViewToChange = (TextView) findViewById(resID);
                textViewToChange.setText("PM2.5: "+forecast[i].pm2_5);
            }
        }
    }
}