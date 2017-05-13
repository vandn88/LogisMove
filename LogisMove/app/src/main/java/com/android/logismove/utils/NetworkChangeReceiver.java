package com.android.logismove.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.logismove.MainActivity;
import com.android.logismove.models.LocationObject;
import com.android.logismove.models.LocationSend;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        NetworkHelper.getConnectivityStatusString(context);
        if(NetworkHelper.isConnected) {
            checkIfHaveCacheCampaign(context);
        }
    }

    public void checkIfHaveCacheCampaign(Context context){
        RealmConfiguration config2 = new RealmConfiguration.Builder(context)
                .name("default2")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm myRealm = Realm.getInstance(config2);
        List<LocationSend> locationSends =  myRealm.where(LocationSend.class).findAll();
        if(locationSends.size()>0){
            Log.i("SEND DATA", "true");
        }

    }
}