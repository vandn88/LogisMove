package com.android.logismove;

import io.realm.RealmObject;

/**
 * Created by Admin on 5/7/2017.
 */

public class LocationObject extends RealmObject {
    public static final int STATUS_START = 0;
    public static final int STATUS_ONGOING = 1;
    public static final int STATUS_END = 2;

    private double Latitude;
    private double Longitude;

    private long Miliseconds;
    private int Status;

    public LocationObject() {
        new LocationObject(0, 0, 0, STATUS_ONGOING);
    }

    public LocationObject(double latitude, double longitude, long miliseconds, int status) {
        Latitude = latitude;
        Longitude = longitude;
        Miliseconds = miliseconds;
        Status = status;
    }

    public long getMiliseconds() {
        return Miliseconds;
    }

    public void setMiliseconds(long mMiliseconds) {
        this.Miliseconds = mMiliseconds;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int mStatus) {
        this.Status = mStatus;
    }


    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double mLatitude) {
        this.Latitude = mLatitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double mLongitude) {
        this.Longitude = mLongitude;
    }


}
