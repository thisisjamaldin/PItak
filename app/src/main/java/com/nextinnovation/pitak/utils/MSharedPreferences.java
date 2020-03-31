package com.nextinnovation.pitak.utils;

import android.content.Context;

public class MSharedPreferences {

    public static <T> void set(Context context, String key, T value) {
        if (value instanceof Integer) {
            context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof String) {
            context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putString(key, (String) value).apply();
        } else if (value instanceof Boolean) {
            context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putBoolean(key, (Boolean) value).apply();
        } else {

        }
    }

    public static <T> T get(Context context, String key, T defValue) {
        if (defValue instanceof Integer) {
            return (T) Integer.valueOf(context.getSharedPreferences("settings", Context.MODE_PRIVATE).getInt(key, (Integer) defValue));
        } else if (defValue instanceof String) {
            return (T) context.getSharedPreferences("settings", Context.MODE_PRIVATE).getString(key, (String) defValue);
        } else if (defValue instanceof Boolean) {
            return (T) Boolean.valueOf(context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean(key, (Boolean) defValue));
        } else {
            return null;
            //TODO: Add other types;
        }
    }

    public static void clear(Context context) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit().clear().apply();
    }
}
