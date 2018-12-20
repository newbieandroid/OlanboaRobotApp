package com.olanboa.robot.structure;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.olanboa.robot.base.BasePresenter;
import com.olanboa.robot.datas.CacheKeys;
import com.olanboa.robot.listener.GetFamilyListListener;
import com.olanboa.robot.util.CacheUtil;
import com.orvibo.homemate.api.FamilyApi;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.event.BaseEvent;
import com.orvibo.homemate.event.family.QueryFamilyEvent;
import com.orvibo.homemate.model.family.FamilyManager;
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
    public void switchFamily(final String familyId) {

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

                        String userId = UserCache.getUserId(getContext(), CacheUtil.getInstance().getStringCache(CacheKeys.LOGINACCOUNT, ""));
                        FamilyManager.saveFamilyId(userId, familyId);
                        FamilyManager.saveCurrentFamilyId(familyId);

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
                            listListener.onResult(true, queryEvent.getFamilyList());
                        }

                        Log.e("csl", "--房间信息-->" + new Gson().toJson(queryEvent.getFamilyList()));


                    } else {
                        if (listListener != null) {
                            listListener.onResult(false, null);
                        }
                    }
                } else {

                    if (listListener != null) {
                        listListener.onResult(false, null);
                    }
                }

                progressDialog.dismiss();

            }
        });
    }

}
