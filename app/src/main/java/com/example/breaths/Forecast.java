package com.example.breaths;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Forecast extends AppCompatActivity {

    class Pollutants {
        public String aqi, pm2_5, pm10, o3, co, so2, no2;
        Pollutants(String aqi, String pm2_5, String pm10, String o3, String co, String so2, String no2) {
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

        Pollutants[] forecast = getData("20.0000", "40.0000");
    }

    //API call to get data
    public Pollutants[] getData(String lat, String lon) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String forecast_url = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat="+lat+"&lon="+lon+"&appid=";  //2bcdd94a20ae1c5acd2f35b063bb3a0f";

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
                                forecast[i] = new Pollutants(details.getJSONObject("main").getString("aqi"),
                                        details.getJSONObject("components").getString("pm2_5"),
                                        details.getJSONObject("components").getString("pm10"),
                                        details.getJSONObject("components").getString("o3"),
                                        details.getJSONObject("components").getString("co"),
                                        details.getJSONObject("components").getString("so2"),
                                        details.getJSONObject("components").getString("no2"));
                            }
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

        return forecast;
    }
}