package com.android.logismove;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.logismove.client.UserProxy;

import java.io.File;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

import static java.security.AccessController.getContext;

/**
 * Created by Admin on 5/10/2017.
 */

public class MyApplicaiton extends MultiDexApplication {
    private static OkHttpClient mOkHttpClient;
    private static UserProxy userProxy;

    @Override
    protected void attachBaseContext(Context newBase) {
        MultiDex.install(newBase);
        super.attachBaseContext(newBase);
    }

    /**
     * OkHttpClient singleton with 2 MB cache.
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        return mOkHttpClient;
    }

    public static UserProxy getUserProxy() {
        if (userProxy == null)
            userProxy = new UserProxy();
        return userProxy;
    }

}
