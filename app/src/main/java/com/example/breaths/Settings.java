package com.example.breaths;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class  Settings extends AppCompatActivity  implements LocationListener {




    protected LocationManager locationManager;

    String location_GPS = null ;




    @Override
    public void onLocationChanged(Location location) {

        String loc_GPS = (location.getLatitude()+","+location.getLongitude());
         location_GPS = loc_GPS;
     }

    @Override
    public void onStatusChanged(String s, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
                MyDBHandler dbHandler = new MyDBHandler(Settings.this);

                // Code here executes on main thread after user presses save button
                String name = editName.getText().toString();
                 int conditionId =(int) spinner.getSelectedItemId();



                if(!(name.equals("")) &&  !(location_GPS == null)) {

                    DataUser data = new DataUser(name, location_GPS,conditionId );
                    dbHandler.addUserInfo(data);

                    Toast.makeText(Settings.this, name, Toast.LENGTH_SHORT).show();
                }else if(!(name.equals(""))){
                    Toast.makeText(Settings.this, "Push the <GET LOCATION> button,please", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(Settings.this, "Name is empty ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        final ImageButton home_button = findViewById(R.id.homeButton);
        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final VibrationEffect vibrationEffect1;
                final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


                Intent myIntent = new Intent(Settings.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Settings.this.startActivity(myIntent);
            }
        });
    }







    public void getGPSlocation (View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (location_GPS == null) {
            if (ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Settings.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                Toast.makeText(this, "ACCESS LOCATION,please" , Toast.LENGTH_LONG).show();

                return;
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                Toast.makeText(this, "Please turn on you location and wait"   , Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(this, "We found your location ", Toast.LENGTH_LONG).show();




    }







}