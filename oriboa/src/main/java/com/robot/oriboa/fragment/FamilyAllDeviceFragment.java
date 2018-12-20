package com.robot.oriboa.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orvibo.homemate.api.LocalDataApi;
import com.orvibo.homemate.bo.Device;
import com.orvibo.homemate.model.family.FamilyManager;
import com.robot.oriboa.R;

import java.util.ArrayList;
import java.util.List;

public class FamilyAllDeviceFragment extends Fragment {

    private List<Device> deviceList = new ArrayList<>();

    private BaseQuickAdapter<Device, BaseViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.familyalldevie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView familyAllDeviceList = view.findViewById(R.id.familyAllDeviceList);
        familyAllDeviceList.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));


        familyAllDeviceList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {


                if (parent.getChildAdapterPosition(view) % 2 == 1) {
                    outRect.set(10, 0, 0, 0);
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }

            }
        });


        familyAllDeviceList.setAdapter(adapter = new BaseQuickAdapter<Device, BaseViewHolder>(R.layout.item_familydevice, deviceList = LocalDataApi.getDevicesByFamily(FamilyManager.getCurrentFamilyId())) {
            @Override
            protected void convert(BaseViewHolder helper, Device item) {
                helper.setText(R.id.familyItemDeviceName, item.getDeviceName());
            }
        });

    }
}
