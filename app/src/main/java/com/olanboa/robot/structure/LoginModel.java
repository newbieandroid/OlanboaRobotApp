package com.olanboa.robot.structure;

import com.olanboa.robot.base.BaseModel;
import com.orvibo.homemate.api.UserApi;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.util.MD5;

public class LoginModel implements BaseModel {


    public void doLogin(String name, String pass, BaseResultListener listener) {
        UserApi.login(name, pass, true, listener);
    }

    public void doReg(String name, String pass, BaseResultListener.DataListener listener) {
        UserApi.registerByEmail(name, MD5.encryptMD5(pass), listener);
    }
}
