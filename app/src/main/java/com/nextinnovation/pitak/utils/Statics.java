package com.nextinnovation.pitak.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import okhttp3.ResponseBody;

public class Statics {
    public static String REGISTERED = "registered";
    public static String USER = "user";
    public static String DRIVER = "DRIVER";
    public static String PASSENGER = "PASSENGER";
    public static String SIGN_UP = "SIGNUP";
    public static String SIGN_IN = "SIGNIN";

    public static String getString(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getResponseError(ResponseBody responseBody) {
        String error = "";
        try {
            error = responseBody.string();
            Log.e("-----ErrorBody", error);
            JSONObject jObjError = new JSONObject(error);
            return jObjError.getString("details");
        } catch (Exception e) {
            try {
                JSONObject jObjError = new JSONObject(error);
                return jObjError.getJSONArray("errors").getJSONObject(0).getString("defaultMessage");
            } catch (Exception t) {
                try {
                    JSONObject jObjError = new JSONObject(error);
                    return jObjError.getString("error");
                } catch (Exception x) {
                    return "Error";
                }
            }
        }
    }

    public static void call(String number, Context context) {
        number = "tel:" + number;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(number));
        context.startActivity(intent);
    }

    public static void openWhatsapp(String number, Context context) {
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void setToken(Context context, String token) {
        MSharedPreferences.set(context, "Token", token);
    }

    public static String getToken(Context context) {
        return "Bearer " + MSharedPreferences.get(context, "Token", "");
    }

    public static void loadImage(ImageView image, String url, Boolean circle) {
        if (circle==null) {
            Glide.with(image.getContext()).load(url).into(image);
        } else if (circle){
            Glide.with(image.getContext()).load(url).apply(RequestOptions.circleCropTransform()).into(image);
        } else {
            Glide.with(image.getContext()).load(url).centerCrop().into(image);
        }
        //        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("Authorization", Statics.getToken(image.getContext())).build());
    }

}
