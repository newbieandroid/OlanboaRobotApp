package com.olanboa.robot.structure;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.olanboa.robot.base.BasePresenter;
import com.olanboa.robot.datas.CacheKeys;
import com.olanboa.robot.listener.FamilySwitchListener;
import com.olanboa.robot.listener.GetFamilyListListener;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.api.FamilyApi;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.event.BaseEvent;
import com.orvibo.homemate.event.family.QueryFamilyEvent;
import com.orvibo.homemate.sharedPreferences.UserCache;

public class FamilyPresenter extends BasePresenter<FamilyModel, FamilyView> {
    public FamilyPresenter(Context context, FamilyView familyView) {
        super(context, familyView);
    }

    @Override
    protected FamilyModel setModel() {
        return new FamilyModel();
    }


    /*切换家庭*/
    public void switchFamily(final String familyId, final FamilySwitchListener listener) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("正在切换家庭,请稍后");
        progressDialog.show();

        FamilyApi.switchFamily(CacheUtil.getInstance().getStringCache(CacheKeys.LOGINACCOUNT, ""),
                CacheUtil.getInstance().getStringCache(CacheKeys.LOGINPASS, ""),
                true,
                familyId,
                new BaseResultListener() {
                    @Override
                    public void onResultReturn(BaseEvent baseEvent) {

                        if (listener != null) {
                            listener.isSwitchOk(baseEvent.isSuccess());
                        }

                        progressDialog.dismiss();


                    }
                });
    }


    /*查询当前用户下的所有家庭*/
    public void queryFamilyList(final GetFamilyListListener listListener) {


        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.show();


        String userId = UserCache.getUserId(getContext(), CacheUtil.getInstance().getStringCache(CacheKeys.LOGINACCOUNT, ""));
        FamilyApi.queryFamilys(userId, new BaseResultListener.DataListener() {
            @Override
            public void onResultReturn(BaseEvent baseEvent, Object data) {

                if (baseEvent.getResult() == 0) {
                    QueryFamilyEvent queryEvent = (QueryFamilyEvent) baseEvent;
                    if (queryEvent.getFamilyList() != null) {

                        if (listListener != null) {
                            listListener.onResult(true, queryEvent.getFamilyList(), "获取成功");
                        }

                    } else {
                        if (listListener != null) {
                            listListener.onResult(false, null, "暂无家庭信息");
                        }
                    }
                } else {

                    if (listListener != null) {
                        listListener.onResult(false, null, "网络链接超时");
                    }
                }

                progressDialog.dismiss();

            }
        });
    }

}
