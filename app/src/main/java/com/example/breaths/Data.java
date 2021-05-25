package com.example.breaths;

public class Data {




    public Long condition_id;
    public String userName;


    public String locationGPS;


    public Data(String name, String l ) {
        this.userName = name;
        this.locationGPS = l;


    }




    public String getLocationGPS() {
        return locationGPS;
    }

    public String getUserName() {
        return userName;
    }

}