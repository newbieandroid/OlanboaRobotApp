package com.olanboa.robot;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.hzy.tvmao.KookongSDK;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.api.OrviboApi;
import com.orvibo.homemate.api.UserApi;
import com.orvibo.homemate.data.IDC;
import com.orvibo.homemate.util.ActivityManager;

public class MyApp extends Application {

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onCreate() {
        super.onCreate();


        UserApi.setDebugMode(true, true);
        //初始化HomeMate SDK。如果app继承了VihomeApplication则不需要再初始化sdk；如果没有继承则需要先初始化。
        OrviboApi.initHomeMateSDK(this);

        KookongSDK.init(this, "sdk_android", getResources().getString(R.string.oriboaKey), getResources().getString(R.string.oriboaSource));

        UserApi.initSource(getResources().getString(R.string.oriboaSource), IDC.DEFAULT);

        CacheUtil.getInstance().init(this);

        //页面状态监听
        registerActivityLifecycleCallbacks(new ActivityStateListener());


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private class ActivityStateListener implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ActivityManager.getInstance().pushActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ActivityManager.getInstance().popActivity(activity);
        }
    }
}
