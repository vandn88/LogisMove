package com.android.logismove.client;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Admin on 5/11/2017.
 */

public class CommonUtils {
    public static void showMessage(final int stringId, Context context) {
        Toast toast = Toast.makeText(context, context.getString(stringId), Toast.LENGTH_SHORT);
        toast.show();
    }
}
