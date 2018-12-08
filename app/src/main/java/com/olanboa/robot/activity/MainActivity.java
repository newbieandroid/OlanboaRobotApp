package com.olanboa.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.olanboa.robot.R;
import com.olanboa.robot.datas.CacheKeys;
import com.olanboa.robot.service.SanpotService;
import com.olanboa.robot.util.CacheUtil;
import com.robot.oriboa.device.AddDeviceTypeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!CacheUtil.getInstance().getBooleanCache(CacheKeys.ISLOGIN, false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        } else {

            startService(new Intent(this, SanpotService.class));

            findViewById(R.id.scanDeiceBtn).setOnClickListener(this);
            findViewById(R.id.testBtn).setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.scanDeiceBtn:
                startActivity(new Intent(this, AddDeviceTypeActivity.class));
                break;
            case R.id.testBtn:
                DrawerLayout mainSlidingMenLayout = findViewById(R.id.mainSlidingMenLayout);
                mainSlidingMenLayout.openDrawer(Gravity.START, true);
                break;
        }
    }


}
