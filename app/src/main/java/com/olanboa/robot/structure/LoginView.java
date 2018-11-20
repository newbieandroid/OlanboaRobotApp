package com.olanboa.robot.structure;

import com.olanboa.robot.base.BaseView;

public interface LoginView extends BaseView {

    void checkLoginInfo();

    boolean checkRegInfo();


    void regOk(String userName, String userPass);
}
