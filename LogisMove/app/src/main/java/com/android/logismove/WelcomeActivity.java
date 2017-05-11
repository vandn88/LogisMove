package com.android.logismove;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.logismove.client.ApiClient;
import com.android.logismove.client.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.button;

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
        String phoneNum = PreferenceUtil.getSharedPreferences(this, PreferencesKey.PREF_USER_ID);
        if(!TextUtils.equals(phoneNum, "0")) {
            getUserInfo(phoneNum);
        } else {
            if (!checkPermissions()) {
                requestPermissions();
            }
            else checkPhoneNum();
        }
    }

    public void checkPhoneNum(){
        final String phoneNum = getMyPhoneNum();
        if (TextUtils.isEmpty(phoneNum)) {
            findViewById(R.id.linear_phonenum_input).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            Toast.makeText(this, getString(R.string.warning_user_have_no_phonenum), Toast.LENGTH_SHORT).show();
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

                    if(s.toString().trim().length()==0){
                        buttonOK.setEnabled(false);
                    } else {
                        buttonOK.setEnabled(true);
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
    }

    public void getUserInfo(String phoneNum) {
        PreferenceUtil.saveSharedPreferences(this, PreferencesKey.PREF_USER_ID, phoneNum);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> call = apiService.getUserInfo("1494062364", phoneNum, "559c6c475ee1ef8e93fc0b130409ef9e6b984c0d");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                String jsonString = response.body().toString();
                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    JSONObject jsonObjectData = jsonObj.getJSONObject("msg_data");
                    UserInfo user = new UserInfo();
                    user.setId(jsonObjectData.getInt("id"));
                    user.setPhone(jsonObjectData.getString("phone"));
                    user.setEmail(jsonObjectData.getString("email"));
                    user.setIdentityCardNum(jsonObjectData.getString("identity_card_num"));
                    Log.d("USER INFO GET", user.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject>call, Throwable t) {
                // Log error here since request failed
                Log.e("USER INFO GET", t.toString());
            }
        });
    }

    public void getCampaign() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> call = apiService.getCampaigns(1, "1494062364", "8666d3b1547a95afc44798514add63c815abf4c8");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                String jsonString = response.body().toString();
                Log.e("CAMPAIGN GET", jsonString);
            }

            @Override
            public void onFailure(Call<JsonObject>call, Throwable t) {
                // Log error here since request failed
                Log.e("CAMPAIGN GET", t.toString());
            }
        });
    }

    public String getMyPhoneNum() {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
            return null;

        String mPhoneNumber = tm.getLine1Number();
        return mPhoneNumber;
    }

    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_PHONE_STATE);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_phone_state,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(WelcomeActivity.this,
                                    PERMISSIONS_PHONE_STATE,
                                    REQUEST_PHONE_STATE_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            // permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(WelcomeActivity.this,
                    PERMISSIONS_PHONE_STATE,
                    REQUEST_PHONE_STATE_PERMISSIONS_REQUEST_CODE);
        }
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
               checkPhoneNum();
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

}
