package com.android.logismove.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.logismove.MainActivity;
import com.android.logismove.MyApplicaiton;
import com.android.logismove.interfaces.AsyncTaskCompleteListener;
import com.android.logismove.models.LocationObject;
import com.android.logismove.models.LocationSend;
import com.google.gson.Gson;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        NetworkHelper.getConnectivityStatusString(context);
    }
}