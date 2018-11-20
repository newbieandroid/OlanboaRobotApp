package com.olanboa.robot.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.olanboa.robot.datas.CacheKeys;

public class CacheUtil {

    private SharedPreferences sharedPreferences;


    private static CacheUtil instance;

    private CacheUtil() {

    }


    public static CacheUtil getInstance() {

        if (instance == null) {
            instance = new CacheUtil();
        }

        return instance;
    }


    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences("OlanboaCache", Context.MODE_PRIVATE);
    }

    public void savaBooleanCache(String key, boolean data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, data);
        editor.apply();
    }

    public boolean getBooleanCache(String key, boolean defaultResult) {
        return sharedPreferences.getBoolean(key, defaultResult);
    }


}
