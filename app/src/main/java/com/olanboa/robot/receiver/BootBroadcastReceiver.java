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


//            String userName = CacheUtil.getInstance().getStringCache(CacheKeys.LOGINACCOUNT, "");
//
//            String userPass = CacheUtil.getInstance().getStringCache(CacheKeys.LOGINPASS, "");


//            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPass)) {
//
//                UserApi.login(userName, userPass, true, new BaseResultListener() {
//                    @Override
//                    public void onResultReturn(BaseEvent baseEvent) {
//
//                        if (baseEvent.isSuccess()) {
                            context.startService(new Intent(context, SanpotService.class));
//                        }
//
//                    }
//                });
//
//            }

        }

    }
}
