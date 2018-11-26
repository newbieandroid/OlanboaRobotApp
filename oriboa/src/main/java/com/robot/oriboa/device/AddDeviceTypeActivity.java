package com.robot.oriboa.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.robot.oriboa.R;
import com.robot.oriboa.ScannerActivity;

import java.util.ArrayList;
import java.util.List;

public class AddDeviceTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQ2SCAN = 123;

    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private List<String> datas = new ArrayList<>();

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddevicetypeactivity);

        findViewById(R.id.addDeviceTypeScanBtn).setOnClickListener(this);
        findViewById(R.id.addDeviceTypeScanBtn).setOnClickListener(this);


        RecyclerView addDeviceTypeList = findViewById(R.id.addDeviceTypeList);
        addDeviceTypeList.setLayoutManager(new LinearLayoutManager(this));
        for (String item : getResources().getStringArray(R.array.deviceType)) {
            datas.add(item);
        }
        addDeviceTypeList.setAdapter(adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_devicetype, datas) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

                helper.setText(R.id.item_deviceType_typeName, item);

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (position) {
                    case 0:
                        startActivity(new Intent(AddDeviceTypeActivity.this, HostDeviceTypeActivity.class));
                        break;
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addDeviceTypeScanBtn) {
            ScannerActivity.start(AddDeviceTypeActivity.this, REQ2SCAN);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case REQ2SCAN:
                    Log.e("csl", "------扫码返回的结果---" + data.getStringExtra(ScannerActivity.SCANRESULT));
                    break;
            }

        }


    }
}
