package com.example.breaths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class  Settings extends AppCompatActivity   {







    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;

    int PERMISSION_ID = 44;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

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

                 //Button save_button = findViewById(R.id.button_location);

                TextView mLocation = findViewById(R.id.text_of_my_location);

                String mylocation = save_button.getText().toString();



                if(!(name.equals("")) &&  !( mylocation == null)) {

                    DataUser data = new DataUser(name,mylocation,conditionId );
                    dbHandler.addUserInfo(data);

                    Toast.makeText(Settings.this, "Saved", Toast.LENGTH_SHORT).show();
                }else if(!(name.equals(""))){
                    Toast.makeText(Settings.this, "Not found Location Restart the App", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(Settings.this, "Name field is empty"+name, Toast.LENGTH_SHORT).show();

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


    @SuppressLint("MissingPermission")
    private String getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            String locationComplete;
                            locationComplete = (location.getLatitude()+","+location.getLongitude());
                            getSavedLoc(locationComplete);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            String onLocation;
            onLocation= (mLastLocation.getLatitude()+","+mLastLocation.getLongitude());


        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public void getSavedLoc(String location_) {
       // Toast.makeText (this, l, Toast.LENGTH_SHORT).show();

        TextView mLocation = findViewById(R.id.text_of_my_location);

        mLocation.setText(location_);



    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }













}