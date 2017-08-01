package com.android.logismove;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.logismove.interfaces.AsyncTaskCompleteListener;
import com.android.logismove.models.LocationSend;
import com.android.logismove.models.UserInfo;
import com.android.logismove.utils.CommonUtils;
import com.android.logismove.utils.NetworkHelper;
import com.android.logismove.utils.ShareDataHelper;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Admin on 5/10/2017.
 */

public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = "WelcomeActivity";
    // Used in checking for runtime permissions.
    private static final int REQUEST_PHONE_STATE_PERMISSIONS_REQUEST_CODE = 35;
    private static String[] PERMISSIONS_PHONE_STATE = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initApp();
        if(isUserSignIn()) {
            if(NetworkHelper.isConnected) {
                checkIfHaveCacheCampaign(WelcomeActivity.this);
            } else {
                CommonUtils.showMessage(R.string.msg_no_internet, WelcomeActivity.this);
            }
        } else {
           initView();
        }
    }

    public boolean isUserSignIn() {
        SharedPreferences userInfo = getSharedPreferences(PreferencesKey.PREF_USER_INFO, 0);
        if (userInfo.contains(PreferencesKey.PREF_USER_ID)) {
            final UserInfo user = ShareDataHelper.getInstance().getUser();
            user.setId(userInfo.getString(PreferencesKey.PREF_USER_ID, "0"));
            user.setName(userInfo.getString(PreferencesKey.PREF_USER_NAME, ""));
            user.setIdentityCardNum(userInfo.getString(PreferencesKey.PREF_USER_EDENTITY, ""));
            user.setPhone(userInfo.getString(PreferencesKey.PREF_USER_PHONE, ""));
            user.setEmail(userInfo.getString(PreferencesKey.PREF_USER_EMAIL, ""));
            return true;
        }
        return false;
    }

    public void initApp() {
        NetworkHelper.getConnectivityStatusString(getApplicationContext());
    }

    public void saveUserInfoToCache() {
        UserInfo user = ShareDataHelper.getInstance().getUser();

        SharedPreferences userPref = getSharedPreferences(PreferencesKey.PREF_USER_INFO, 0);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString(PreferencesKey.PREF_USER_ID, user.getId());
        editor.putString(PreferencesKey.PREF_USER_NAME, user.getName());
        editor.putString(PreferencesKey.PREF_USER_EDENTITY, user.getIdentityCardNum());
        editor.putString(PreferencesKey.PREF_USER_EMAIL, user.getEmail());
        editor.putString(PreferencesKey.PREF_USER_PHONE, user.getPhone());
        editor.apply();
    }

    public void gotoMain() {
        final Intent myIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    public void getUserInfo(String phoneNum) {
        final ProgressDialog progressDialog = CommonUtils.showProgressBar(WelcomeActivity.this, R.string.msg_loading);
        MyApplicaiton.getUserProxy().getUserInfo(phoneNum, new AsyncTaskCompleteListener<Boolean>() {
            @Override
            public void onTaskComplete(Boolean isSuccess) {
                progressDialog.dismiss();
                saveUserInfoToCache();
                gotoMain();
            }

            @Override
            public void onFailure(final int errorMessage) {
                progressDialog.dismiss();
                CommonUtils.showMessage(errorMessage, WelcomeActivity.this);
            }
        });
    }


    public void initView(){
        findViewById(R.id.linear_phonenum_input).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        final EditText editTextPhoneNum = (EditText)findViewById(R.id.input_phone_num);
        final View buttonOK = findViewById(R.id.btn_enter_phone_num);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo(editTextPhoneNum.getText().toString());
            }
        });

        editTextPhoneNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()>9){
                    buttonOK.setEnabled(true);
                } else {
                    buttonOK.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_STATE_PERMISSIONS_REQUEST_CODE) {
            Log.i(TAG, "Received response for phone state permissions request.");
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
               initView();
            } else {
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_phone_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }


    public void checkIfHaveCacheCampaign(Context context){
        final RealmConfiguration config2 = new RealmConfiguration.Builder(context)
                .name("default2")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();

        final Realm myRealm = Realm.getInstance(config2);
        List<LocationSend> locationSends =  myRealm.where(LocationSend.class).findAll();
        if(locationSends.size()>0){
            String json ="{\"list\":[";
            for(int i = 0; i<locationSends.size(); i++) {
                LocationSend locationSend = locationSends.get(i);
                if(locationSend.getTime() == null)
                    continue;
                json += "{\"long\":" + locationSend.getLng() + ","
                        + "\"lat\":" + locationSend.getLat() + ","
                        + "\"register_id\":" + locationSend.getCampaignId() + ","
                        + "\"created_at\":" + locationSend.getTime() + ","
                        + "\"tracker_id\":" + ShareDataHelper.getInstance().getUser().getId()+ ","
                        + "\"progress\":" + locationSend.getLocationState()
                        + "},";
            }
            json = json.substring(0, json.length()-1);
            json += "]}";
            MyApplicaiton.getUserProxy().postListLocation(json, new AsyncTaskCompleteListener<Boolean>() {
                @Override
                public void onTaskComplete(Boolean isSuccess) {
                    if(isSuccess) {
                        Realm realm = Realm.getInstance(config2);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.clear(LocationSend.class);
                            }});
                    }
                    gotoMain();
                }

                @Override
                public void onFailure(int errorCode) {
                    if (BuildConfig.DEBUG) {
                        CommonUtils.showMessage(errorCode, WelcomeActivity.this);
                    }
                    gotoMain();
                }
            });
        } else
            gotoMain();

    }

}
