package com.olanboa.robot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.olanboa.robot.R;
import com.olanboa.robot.structure.LoginModel;
import com.olanboa.robot.structure.LoginPresenter;
import com.olanboa.robot.structure.LoginView;
import com.olanboa.robot.util.RegUtils;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView {

    private EditText loginNameEt, loginPassEt;
    private Button loginBtn, regBtn;

    private LoginPresenter mainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginNameEt = findViewById(R.id.loginNameEt);
        loginPassEt = findViewById(R.id.loginPassEt);

        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.regBtn);

        mainPresenter = new LoginPresenter(this, this) {
            @Override
            protected LoginModel setModel() {
                return new LoginModel();
            }
        };

        loginBtn.setOnClickListener(this);
        regBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:
                mainPresenter.doLogin(loginNameEt.getText().toString(), loginPassEt.getText().toString());
                break;
            case R.id.regBtn:
                mainPresenter.reg(loginNameEt.getText().toString(), loginPassEt.getText().toString());
                break;
        }

    }

    @Override
    public void checkLoginInfo() {
        if (TextUtils.isEmpty(loginNameEt.getText())) {
            Toast.makeText(this, "请输入登录账号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(loginPassEt.getText())) {
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public boolean checkRegInfo() {

        if (TextUtils.isEmpty(loginNameEt.getText())
                || !RegUtils.isEmail(loginNameEt.getText().toString())) {
            Toast.makeText(this, "注册账号只能是邮箱", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(loginPassEt.getText())) {
            Toast.makeText(this, "请输入注册密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void regOk(String userName, String userPass) {
        loginNameEt.setText(userName);
        loginPassEt.setText(userPass);
    }
}
