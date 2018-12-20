package com.olanboa.robot.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.olanboa.robot.R;
import com.olanboa.robot.listener.GetFamilyListListener;
import com.olanboa.robot.structure.FamilyPresenter;
import com.olanboa.robot.structure.FamilyView;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.bo.Family;
import com.orvibo.homemate.model.family.FamilyManager;

import java.util.List;


public class FamilyActivity extends AppCompatActivity implements FamilyView {

    private RecyclerView familyListView;

    private FamilyPresenter familyPresenter;

    private BaseQuickAdapter<Family, BaseViewHolder> adapter;

    private Button exitLoginActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.familyactivitylayout);

        exitLoginActivity = findViewById(R.id.exitLoginActivity);

        exitLoginActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CacheUtil.getInstance().clearLoginInfo();

                startActivity(new Intent(FamilyActivity.this, LoginActivity.class));
                finish();

            }
        });


        familyPresenter = new FamilyPresenter(this, this);

        initRecyleView();

        familyPresenter.queryFamilyList(new GetFamilyListListener() {
            @Override
            public void onResult(boolean hasDatas, final List<Family> familyList) {

                if (hasDatas) {
                    familyListView.setAdapter(adapter = new BaseQuickAdapter<Family, BaseViewHolder>(R.layout.item_familydevice, familyList) {
                        @Override
                        protected void convert(BaseViewHolder helper, Family item) {
                            helper.setText(R.id.familyItemDeviceName, item.getFamilyName());

                            RequestManager.DefaultOptions defaultOptions = new RequestManager.DefaultOptions() {
                                @Override
                                public <T> void apply(GenericRequestBuilder<T, ?, ?, ?> genericRequestBuilder) {

                                }
                            };

                            Glide.with(FamilyActivity.this).load(item.getPic()).error(R.mipmap.ic_launcher_round).into((ImageView) helper.itemView.findViewById(R.id.familyItemDeviceTypeIcon));
                        }

                    });
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                            final String switchFamilyId = ((Family) adapter.getData().get(position)).getFamilyId();

                            if (FamilyManager.getCurrentFamilyId().equals(switchFamilyId)) {
                                return;
                            }

                            familyPresenter.switchFamily(switchFamilyId);
                        }
                    });


                } else {

                    Toast.makeText(FamilyActivity.this, "获取家庭信息失败", Toast.LENGTH_SHORT).show();

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
}
