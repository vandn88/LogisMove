package com.android.logismove.client;

import com.android.logismove.BuildConfig;
import com.android.logismove.MyApplicaiton;
import com.android.logismove.R;
import com.android.logismove.interfaces.AsyncTaskCompleteListener;
import com.android.logismove.utils.CommonUtils;
import com.android.logismove.utils.NetworkHelper;
import com.android.logismove.utils.ShareDataHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @class: BaseProxy
 * @author damngocvan
 * @since 20/04/2015
 * @category proxy
 * @description base class to call service
 * */

public class BaseProxy {
	private String tag = "BaseProxy";
	private AsyncTaskCompleteListener<JSONObject> callback;
	protected static HashMap<String, String> headers;
	protected Headers headerResponse;

	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

	/**
	 * DO GET REQUEST
	 * */
	protected void doGetRequest(final String link, final String params[],
			final String paramsValue[], final AsyncTaskCompleteListener<JSONObject> cb) {
		this.callback = cb;
		Request request = null;
		if (NetworkHelper.isConnected) {
			String url = ClientConstants.API + configUrl(putParams(link, params, paramsValue), link);
			request = new Request.Builder().url(url).build();
			MyApplicaiton.getOkHttpClient().newCall(request).enqueue(responseHandler);
		} else {
			callback.onFailure( R.string.msg_no_internet);
		}
	}

	protected void doPostRequest(String link, double lat, double lng, String id, int progress, AsyncTaskCompleteListener<JSONObject> callback) {
		this.callback = callback;
		if (NetworkHelper.isConnected) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("long",lng);
				obj.put("lat",lat);
				obj.put("register_id",id);
				obj.put("progress",progress);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			/*for (int i = 0; i < params.length; i++) {
				try {
					obj.put(params[i], paramsValue[i]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			// post params
			RequestBody formBody = RequestBody.create(JSON, obj.toString());

			Request request = null;
			try {
				String url = ClientConstants.API + configUrl(link);
				request = new Request.Builder().url(url).post(formBody).build();
				MyApplicaiton.getOkHttpClient().newCall(request)
						.enqueue(responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			callback.onFailure(R.string.msg_no_internet);
		}
	}

	private String putParams(String url, String params[], String[] paramsValue) {
		if(params.length > 0) {
			String mParam = "?" + params[0] + "=" + paramsValue[0];
			for (int i = 1; i < params.length; i++) {
				mParam += "&" + params[i] + "=" + paramsValue[i];
			}
			return url + mParam;
		}
		return url;
	}

	private String configUrl(String url, String secureLink) {
		String mParam = "?";
		if (url.indexOf("?") != -1) {
			mParam = "&";
		}
		String time = CommonUtils.getCurrentTime();
		url += mParam + "time=" + time + "&sign=" + HashLink.secureLink(secureLink, time);
		return url;
	}

	private String configUrl(String url) {
		String mParam = "?";
		if (url.indexOf("?") != -1) {
			mParam = "&";
		}
		String time = CommonUtils.getCurrentTime();
		url += mParam + "time=" + time + "&sign=" + HashLink.secureLink(url, time);
		return url;
	}

	private Callback responseHandler = new Callback() {
		@Override
		public void onFailure(Call call, IOException e) {
			callback.onFailure(R.string.msg_error_connect_server);

		}

		@Override
		public void onResponse(Call call, Response response) throws IOException {
			if (response.isSuccessful()) {
				try {
					String jsonData = response.body().string();
					headerResponse = response.headers();
					JSONObject jsonObj = new JSONObject(jsonData);
					callback.onTaskComplete(jsonObj);
				} catch (JSONException e) {
					callback.onFailure(R.string.msg_error_data);
				}
			} else {
				ErrorPool.handleErrorMsgString(response.code(), callback);
			}
		}
	};

}
