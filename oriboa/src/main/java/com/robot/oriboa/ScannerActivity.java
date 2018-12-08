package com.robot.oriboa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.orvibo.homemate.bo.QRCode;
import com.orvibo.homemate.dao.QRCodeDao;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScannerActivity extends AppCompatActivity implements QRCodeView.Delegate {


    //扫描欧瑞博设备的二维码格式
    private static final String QRRULE = "http://www.orvibo.com/software/365.html?id=";


    public static final String SCANRESULT = "scanResult";

    public static void start(Activity activity, int reqCode) {

        activity.startActivityForResult(new Intent(activity, ScannerActivity.class), reqCode);
    }


    private ZXingView mZXingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        mZXingView = findViewById(R.id.zxingview);
        mZXingView.setDelegate(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {


        //欧瑞博扫描后的识别见说明文档：http://wiki.orvibo.com:8190/pages/viewpage.action?pageId=19072404

        if (result.contains(QRRULE)) {

            //扫描的设备为欧瑞博正确的格式则返回到上一级页面
            Intent intent = new Intent();
            intent.putExtra(SCANRESULT, result.replace(QRRULE, ""));//把格式部分去掉拿到最后的结果
            setResult(RESULT_OK, intent);
            finish();
        } else {
            //如果扫描的格式不正确则提示信息然后继续扫描
            mZXingView.startSpot(); //拿到识别的结果后继续扫描
            Toast.makeText(this, "设备识别信息不正确,请重新扫描", Toast.LENGTH_SHORT).show();
        }


        Log.e("csl", "-------二维码识别结果-----" + result);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

        if (isDark) {
            mZXingView.openFlashlight();
        }

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

        Toast.makeText(this, "打开摄像头出错,请重试", Toast.LENGTH_SHORT).show();
        finish();

    }
}
