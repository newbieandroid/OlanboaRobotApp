package com.olanboa.robot.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.olanboa.robot.R;
import com.olanboa.robot.activity.LoginActivity;
import com.olanboa.robot.datas.CacheKeys;
import com.olanboa.robot.datas.GrammerData;
import com.olanboa.robot.util.BdSdkUtils;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.api.LocalDataApi;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.bo.Device;
import com.orvibo.homemate.bo.Room;
import com.orvibo.homemate.core.product.ProductManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static com.olanboa.robot.datas.GrammerData.closeOrder;
import static com.olanboa.robot.datas.GrammerData.highState;
import static com.olanboa.robot.datas.GrammerData.lowerState;
import static com.olanboa.robot.datas.GrammerData.openOrder;


public class SanpotService extends BindBaseService {
    private ServiceConnection mConnection;

    private static final int PID = android.os.Process.myPid();

    private static final int serviceFlags = 100;


    public void setForeground() {
        // sdk < 18 , 直接调用startForeground即可,不会在通知栏创建通知
        if (Build.VERSION.SDK_INT < 18) {
            this.startForeground(serviceFlags, getNotification());
            return;
        }

        if (null == mConnection) {
            mConnection = new CoverServiceConnection();
        }

        this.bindService(new Intent(this, SanpotService.class), mConnection,
                Service.BIND_AUTO_CREATE);
    }

    private Notification getNotification() {
        // 定义一个notification
        Notification notification = new Notification();
        Intent notificationIntent = new Intent(this, SanpotService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        // notification.setLatestEventInfo(this, "Foreground", "正在运行哦",
        // pendingIntent);

        Notification.Builder builder = new Notification.Builder(this)
                .setAutoCancel(true).setContentTitle("欧朗博")
                .setContentText("智能语音控制系统正在运行").setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis()).setOngoing(true);
        notification = builder.getNotification();
        return notification;
    }

    private class CoverServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {

            // sdk >= 18 的，会在通知栏显示service正在运行，这里不要让用户感知，所以这里的实现方式是利用2个同进程的service，利用相同的notificationID，
            // 2个service分别startForeground，然后只在1个service里stopForeground，这样即可去掉通知栏的显示
            Service helpService = ((GuardService.LocalBinder) binder)
                    .getService();
            startForeground(serviceFlags, getNotification());
            helpService.startForeground(PID, getNotification());
            helpService.stopForeground(true);

