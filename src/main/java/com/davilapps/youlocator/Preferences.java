package com.davilapps.youlocator;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.SharedPreferences;

import com.google.android.gms.common.internal.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Preferences {

    public static void setPrefs(String key, String value, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.EXTRA_PRIORITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPrefs(String key, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.EXTRA_PRIORITY, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "notfound");
    }



}
