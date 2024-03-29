package com.nextinnovation.pitak.utils;

import android.content.Context;
import android.widget.Toast;

import com.nextinnovation.pitak.R;

import okhttp3.ResponseBody;

public class MToast {

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public static void showInternetError(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
    }
    public static void showResponseError(Context context, ResponseBody responseBody) {
        Toast.makeText(context, Statics.getResponseError(responseBody), Toast.LENGTH_LONG).show();
    }
}
