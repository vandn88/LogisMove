package com.android.logismove.models;

/**
 * Created by Admin on 5/13/2017.
 */

public class Campaign {
    private String campaginId;

    public String getCampaginId() {
        return campaginId;
    }

    public void setCampaginId(String campaginId) {
        this.campaginId = campaginId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    private String campaignName;

    public Campaign(String campaginId, String campaignName) {
        this.campaginId = campaginId;
        this.campaignName = campaignName;
    }
}
