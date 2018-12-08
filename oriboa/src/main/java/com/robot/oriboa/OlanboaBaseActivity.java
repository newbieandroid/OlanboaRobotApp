package com.robot.oriboa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class OlanboaBaseActivity extends AppCompatActivity {


    private ToolbarOption toolbarOption;

    protected abstract int setLayoutRes();

    protected abstract ToolbarOption setOptions();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutRes());
        init(savedInstanceState);
        initToolbar();
    }


    private void initToolbar() {

        toolbarOption = setOptions();

        if (!toolbarOption.isHasNormalToolbar()) {
            return;
        }

        RelativeLayout olanboHeadView = findViewById(R.id.olanboHeadView);

        if (toolbarOption.getToolBarMainRes() > 0) {
            olanboHeadView.setBackgroundResource(toolbarOption.getToolBarMainRes());
        }

        ImageView olanboa_head_back = findViewById(R.id.olanboa_head_back);

        if (toolbarOption.getToolBarBackRes() > 0) {
            olanboa_head_back.setBackgroundResource(toolbarOption.getToolBarBackRes());
        }


        if (toolbarOption.getToolBarBackListener() != null) {
            olanboa_head_back.setOnClickListener(toolbarOption.getToolBarBackListener());
        }


        TextView olanboa_head_title = findViewById(R.id.olanboa_head_title);

        if (!TextUtils.isEmpty(toolbarOption.getTitleString())) {
            olanboa_head_title.setText(toolbarOption.getTitleString());
        }

        ImageView olanboa_head_menu = findViewById(R.id.olanboa_head_menu);

        if (toolbarOption.getToolBarMenuRes() > 0) {
            olanboa_head_menu.setBackgroundResource(toolbarOption.getToolBarMenuRes());
        }

        if (toolbarOption.getToolBarMenuListener() != null) {
            olanboa_head_menu.setOnClickListener(toolbarOption.getToolBarMenuListener());
        }

    }


}
