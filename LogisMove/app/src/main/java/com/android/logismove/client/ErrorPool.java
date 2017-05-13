package com.android.logismove.client;

import com.android.logismove.R;
import com.android.logismove.interfaces.AsyncTaskCompleteListener;

import org.json.JSONObject;

import static android.view.View.GONE;

/**
 * Created by Admin on 5/13/2017.
 */

public class ErrorPool {
    // data server tra ve khong co gi thay doi, khong can update view.
    public final static int NOT_MODIFY = 304;

    // Khi gap loi nay kiem tra danh sach params gui len server xem co bi thieu
    // khong.
    public final static int BAD_REQUEST = 400;

    // Khi gap loi nay yeu cau nguoi dung dang nhap lai
    public final static int UNAUTHORIZED = 401;

    // Khi gap loi nay yeu cau nguoi dung dang nhap lai
    public final static int FORBIDDEN = 403;

    // Nguoi dung khong co quyen xem thong tin vod hoac channel
    public final static int NOT_ACCEPTABLE = 406;

    // Loi ket noi server
    public final static int TIMEOUT = 408;

    // Loi du lieu bi xoa
    public final static int GONE = 410;
    public static void handleErrorMsgString(int errorCode,  AsyncTaskCompleteListener<JSONObject> callback) {
        switch (errorCode) {
            case NOT_MODIFY:
                callback.onTaskComplete(null);
                break;
            case FORBIDDEN:
                callback.onFailure(R.string.msg_error_forbidden);
                break;
            case BAD_REQUEST:
                callback.onFailure(R.string.msg_error_bad_request);
                break;
            case UNAUTHORIZED:
                callback.onFailure(R.string.msg_require_login);
                break;
            case NOT_ACCEPTABLE:
                callback.onFailure(R.string.msg_error_forbidden);
                break;
            case TIMEOUT:
                callback.onFailure(R.string.msg_error_connect_server);
                break;
            case GONE:
                callback.onFailure(R.string.msg_error_item_is_deleted);
                break;
            default:
                callback.onFailure(R.string.msg_error_server);
                break;
        }
    }
}
