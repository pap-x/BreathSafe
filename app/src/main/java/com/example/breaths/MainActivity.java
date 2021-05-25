package com.example.breaths;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData("40.674972", "22.895322");


        final ImageButton button = findViewById(R.id.homeButton);
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
        String url = "https://api.openweathermap.org/data/2.5/air_pollution?lat="+lat+"&lon="+lon+"&appid="; //2bcdd94a20ae1c5acd2f35b063bb3a0f";
        String forecast_url = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat="+lat+"&lon="+lon+"&appid=";  //2bcdd94a20ae1c5acd2f35b063bb3a0f";

        //*****************GET THE CURRENT VALUES***********************
        // Request a JSON response for the current values from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject details = response.getJSONArray("list").getJSONObject(0);
                        String aqi = details.getJSONObject("main").getString("aqi");
                        JSONObject pollutants = details.getJSONObject("components");
                        // Display the response string.
                        Toast.makeText(MainActivity.this, aqi + " " + pollutants.toString(), Toast.LENGTH_SHORT).show();
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
        // Request a JSON response for the current values from the provided URL.
        JsonObjectRequest stringRequestF = new JsonObjectRequest(Request.Method.GET, forecast_url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject details1 = response.getJSONArray("list").getJSONObject(132);
                        String aqi1 = details1.getJSONObject("main").getString("aqi");
                        JSONObject details2 = response.getJSONArray("list").getJSONObject(156);
                        String aqi2 = details2.getJSONObject("main").getString("aqi");
                        JSONObject details3 = response.getJSONArray("list").getJSONObject(180);
                        String aqi3 = details3.getJSONObject("main").getString("aqi");
                        JSONObject details4 = response.getJSONArray("list").getJSONObject(204);
                        String aqi4 = details4.getJSONObject("main").getString("aqi");
                        JSONObject details5 = response.getJSONArray("list").getJSONObject(228);
                        String aqi5 = details5.getJSONObject("main").getString("aqi");
                        // Display the response string.
                        //Toast.makeText(MainActivity.this, aqi + " " + pollutants.toString(), Toast.LENGTH_SHORT).show();
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


    //On click name




}