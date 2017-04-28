package com.example.android.quakereport;

/**
 * Created by Mason on 4/28/2017.
 */

public class Earthquake {
    //magnitude of earthquake
    private double mMagnitude;
    //city of earthquake
    private String mCity;
    //date when earthquake originated
    private String mDate;

    public Earthquake(double magnitude, String city, String date) {
        mMagnitude = magnitude;
        mCity= city;
        mDate = date;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getCity() {
        return mCity;
    }

    public String getDate() {
        return mDate;
    }
}
