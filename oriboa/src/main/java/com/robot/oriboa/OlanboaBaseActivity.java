package com.robot.oriboa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class OlanboaBaseActivity extends AppCompatActivity {

    protected abstract int setLayoutRes();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutRes());
        init(savedInstanceState);
    }
}
