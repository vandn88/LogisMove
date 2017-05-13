package com.android.logismove.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.logismove.R;
import com.android.logismove.models.Campaign;

import java.util.ArrayList;

/**
 * Created by Admin on 5/13/2017.
 */

public class CampaignAdapter extends ArrayAdapter<Campaign> {
    private Context context;
    private Campaign[] campaigns;
    public CampaignAdapter(@NonNull Context context, @LayoutRes int resource, Campaign[] campaigns) {
        super(context, resource, campaigns);
        this.context = context;
        this.campaigns = campaigns;
    }

    public int getCount(){
        return campaigns.length;
    }

    public Campaign getItem(int position){
        return campaigns[position];
    }

    public String getItemID(int position){
        return getItem(position).getCampaginId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parents) {
        TextView label = (TextView) super.getView(position, convertView, parents);
        label.setTextColor(Color.BLACK);
        Campaign campaign = getItem(position);
        label.setText(campaign.getCampaignName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getCampaignName());
        return label;
    }
}
