package com.robot.oriboa.helper;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.AcIr;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.orvibo.homemate.api.DeviceControlApi;
import com.orvibo.homemate.api.listener.BaseResultListener;
import com.orvibo.homemate.bo.Device;
import com.orvibo.homemate.event.BaseEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * 欧瑞博设备的控制
 */
public class DeviceControHelper {

//设备类型:
// 0：调光灯、
// 1：普通灯光、
// 2：插座、
// 3：幕布、
// 4：百叶窗、
// 5：空调、
// 6：电视；
// 7：音箱；
// 8：对开窗帘
// 10：开关型继电器
// 11：红外转发器；
// 14：摄像头；
// 15：情景面板；
// 16：遥控器；
// 17：中继器；
// 18：亮度传感器;
// 19：RGB灯；
// 21:门锁;
// 22:温度传感器；
// 23：湿度传感器;
// 24:空气质量传感器;
// 25:可燃气体传感器;
// 26:红外人体传感器;
// 27:烟雾传感器;
// 28:报警设备；
// 29：S20；
// 30：Allone
// 32：机顶盒；
// 33：自定义红外；
// 34：对开窗帘（支持按照百分比控制）；
// 35：卷帘（支持按照百分比控制）；
// 36：空调面板；
// 37：推窗器；
// 38：色温灯；
// 39：卷闸门；
// 41：推窗器(已不用)；
// 42：卷帘（无百分比）；
// 43：单控排插；
// 44：vicenter300主机；
// 45：miniHub；
// 46：门磁；
// 47：窗磁；
// 48：抽屉磁；
// 49：其他类型的门窗磁；
// 50：情景面板（5键）；
// 51：情景面板（7键）；
// 52：晾衣架；
// 54：水浸传探测器；
// 55：一氧化碳报警器；
// 56：紧急按钮
// 57：背景音乐；
// 58：电风扇；
// 59：电视盒子；
// 61：情景面板（1键）；
// 62：情景面板（2键）；
// 63：情景面板（4键）；
// 64：智能配电箱；
// 65：甲醛探测仪 ；
// 66：一氧化碳探测仪；
// 67：RF主机(Allone Pro)；
// 68：开合帘电机；
// 69：开窗电机；
// 71：百叶帘电机；
// 72：卷帘（支持百分比，支持子类）；
// 77：RF开关(状态码)；
// 78：RF开关(翻转码)；
// 81：VRV中央空调控制器；


    private Device controlDevice;

    public DeviceControHelper(Device controlDevice) {
        this.controlDevice = controlDevice;
    }


    /*打开或者关闭设备*/
    public void deviceSwitch(boolean isOpen, BaseResultListener listener) {


        switch (controlDevice.getDeviceType()) {

            case 0: //能够直接控制开关状态的设备
            case 1:
            case 2:
            case 19:
                if (isOpen) {
                    DeviceControlApi.deviceOpen(controlDevice.getUid(), controlDevice.getDeviceId(), 100, listener);
                } else {
                    DeviceControlApi.deviceClose(controlDevice.getUid(), controlDevice.getDeviceId(), 100, listener);
                }
                break;
            case 6://电视控制

                KookongSDK.getIRDataById(controlDevice.getIrDeviceId(), com.hzy.tvmao.ir.Device.TV, "", new IRequestResult<IrDataList>() {

                    @Override
                    public void onSuccess(String s, IrDataList irDataList) {

                        Log.e("csl", "------电视获取码库成功----->" + new Gson().toJson(irDataList));

                        IrData irData = irDataList.getIrDataList().get(0);

                        ArrayList<IrData.IrKey> keyArrayList = irData.keys;

                        for (IrData.IrKey item : keyArrayList) {
                            if (item.fkey.equals("电源")) {
                                DeviceControlApi.allOneControl(controlDevice.getUid(),
                                        controlDevice.getDeviceId(),
                                        irData.fre,
                                        item.pulse.split(",").length,
                                        item.pulse,
                                        false, new BaseResultListener() {
                                            @Override
                                            public void onResultReturn(BaseEvent baseEvent) {
                                                Log.e("csl", "---电视开关F操作结果------->" + baseEvent.isSuccess());


                                            }
                                        });

                                break;
                            }
                        }


                        //取得SD卡根目录
                        try {
                            File file = new File(Environment.getExternalStorageDirectory(), "aaa.txt");
                            if (!file.exists()) {
                                file.createNewFile();
                            }

                            OutputStreamWriter outputStreamWriter = null;

                            BufferedWriter out = new BufferedWriter(outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(), true)));
                            out.newLine();
                            out.write(new Gson().toJson(irDataList));

                            outputStreamWriter.close();
                            out.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFail(Integer integer, String s) {
                        Log.e("csl", "------电视失败----->" + integer);
                    }
                });


                break;
            case 5: //空调控制

                String value = "off";
                if (isOpen) {
                    value = "on";
                }


                KookongSDK.getACIrByCmd(controlDevice.getIrDeviceId(), "power", value, 1, controlDevice.getDeviceId(), new IRequestResult<AcIr>() {

                    @Override
                    public void onSuccess(String s, final AcIr acIr) {
                        Log.e("csl", "------空调操作成功------->" + new Gson().toJson(acIr));


                        DeviceControlApi.allOneControl(controlDevice.getUid(),
                                controlDevice.getDeviceId(),
                                acIr.frequency,
                                acIr.pulse.split(",").length,
                                acIr.pulse,
                                false, new BaseResultListener() {
                                    @Override
                                    public void onResultReturn(BaseEvent baseEvent) {
                                        Log.e("csl", "---空调操作结果------->" + baseEvent.isSuccess());


                                    }
                                });


//                        KookongSDK.getIRDataById("" + acIr.rid, 5, "", new IRequestResult<IrDataList>() {
//                            public void onSuccess(String var1, IrDataList irDataList) {
//
//
//                                IrData irData = irDataList.getIrDataList().get(0);
//                                final KKACManagerV2 kkacManagerV2 = new KKACManagerV2();
//                                kkacManagerV2.initIRData(irData.rid, irData.exts, null);
//
//
//                                KookongSDK.getDefaultACStatus(irData.rid + "", new IRequestResult<ACStateV2>() {
//                                    @Override
//                                    public void onSuccess(String var1, ACStateV2 acStateV2) {
//
//
//                                        Log.e("csl", "-------成功0------>" + acStateV2.getCurPowerState());
//
//                                        String acState = "";
//                                        if (acStateV2 != null) {
//                                            acState = Json.get().toJson(acStateV2);
//                                        }
//                                        kkacManagerV2.setACStateV2FromString(acState);
//
////                                        kkacManagerV2.changePowerState();
//
//
//
//
//                                    }
//
//                                    @Override
//                                    public void onFail(Integer var1, String var2) {
//
//
//                                        Log.e("csl", "-------失败------>");
//
//
//                                    }
//                                });
//
//
//                            }
//
//                            public void onFail(Integer var1, String var2) {
//                            }
//                        });


                    }

                    @Override
                    public void onFail(Integer integer, String s) {

                        Log.e("csl", "------空调操作失败----" + s + "--->" + integer);

                    }
                });
                break;
        }


    }


}
