package com.example.realmsample.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2018/1/3.
 */

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
