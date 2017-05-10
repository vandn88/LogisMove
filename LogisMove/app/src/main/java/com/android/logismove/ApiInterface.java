package com.android.logismove;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Admin on 5/10/2017.
 */

public interface ApiInterface {
    @GET("api/tracker/info")
    Call<JsonObject> getUserInfo(@Query("time") String time, @Query("phone") String phone, @Query("sign") String sign);

   @GET("api/tracker/{id}/getcampaign")
    Call<JsonObject> getCampaigns(@Path("id") int id, @Query("time") String time, @Query("sign") String sign);

    @POST("api/tracker/locatelist")
    Call<JsonObject> sendUserLocation(@Field("time") String time, @Field("sign") String sign);
}
