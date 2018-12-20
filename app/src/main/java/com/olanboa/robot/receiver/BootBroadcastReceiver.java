package com.olanboa.robot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.olanboa.robot.service.SanpotService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_BOOT)) {
            context.startService(new Intent(context, SanpotService.class));
        }

    }
}
