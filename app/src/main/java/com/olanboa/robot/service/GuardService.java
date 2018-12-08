package com.olanboa.robot.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.olanboa.robot.ProcessConnection;

public class GuardService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, new Notification());
        //绑定建立链接
        bindService(new Intent(this, SanpotService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("test", "GuardService:建立链接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            startService(new Intent(GuardService.this, SanpotService.class));
            //重新绑定
            bindService(new Intent(GuardService.this, SanpotService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };


}
