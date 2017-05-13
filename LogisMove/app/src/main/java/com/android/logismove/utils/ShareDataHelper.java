package com.android.logismove.utils;

import com.android.logismove.models.UserInfo;

import static okhttp3.internal.Internal.instance;

/**
 * Created by Admin on 5/13/2017.
 */

public class ShareDataHelper {
    private static ShareDataHelper instance = null;

    private UserInfo user;

    protected ShareDataHelper() {
        user = new UserInfo();
    }

    public static ShareDataHelper getInstance() {

        if (instance == null) {
            instance = new ShareDataHelper();
        }
        return instance;
    }
    public UserInfo getUser() {
        return user;
    }

}
