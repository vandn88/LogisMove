package com.android.logismove.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Created by Admin on 5/11/2017.
 */

public class CommonUtils {
    public static void showMessage(final int stringId, final Activity context) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(context, context.getResources().getString(stringId), Toast.LENGTH_LONG);
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
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

}
