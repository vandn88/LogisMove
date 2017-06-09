package com.android.logismove.client;

import android.text.TextUtils;
import android.util.Log;

import com.android.logismove.R;
import com.android.logismove.models.Campaign;
import com.android.logismove.models.UserInfo;
import com.android.logismove.interfaces.AsyncTaskCompleteListener;
import com.android.logismove.utils.CommonUtils;
import com.android.logismove.utils.NetworkHelper;
import com.android.logismove.utils.ShareDataHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.RequestBody;

import static android.R.attr.id;
import static android.R.id.list;

/**
 * Created by Admin on 5/13/2017.
 */

public class UserProxy extends BaseProxy {
    private final static String TAG_USER_INFO = "info";
    private final static String TAG_USER_CAMPAIGN = "/getcampaign";
    private final static String TAG_LOCATION = "locate";
    private final static String TAG_LOCATION_LIST = "locatelist";
    private final static String TAG_JSON_DATA = "msg_data";

   // @Query("time") String time, @Query("phone") String phone, @Query("sign") String sign
   //@GET("api/tracker/info")
    public void getUserInfo(String userPhoneNum, final AsyncTaskCompleteListener<Boolean> callback) {
        String secureLink = ClientConstants.TAG_API + ClientConstants.TAG_TRACKER + TAG_USER_INFO;
        String[] arrParams = {"phone"};
        String[] paramsValue = {userPhoneNum};
        super.doGetRequest(secureLink, arrParams, paramsValue, new AsyncTaskCompleteListener<JSONObject>() {
            @Override
            public void onTaskComplete(JSONObject result) {
                UserInfo user = ShareDataHelper.getInstance().getUser();
                try {
                    JSONObject jsonObjectData = result.getJSONObject(TAG_JSON_DATA);
                    user.setId(String.valueOf(jsonObjectData.getInt("id")));
                    user.setPhone(jsonObjectData.getString("phone"));
                    user.setEmail(jsonObjectData.getString("email"));
                    user.setIdentityCardNum(jsonObjectData.getString("identity_card_num"));
                    user.setName(jsonObjectData.getString("fullname"));
                    callback.onTaskComplete(true);
                }catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(R.string.msg_error_data);
                }
            }

            @Override
            public void onFailure(int errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }


    //@GET("api/tracker/{id}/getcampaign")
    //(@Path("id") int id, @Query("time") String time, @Query("sign") String sign);
    public void getUserCampaign(String userId, final AsyncTaskCompleteListener<ArrayList<Campaign>> callback) {
        String secureLink = ClientConstants.TAG_API + ClientConstants.TAG_TRACKER + userId + TAG_USER_CAMPAIGN;
        String[] arrParams = {};
        String[] paramsValue = {};
        super.doGetRequest(secureLink, arrParams, paramsValue, new AsyncTaskCompleteListener<JSONObject>() {
            @Override
            public void onTaskComplete(JSONObject result) {
                ArrayList<Campaign> lstCampaign = new ArrayList<Campaign>();
                try {
                    JSONArray arrJson = result.getJSONArray(TAG_JSON_DATA);
                    for (int i = 0; i < arrJson.length(); i++) {
                        JSONObject obj = arrJson.getJSONObject(i);
                        lstCampaign.add(new Campaign(String.valueOf(obj.getInt("id")), obj.getString("name")));
                    }
                    callback.onTaskComplete(lstCampaign);
                }catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(R.string.msg_error_data);
                }
            }

            @Override
            public void onFailure(int errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    //@GET("api/tracker/locate")
    //(@Query("time") String time, @Query("sign") String sign);
    //@param {"long":106.7008,"lat":10.7759,"register_id":1,"progress":2, register_id:1 (id from getcampaign), tracker_id: user id}
    public void postLocation(double lat, double lng, String campaignId, int progress,
                             final AsyncTaskCompleteListener<Boolean> callback) {
        String secureLink = ClientConstants.TAG_API + ClientConstants.TAG_TRACKER + TAG_LOCATION;
        JSONObject obj = new JSONObject();
        try {
            obj.put("long",lng);
            obj.put("lat",lat);
            obj.put("register_id",campaignId);
            obj.put("progress",progress);
            obj.put("tracker_id", ShareDataHelper.getInstance().getUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // post params
        super.doPostRequest(secureLink, obj.toString(),
                new AsyncTaskCompleteListener<JSONObject>() {

                    @Override
                    public void onTaskComplete(JSONObject result) {
                        String TAG_MSG = "msg_code";
                        String TAG_STATUS = "status";
                        String TAG_ERROR_CODE = "error_code";
                        try {
                           if(TextUtils.equals(result.getString(TAG_MSG), "success")) {
                               callback.onTaskComplete(true);
                           }
                           else
                                callback.onFailure(R.string.msg_error_data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(R.string.msg_error_data);
                        }
                    }

                    @Override
                    public void onFailure(int errorMessage) {
                        // TODO Auto-generated method stub
                        callback.onFailure(errorMessage);
                    }
                });
    }

    public void postListLocation(String jsonParams, final AsyncTaskCompleteListener<Boolean> callback) {
        String secureLink = ClientConstants.TAG_API + ClientConstants.TAG_TRACKER + TAG_LOCATION_LIST;
        // post params
        super.doPostRequest(secureLink, jsonParams, new AsyncTaskCompleteListener<JSONObject>() {

                    @Override
                    public void onTaskComplete(JSONObject result) {
                        String TAG_MSG = "msg_code";
                        String TAG_STATUS = "status";
                        String TAG_ERROR_CODE = "error_code";
                        try {
                            if(TextUtils.equals(result.getString(TAG_MSG), "success")) {
                                callback.onTaskComplete(true);
                            }
                            else
                                callback.onFailure(R.string.msg_error_data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(R.string.msg_error_data);
                        }
                    }

                    @Override
                    public void onFailure(int errorMessage) {
                        // TODO Auto-generated method stub
                        callback.onFailure(errorMessage);
                    }
                });
    }

}
