package com.robot.oriboa.helper;

import com.orvibo.homemate.api.DeviceControlApi;
import com.orvibo.homemate.api.LocalDataApi;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.api.listener.EventDataListener;
import com.orvibo.homemate.bo.Device;
import com.orvibo.homemate.bo.DeviceStatus;
import com.orvibo.homemate.dao.DeviceStatusDao;
import com.orvibo.homemate.event.BaseEvent;
import com.orvibo.homemate.model.family.FamilyManager;

import java.util.List;

/**
 * 欧瑞博设备的控制
 */
public class DeviceControHelper {


    private Device controlDevice;

    public DeviceControHelper(Device controlDevice) {
        this.controlDevice = controlDevice;
    }


    /*打开或者关闭设备*/
    public void deviceSwitch(boolean isOpen, BaseResultListener listener) {


        if (isOpen) {
            DeviceControlApi.deviceOpen(controlDevice.getUid(), controlDevice.getDeviceId(), 100, listener);
        } else {
            DeviceControlApi.deviceClose(controlDevice.getUid(), controlDevice.getDeviceId(), 100, listener);
        }
    }


    /*识别设备*/
    public void knowDevice(BaseResultListener listener) {
        DeviceControlApi.identify(controlDevice.getUid(), controlDevice.getDeviceId(), listener);
    }
}
