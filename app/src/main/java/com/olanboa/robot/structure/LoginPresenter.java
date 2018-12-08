package com.olanboa.robot.structure;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.olanboa.robot.activity.MainActivity;
import com.olanboa.robot.base.BasePresenter;
import com.olanboa.robot.datas.CacheKeys;
import com.olanboa.robot.service.GuardService;
import com.olanboa.robot.service.SanpotService;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.event.BaseEvent;
import com.orvibo.homemate.util.ActivityManager;

public abstract class LoginPresenter extends BasePresenter<LoginModel, LoginView> {
    public LoginPresenter(Context context, LoginView mainView) {
        super(context, mainView);
    }


    public void startService() {
        getContext().startService(new Intent(getContext(), SanpotService.class));
        getContext().startService(new Intent(getContext(), GuardService.class));
        ActivityManager.getInstance().finishAllActivity();
    }


    public void doLogin(String name, String pass) {

        getView().checkLoginInfo();

        getModel().doLogin(name, pass, new BaseResultListener() {
            @Override
            public void onResultReturn(BaseEvent baseEvent) {

                if (baseEvent.isSuccess()) {
                    showToastInfo("登录成功");
                    startService();
                    CacheUtil.getInstance().savaBooleanCache(CacheKeys.ISLOGIN, true);


                } else {
                    showToastInfo("登录失败,错误码:" + baseEvent.getResult());
                }

            }
        });

    }


    public void reg(final String name, final String pass) {
        if (getView().checkRegInfo()) {
            getModel().doReg(name, pass, new BaseResultListener.DataListener() {
                @Override
                public void onResultReturn(BaseEvent baseEvent, Object o) {

                    if (baseEvent.isSuccess()) {
                        showToastInfo("注册成功");
                        getView().regOk(name, pass);
                        doLogin(name, pass);
                    } else {
                        showToastInfo("注册失败,错误码:" + baseEvent.getResult());
                    }

                }
            });
        }
    }

}
