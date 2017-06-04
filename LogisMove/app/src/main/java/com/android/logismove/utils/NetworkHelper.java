package com.android.logismove.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.android.logismove.R;

public class NetworkHelper {

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	public static boolean isConnected = false;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;

			if(activeNetwork.isConnected())
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = NetworkHelper.getConnectivityStatus(context);
		String status = "";
		if (conn == NetworkHelper.TYPE_WIFI) {
			status = "wifi";
			isConnected = true;
		} else if (conn == NetworkHelper.TYPE_MOBILE) {
			status = "3g";
			isConnected = true;

		} else if (conn == NetworkHelper.TYPE_NOT_CONNECTED) {
			status = context.getResources().getString(R.string.msg_no_internet);
			isConnected = false;
		}
		return status;
	}

	private boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
