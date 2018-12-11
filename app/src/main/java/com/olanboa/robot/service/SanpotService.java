package com.olanboa.robot.service;


import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.olanboa.robot.ProcessConnection;
import com.olanboa.robot.activity.LoginActivity;
import com.olanboa.robot.datas.CacheKeys;
import com.olanboa.robot.datas.GrammerData;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.api.LocalDataApi;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.bo.Device;
import com.orvibo.homemate.event.BaseEvent;
import com.orvibo.homemate.model.family.FamilyManager;
import com.orvibo.homemate.util.ActivityManager;
import com.robot.oriboa.helper.DeviceControHelper;
import com.sanbot.opensdk.base.BindBaseService;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.speech.Grammar;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.interfaces.speech.RecognizeListener;

import java.util.List;
import java.util.Random;

import static com.olanboa.robot.datas.GrammerData.closeOrder;
import static com.olanboa.robot.datas.GrammerData.openOrder;


public class SanpotService extends BindBaseService {


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("csl", "SanpotService:建立链接");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //断开链接
            startService(new Intent(SanpotService.this, GuardService.class));
            //重新绑定
            bindService(new Intent(SanpotService.this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };


    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };

    }


    @Override
    public void onCreate() {
        register(SanpotService.class);
        super.onCreate();

        if (!CacheUtil.getInstance().getBooleanCache(CacheKeys.ISLOGIN, false)) {
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            ActivityManager.getInstance().finishAllActivity();
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(1, new Notification());
        //绑定建立链接
        bindService(new Intent(this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    protected void onMainServiceConnected() {


        //设置机器人的表情
        SystemManager systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
        if (systemManager != null) {
            systemManager.showEmotion(EmotionsType.SMILE);
        }


        final SpeechManager speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);

        if (speechManager != null) {


            //开始欢迎语的语音合成
            startSpeak(speechManager, GrammerData.SanPotWelcome);

            //监听机器人识别的文字
            speechManager.setOnSpeechListener(new RecognizeListener() {

                @Override
                public void onStopRecognize() {

                }

                @Override
                public void onStartRecognize() {

                }

                @Override
                public void onRecognizeVolume(int i) {

                }

                @Override
                public boolean onRecognizeResult(Grammar grammar) {
                    //当返回值为true时，表示机器人不再对该文字做后续响应，false反之


                    //获取家庭下所有的设备
                    String currentFamilyId = CacheUtil.getInstance().getStringCache(CacheKeys.CURRENTFAMILYID, "");

                    if (TextUtils.isEmpty(currentFamilyId)) {
                        currentFamilyId = FamilyManager.getCurrentFamilyId();
                        CacheUtil.getInstance().savaStringCache(CacheKeys.CURRENTFAMILYID, currentFamilyId);
                    }


                    //获取所有的设备
                    final List<Device> deviceList = LocalDataApi.getDevicesByFamily(currentFamilyId);


                    for (Device item : deviceList) {
                        Log.e("csl", "设备信息:" + new Gson().toJson(item));
                    }


                    CacheUtil.getInstance().getStringCache(CacheKeys.CURRENTFAMILYID, "");


                    String meansText = grammar.getText();

                    Log.e("csl", "=======机器人识别的文字=====" + meansText);

                    for (Device item : deviceList) {

                        if (meansText.contains(item.getDeviceName())) {


                            DeviceControHelper deviceControHelper = new DeviceControHelper(item);

                            if (meansText.contains(openOrder)) {
                                startSpeak(speechManager, GrammerData.orderDO[new Random().nextInt(GrammerData.orderDO.length)]);

                                deviceControHelper.deviceSwitch(true, new BaseResultListener() {
                                    @Override
                                    public void onResultReturn(BaseEvent baseEvent) {
                                    }
                                });

                            } else if (meansText.contains(closeOrder)) {
                                startSpeak(speechManager, GrammerData.orderDO[new Random().nextInt(GrammerData.orderDO.length)]);

                                deviceControHelper.deviceSwitch(false, new BaseResultListener() {
                                    @Override
                                    public void onResultReturn(BaseEvent baseEvent) {
                                    }
                                });

                            } else {
                                startSpeak(speechManager, GrammerData.orderError);
                            }


                            return true;
                        }

                    }

                    Log.e("csl", "识别的内容交给机器人自己处理");

                    return false;
                }

                @Override
                public void onError(int i, int i1) {

                }
            });


        }
    }


    private void startSpeak(SpeechManager speechManager, String text) {
        //开始欢迎语的语音合成
        final SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_CHINESE);
        speechManager.startSpeak(text, speakOption);
    }
}
