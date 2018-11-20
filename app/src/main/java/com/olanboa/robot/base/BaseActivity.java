package com.olanboa.robot.base;

import android.os.Bundle;

import com.sanbot.opensdk.base.TopBaseActivity;

public abstract class BaseActivity extends TopBaseActivity {

    protected abstract int setLayoutRes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(BaseActivity.class);
        super.onCreate(savedInstanceState);
        setBodyView(setLayoutRes());
    }

}
