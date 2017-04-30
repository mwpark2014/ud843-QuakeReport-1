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
    private long mDate;

    public Earthquake(double magnitude, String city, long date) {
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

    public long getDate() {
        return mDate;
    }
}
