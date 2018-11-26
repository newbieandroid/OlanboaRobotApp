package com.robot.oriboa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScannerActivity extends AppCompatActivity implements QRCodeView.Delegate {


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
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(200);
//        mZXingView.startSpot(); //拿到识别的结果后继续扫描


        Intent intent = new Intent();
        intent.putExtra(SCANRESULT, result);
        setResult(RESULT_OK, intent);
        finish();

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
