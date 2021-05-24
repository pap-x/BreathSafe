package com.example.breaths;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;

import android.util.Log;

public class  Settings extends AppCompatActivity  implements LocationListener {




    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;

    String location_GPS  ;
    String namme ;

    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;



    @Override
    public void onLocationChanged(Location location) {

        String loc_GPS =("Gps: " + location.getLatitude() + "," + location.getLongitude());
        ///Toast.makeText(Settings.this, location_GPS, Toast.LENGTH_SHORT).show();
        location_GPS = loc_GPS;
     }

    @Override
    public void onStatusChanged(String s, int status, Bundle extras) {
        Log.d("Latitude", "status");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Show dropdown for conditions
        Spinner spinner = (Spinner) findViewById(R.id.condition_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conditions_array, R.layout.conditions_spinner);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        EditText editName  = (EditText) findViewById(R.id.name_edittext);

        final Button save_button = findViewById(R.id.save_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(Settings.this, null, null, 1);

                // Code here executes on main thread after user presses save button
                String name = editName.getText().toString();
                Long conditionId = spinner.getSelectedItemId();
                  namme =name;



                Toast.makeText(Settings.this, name + " " + location_GPS, Toast.LENGTH_SHORT).show();

            }
        });

        final ImageButton home_button = findViewById(R.id.homeButton);
        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Settings.this.startActivity(myIntent);
            }
        });
    }

    public void getGP  (View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Data data = new Data(namme,location_GPS);

        dbHandler.addUserInfo(data);



    }





    public void getGPSlocation (View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Settings.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }








}