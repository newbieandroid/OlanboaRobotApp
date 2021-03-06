package com.olanboa.robot.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.olanboa.robot.R;
import com.olanboa.robot.datas.CacheKeys;
import com.olanboa.robot.listener.FamilySwitchListener;
import com.olanboa.robot.listener.GetFamilyListListener;
import com.olanboa.robot.structure.FamilyPresenter;
import com.olanboa.robot.structure.FamilyView;
import com.olanboa.robot.transform.CircleTransform;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.api.LocalDataApi;
import com.orvibo.homemate.api.UserApi;
import com.orvibo.homemate.bo.Device;
import com.orvibo.homemate.bo.Family;
import com.orvibo.homemate.model.family.FamilyManager;
import com.sanbot.opensdk.base.BindBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.unit.SpeechManager;

import java.util.List;


public class FamilyActivity extends BindBaseActivity implements FamilyView {


    private boolean isOnclickEnable = true;


    private RecyclerView familyListView;

    private FamilyPresenter familyPresenter;

    private BaseQuickAdapter<Family, BaseViewHolder> adapter;

    private Button exitLoginActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        register(FamilyActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.familyactivitylayout);


        final SpeechManager speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);


        exitLoginActivity = findViewById(R.id.exitLoginActivity);

        exitLoginActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                UserApi.logout(CacheUtil.getInstance().getStringCache(CacheKeys.LOGINACCOUNT, ""));
                CacheUtil.getInstance().clearLoginInfo();
                startActivity(new Intent(FamilyActivity.this, LoginActivity.class));
                finish();

            }
        });


        familyPresenter = new FamilyPresenter(this, this);

        initRecyleView();

        final CircleTransform circleTransform = new CircleTransform(getContext());


        familyPresenter.queryFamilyList(new GetFamilyListListener() {
            @Override
            public void onResult(boolean hasDatas, final List<Family> familyList, final String msg) {

                if (hasDatas) {
                    familyListView.setAdapter(adapter = new BaseQuickAdapter<Family, BaseViewHolder>(R.layout.item_family, familyList) {
                        @Override
                        protected void convert(BaseViewHolder helper, Family item) {
                            helper.setText(R.id.familyItemeName, item.getFamilyName());


                            Glide.with(FamilyActivity.this).load(item.getPic())
//                                    .thumbnail(transforms)
                                    .error(R.drawable.ic_launcher)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(circleTransform).into((ImageView) helper.itemView.findViewById(R.id.familyItemIcon));


                            if (FamilyManager.getCurrentFamilyId().equals(item.getFamilyId())) {
                                helper.setImageDrawable(R.id.familyItemStateIcon, getResources().getDrawable(R.drawable.select));
                            } else {
                                helper.setImageDrawable(R.id.familyItemStateIcon, getResources().getDrawable(R.drawable.select_default));
                            }


                            helper.addOnClickListener(R.id.familyItemStateIcon);

                        }

                    });

                    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(final BaseQuickAdapter adapter, View view, int position) {

                            if (!isOnclickEnable) {
                                return;
                            }

                            isOnclickEnable = false;


                            switch (view.getId()) {
                                case R.id.familyItemStateIcon:


                                    final Family family = ((Family) adapter.getData().get(position));


                                    if (family.getFamilyId().equals(FamilyManager.getCurrentFamilyId())) {
                                        isOnclickEnable = true;
                                        return;
                                    }

                                    familyPresenter.switchFamily(family.getFamilyId(), new FamilySwitchListener() {

                                        @Override
                                        public void isSwitchOk(boolean state) {


                                            List<Device> devices = LocalDataApi.getDevicesByFamily(family.getFamilyId());

                                            for (Device item : devices) {
                                                Log.e("csl", family.getFamilyName() + "==设备==>" + new Gson().toJson(item));
                                            }


                                            isOnclickEnable = true;
                                            if (state) {
                                                adapter.notifyDataSetChanged();

                                                speechManager.startSpeak("已切换到" + family.getFamilyName());
                                            }
                                        }
                                    });

                                    break;
                            }

                        }
                    });


                } else {
                    speechManager.startSpeak(msg);
                }

            }
        });


    }


    @Override
    public void initRecyleView() {


        familyListView = findViewById(R.id.familyList);

        familyListView.setLayoutManager(new LinearLayoutManager(this));

        familyListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                if (parent.getChildAdapterPosition(view) % 2 == 1) {
                    outRect.set(0, 15, 0, 0);
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }

            }
        });

    }

    @Override
    protected void onMainServiceConnected() {

    }
}
