package com.android.logismove;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Admin on 5/10/2017.
 */

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCampaign();
    }

    public void getUerInfo() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> call = apiService.getUserInfo("1494062364", "0938005053", "559c6c475ee1ef8e93fc0b130409ef9e6b984c0d");
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
                    Log.d("USER", user.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
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
                Log.e("CAMPAIGN", jsonString);
            }

            @Override
            public void onFailure(Call<JsonObject>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
