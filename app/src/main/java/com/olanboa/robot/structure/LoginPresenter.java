package com.olanboa.robot.structure;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.olanboa.robot.activity.FamilyActivity;
import com.olanboa.robot.activity.LoginActivity;
import com.olanboa.robot.base.BasePresenter;
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
        getContext().startActivity(new Intent(getContext(), FamilyActivity.class));

        ActivityManager.getInstance().finishActivity(LoginActivity.class.getName());
    }


    public void doLogin(final String name, final String pass) {

        if (getView().checkLoginInfo()) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("账号登录中,请稍后");
        progressDialog.show();

        getModel().doLogin(name, pass, new BaseResultListener() {
            @Override
            public void onResultReturn(BaseEvent baseEvent) {
                if (baseEvent.isSuccess()) {
                    showToastInfo("登录成功");
                    startService();
                    CacheUtil.getInstance().saveLoginInfo(name, pass);

                } else {
                    showToastInfo("登录失败,错误码:" + baseEvent.getResult());
                }

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
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
