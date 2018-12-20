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


    public void savaStringCache(String key, String data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public String getStringCache(String key, String defaultResult) {
        return sharedPreferences.getString(key, defaultResult);
    }

    public void savaBooleanCache(String key, boolean data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, data);
        editor.apply();
    }

    public boolean getBooleanCache(String key, boolean defaultResult) {
        return sharedPreferences.getBoolean(key, defaultResult);
    }


    public void clearLoginInfo() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CacheKeys.ISLOGIN);
        editor.remove(CacheKeys.LOGINACCOUNT);
        editor.remove(CacheKeys.LOGINPASS);
        editor.apply();

    }

    public void saveLoginInfo(String userName, String passWord) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CacheKeys.ISLOGIN, true);
        editor.putString(CacheKeys.LOGINACCOUNT, userName);
        editor.putString(CacheKeys.LOGINPASS, passWord);
        editor.apply();

    }
}
