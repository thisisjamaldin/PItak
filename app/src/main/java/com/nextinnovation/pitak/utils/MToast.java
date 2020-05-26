package com.nextinnovation.pitak.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.nextinnovation.pitak.R;

import okhttp3.ResponseBody;

public class MToast {

    public static void show(Context context, String msg) {
        Log.e("-------toast", msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public static void showInternetError(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
    }
    public static void showResponseError(Context context, ResponseBody responseBody) {
        Toast.makeText(context, Statics.getResponseError(responseBody, context.getClass().getName()), Toast.LENGTH_LONG).show();
    }
}
