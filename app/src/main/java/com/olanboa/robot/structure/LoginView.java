package com.olanboa.robot.structure;

import android.widget.EditText;

import com.olanboa.robot.base.BaseView;

public interface LoginView extends BaseView {


    void checkLoginCache(EditText userNameEt,EditText passEt);


    void checkLoginInfo();

    boolean checkRegInfo();


    void regOk(String userName, String userPass);
}
