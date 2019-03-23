package com.olanboa.robot.listener;

import com.danale.video.sdk.platform.entity.Device;

public interface InitDanaleDeviceListener {


    void initOk(Device danaleDevice);

    void error();

}
