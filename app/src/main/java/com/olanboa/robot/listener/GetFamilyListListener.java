package com.olanboa.robot.listener;

import com.orvibo.homemate.bo.Family;

import java.util.List;

public interface GetFamilyListListener {


    void onResult(boolean hasDatas, List<Family> familyList, String msg);

}