            SanpotService.this.unbindService(mConnection);
            mConnection = null;
        }
    }


    @Override
    public void onCreate() {
        register(SanpotService.class);
        super.onCreate();

        if (!CacheUtil.getInstance().getBooleanCache(CacheKeys.ISLOGIN, false)) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            ActivityManager.getInstance().finishAllActivity();
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        setForeground();

        return START_STICKY;
    }

    @Override
    protected void onMainServiceConnected() {


        //设置机器人的表情
        final SystemManager systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
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
                public boolean onRecognizeResult(final Grammar grammar) {
                    //当返回值为true时，表示机器人不再对该文字做后续响应，false反之

                    if (!CacheUtil.getInstance().getBooleanCache(CacheKeys.ISLOGIN, false)) {
                        return false;
                    }


                    Log.e("csl", "=======机器人识别的文字=====" + grammar.getText());


                    final List<Room> roomList = LocalDataApi.getAllRooms(FamilyManager.getCurrentFamilyId());

                    final List<Device> deviceList = LocalDataApi.getDevicesByFamily(FamilyManager.getCurrentFamilyId());

                    for (Device item : deviceList) {
                        Log.e("csl", "--所有本地设备信息-->" + new Gson().toJson(item));

                        Log.e("csl", "=============>" + ProductManager.isRFSonDevice(item));
                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (final Device item : deviceList) {

                                //todo 每次增加控制设备 在这里需要添加对应的设备号
                                if (item.getDeviceType() != 0
                                        && item.getDeviceType() != 1
                                        && item.getDeviceType() != 19
                                        && item.getDeviceType() != 38
                                        && item.getDeviceType() != 2
                                        && item.getDeviceType() != 43
                                        && item.getDeviceType() != 29
                                        && item.getDeviceType() != 6
                                        && item.getDeviceType() != 5
                                        && item.getDeviceType() != 3
                                        && item.getDeviceType() != 4
                                        && item.getDeviceType() != 8
                                        && item.getDeviceType() != 34
                                        && item.getDeviceType() != 35
                                        && item.getDeviceType() != 42
                                        && item.getDeviceType() != 72

                                        ) {
                                    continue;
                                }


                                try {

                                    String meansText = grammar.getText();
                                    if (meansText.contains("text")) {
                                        meansText = new JSONObject(grammar.getText()).getString("text");
                                    }


                                    String deviceTypeName = "";

                                    switch (item.getDeviceType()) {
                                        case 5://空调
                                            deviceTypeName = "空调";

                                            if (meansText.contains("温度")) {
                                                deviceTypeName = "温度";
                                            }

                                            break;
                                        case 6://电视
                                            deviceTypeName = "电视";
                                            break;

                                    }

                                    if (!deviceTypeName.isEmpty()) {

                                        if (meansText.contains(deviceTypeName)
                                                &&
                                                (meansText.contains(openOrder) || meansText.contains(closeOrder)
                                                        || meansText.contains(highState) || meansText.contains(lowerState))
                                                ) {


                                            //获取当前设备的房间名称
                                            String roomName = "";
                                            for (Room room : roomList) {
                                                if (room.getRoomId() == item.getRoomId()) {
                                                    roomName = room.getRoomName();
                                                    break;
                                                }
                                            }


                                            if (meansText.contains(openOrder)) {
                                                meansText = openOrder + deviceTypeName;
                                            } else if (meansText.contains(closeOrder)) {
                                                meansText = closeOrder + deviceTypeName;
                                            } else if (meansText.contains(highState)) {
                                                meansText = highState + deviceTypeName;
                                            } else if (meansText.contains(lowerState)) {
                                                meansText = lowerState + deviceTypeName;
                                            }


                                            if (!TextUtils.isEmpty(roomName) && meansText.contains(roomName)) {
                                                meansText = roomName + roomName;
                                            }


                                            if (!TextUtils.isEmpty(roomName) && item.getDeviceName().contains(roomName)) {
                                                item.setDeviceName(item.getRoomName() + deviceTypeName);
                                            } else {
                                                item.setDeviceName(deviceTypeName);
                                            }


                                        }

                                    }


                                    //进行语义识别
                                    JSONObject jsonObject = BdSdkUtils.getInstance().simnet(meansText, item);
                                    Log.e("csl", "--------语义相识度------>" + jsonObject.toString());

                                    //如果超过百度每秒的识别限制则直接退出
                                    if (jsonObject.toString().contains("error_code")
                                            &&
                                            (jsonObject.getInt("error_code") == 17
                                                    || jsonObject.getInt("error_code") == 18
                                                    || jsonObject.getInt("error_code") == 19)
                                            ) {
                                        systemManager.showEmotion(EmotionsType.QUESTION);
                                        startSpeak(speechManager, GrammerData.orderError);
                                        break;
                                    }


                                    Thread.sleep(200);

                                    if (jsonObject.getDouble("score") >= 0.55) {

                                        DeviceControHelper deviceControHelper = new DeviceControHelper(item);

                                        if (meansText.contains(openOrder) || meansText.contains(closeOrder)) {
                                            startSpeak(speechManager, GrammerData.orderDO[new Random().nextInt(GrammerData.orderDO.length)]);

                                            deviceControHelper.deviceSwitch(CacheUtil.getInstance().getStringCache(CacheKeys.LOGINACCOUNT, ""), meansText.contains(openOrder) ? true : false, new BaseResultListener() {
                                                @Override
                                                public void onResultReturn(BaseEvent baseEvent) {


                                                }
                                            });

                                        } else if (meansText.contains(highState) || meansText.contains(lowerState)) {
                                            startSpeak(speechManager, GrammerData.orderDO[new Random().nextInt(GrammerData.orderDO.length)]);

                                            deviceControHelper.tempDeviceControl(false, meansText.contains(highState) ? true : false, new BaseResultListener() {
                                                @Override
                                                public void onResultReturn(BaseEvent baseEvent) {
                                                }
                                            });

                                        } else {
                                            startSpeak(speechManager, GrammerData.orderError);
                                        }


                                        break;
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }


                        }
                    }).start();


                    if (grammar.getText().contains(openOrder)
                            || grammar.getText().contains(closeOrder)
                            || grammar.getText().contains(highState)
                            || grammar.getText().contains(lowerState)) {
                        return true;
                    }


                    return false;
                }

                @Override
                public void onError(int i, int i1) {

                }
            });


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= 24) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
        } else {
            stopForeground(true);
        }

    }

    private void startSpeak(SpeechManager speechManager, String text) {
        //开始欢迎语的语音合成
        final SpeakOption speakOption = new SpeakOption();
        speakOption.setLanguageType(SpeakOption.LAG_CHINESE);
        speechManager.startSpeak(text, speakOption);
    }


}
