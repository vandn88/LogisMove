package com.android.logismove.client;

/**
 * Created by Admin on 5/10/2017.
 */

public interface ApiInterface {
  /*  @GET("api/tracker/info")
    Call<JsonObject> getUserInfo(@Query("time") String time, @Query("phone") String phone, @Query("sign") String sign);

   @GET("api/tracker/{id}/getcampaign")
    Call<JsonObject> getCampaigns(@Path("id") int id, @Query("time") String time, @Query("sign") String sign);

    @POST("api/tracker/locatelist")
    Call<JsonObject> sendUserLocation(@Field("time") String time, @Field("sign") String sign);*/

   /*  public void getUserInfo(String phoneNum) {
        PreferenceUtil.saveSharedPreferences(this, PreferencesKey.PREF_USER_ID, phoneNum);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> call = apiService.getUserInfo("1494062364", phoneNum, "559c6c475ee1ef8e93fc0b130409ef9e6b984c0d");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                String jsonString = response.body().toString();
                if(TextjsonString)
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
    }*/
}
