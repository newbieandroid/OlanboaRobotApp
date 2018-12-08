package com.olanboa.robot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.olanboa.robot.service.GuardService;
import com.olanboa.robot.service.SanpotService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_BOOT)) {

            Log.e("csl", "----收到开机启动广播----------");

            context.startService(new Intent(context, SanpotService.class));
            context.startService(new Intent(context, GuardService.class));
        }

    }
}
