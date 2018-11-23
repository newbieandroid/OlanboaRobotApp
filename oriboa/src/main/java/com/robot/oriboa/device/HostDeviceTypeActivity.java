package com.robot.oriboa.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.robot.oriboa.R;

import java.util.ArrayList;
import java.util.List;

public class HostDeviceTypeActivity extends AppCompatActivity {

    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hostdevicetypeactivity);


        RecyclerView hostDeviceTypeList = findViewById(R.id.hostDeviceTypeList);
        hostDeviceTypeList.setLayoutManager(new LinearLayoutManager(this));


        List<String> datas = new ArrayList<>();
        for (String item : getResources().getStringArray(R.array.hostType)) {
            datas.add(item);
        }

        hostDeviceTypeList.setAdapter(adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_devicetype, datas) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

                helper.setText(R.id.item_deviceType_typeName, item);

            }
        });

    }
}
