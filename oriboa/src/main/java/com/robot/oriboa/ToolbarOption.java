package com.robot.oriboa;

import android.app.Activity;
import android.view.View;

public class ToolbarOption {


    private boolean hasNormalToolbar = true;
    private Activity activity;

    public ToolbarOption(Activity activity) {
        this.activity = activity;
    }

    private int toolBarBackRes = R.mipmap.icon_olanboa_back;//左边按钮的图片资源

    private int toolBarMenuRes;//菜单键的图片资源

    private int toolBarMainRes;//背景色

    private String titleString; //主标题


    /*菜单键的点击事件*/
    private View.OnClickListener toolBarMenuListener;


    /*返回键的点击事件*/
    private View.OnClickListener toolBarBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.finish();
        }
    };

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getToolBarBackRes() {
        return toolBarBackRes;
    }

    public void setToolBarBackRes(int toolBarBackRes) {
        this.toolBarBackRes = toolBarBackRes;
    }

    public int getToolBarMainRes() {
        return toolBarMainRes;
    }

    public void setToolBarMainRes(int toolBarMainRes) {
        this.toolBarMainRes = toolBarMainRes;
    }

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    public View.OnClickListener getToolBarMenuListener() {
        return toolBarMenuListener;
    }

    public void setToolBarMenuListener(View.OnClickListener toolBarMenuListener) {
        this.toolBarMenuListener = toolBarMenuListener;
    }

    public View.OnClickListener getToolBarBackListener() {
        return toolBarBackListener;
    }

    public void setToolBarBackListener(View.OnClickListener toolBarBackListener) {
        this.toolBarBackListener = toolBarBackListener;
    }

    public boolean isHasNormalToolbar() {
        return hasNormalToolbar;
    }

    public void setHasNormalToolbar(boolean hasNormalToolbar) {
        this.hasNormalToolbar = hasNormalToolbar;
    }


    public int getToolBarMenuRes() {
        return toolBarMenuRes;
    }

    public void setToolBarMenuRes(int toolBarMenuRes) {
        this.toolBarMenuRes = toolBarMenuRes;
    }
}
