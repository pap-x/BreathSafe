package com.example.breaths;

public class DataUser {




    public int condId;
    public String userName;
public String locationGPS;


    public DataUser(String name, String local, int conditionId) {
        this.userName = name;
        this.locationGPS = local;
        this.condId = conditionId;

    }




    public String getLocationGPS() {
        return locationGPS;
    }

    public String getUserName() {
        return userName;
    }

    public int getCondId() {
        return condId;
    }

}