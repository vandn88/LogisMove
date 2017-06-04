package com.android.logismove.models;

import io.realm.RealmObject;

/**
 * Created by Admin on 5/13/2017.
 */

public class LocationSend extends RealmObject {
    private String lat;
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String  campaignId) {
        this.campaignId = campaignId;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    private String campaignId;
    private String locationState;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
}
