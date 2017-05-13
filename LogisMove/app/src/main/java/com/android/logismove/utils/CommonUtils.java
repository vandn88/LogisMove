package com.android.logismove.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by Admin on 5/11/2017.
 */

public class CommonUtils {
    public static void showMessage(final int stringId, final Activity context) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(context, context.getResources().getString(stringId), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    public static ProgressDialog showProgressBar(Activity mContext, int messageCode) {
        return ProgressDialog.show(mContext, "", mContext.getResources()
                .getString(messageCode), true, false);
    }
    public static String getCurrentTime(){
        return String.valueOf(System.currentTimeMillis()/1000);
    }
}
