package com.android.logismove;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 5/7/2017.
 */

public class PreferenceUtil {
    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    static boolean requestingLocationUpdates(Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        android.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "Lat:" + location.getLatitude() + ", Long:" + location.getLongitude() + ")";
    }

    static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
    }

    public static String getSharedPreferences(Context mContext, String key) {
        SharedPreferences preferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(mContext);
        String str = preferences.getString(key, "0");
        return str;
    }

    public static void saveSharedPreferences(Context mContext, String key,
                                             String value) {
        SharedPreferences preferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        // apply: it commits without returning a boolean indicating success or
        // failure so it is faster than commit
    }
}
