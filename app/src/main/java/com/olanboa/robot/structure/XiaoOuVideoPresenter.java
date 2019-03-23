package com.olanboa.robot.structure;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.view.SurfaceView;

import com.danale.video.sdk.Danale;
import com.danale.video.sdk.device.entity.Connection;
import com.danale.video.sdk.device.handler.DeviceResultHandler;
import com.danale.video.sdk.device.result.DeviceResult;
import com.danale.video.sdk.http.exception.HttpException;
import com.danale.video.sdk.platform.base.PlatformResult;
import com.danale.video.sdk.platform.constant.DeviceType;
import com.danale.video.sdk.platform.constant.GetType;
import com.danale.video.sdk.platform.constant.OnlineType;
import com.danale.video.sdk.platform.handler.PlatformResultHandler;
import com.danale.video.sdk.platform.result.GetDeviceListResult;
import com.danale.video.sdk.player.DanalePlayer;
import com.danale.video.view.opengl.DanaleGlSurfaceView;
import com.olanboa.robot.R;
import com.olanboa.robot.base.BasePresenter;
import com.olanboa.robot.listener.InitDanaleDeviceListener;
import com.orvibo.homemate.bo.Device;
import com.orvibo.homemate.camera.danale.DanaleLoginBiz;
import com.orvibo.homemate.camera.danale.LoginDanaleCallBack;
import com.sanbot.opensdk.function.unit.SpeechManager;

public class XiaoOuVideoPresenter extends BasePresenter<XiaoOuVideoModel, XiaoOuVideoView> implements DanalePlayer.OnPlayerStateChangeListener {


    private SpeechManager speechManager;


    private Connection connection;
    private DanalePlayer danalePlayer;

    private AudioTrack audioTrack;
    private AudioRecord audioRecord;


    private boolean isVideoOpened;
    private boolean isAudioOpened;
    private boolean isTalkOpened;
    private int channel = 1;
    private int videoQuality = 30;


    private com.danale.video.sdk.platform.entity.Device danaleDevice;

    public XiaoOuVideoPresenter(Context context, XiaoOuVideoView xiaoOuVideoView, SpeechManager speechManager) {
        super(context, xiaoOuVideoView);
        this.speechManager = speechManager;
    }

    @Override
    protected XiaoOuVideoModel setModel() {
        return new XiaoOuVideoModel();
    }

    private void initDannleDevice(final Device oDevice, final InitDanaleDeviceListener initDanaleDeviceListener) {


        if (Danale.getSession() == null) {
            //未登录到第三方服务器，需要登录
            DanaleLoginBiz danaleLoginBiz = new DanaleLoginBiz(new LoginDanaleCallBack() {
                @Override
                public void getTokenFail() {
                    //登录第三方服务器失败的处理

                    if (initDanaleDeviceListener != null) {
                        initDanaleDeviceListener.error();
                    }

                }

                @Override
                public void loginDanaleSuccess() {
                    getDanaleDeviceList(oDevice, initDanaleDeviceListener);
                }

                @Override
                public void loginDanaleFail() {
                    //登录第三方服务器失败的处理

                    if (initDanaleDeviceListener != null) {
                        initDanaleDeviceListener.error();
                    }
                }

                @Override
                public void loginCountOut() {
                    //登录第三方服务器失败的处理

                    if (initDanaleDeviceListener != null) {
                        initDanaleDeviceListener.error();
                    }
                }
            }, getContext());
            danaleLoginBiz.getDanaleAccessToken();
        } else {
            getDanaleDeviceList(oDevice, initDanaleDeviceListener);
        }
    }


    private void getDanaleDeviceList(final Device oDevice, final InitDanaleDeviceListener initDanaleDeviceListener) {
        Danale.getSession().getDeviceList(0, GetType.MINE, 1, 30, new PlatformResultHandler() {
            @Override
            public void onSuccess(PlatformResult platformResult) {
                // “获取设备列表”成功的处理
                GetDeviceListResult ret = (GetDeviceListResult) platformResult;
                if (ret != null && ret.getDeviceList() != null && ret.getDeviceList().size() > 0) {
                    //由于添加成功后没有返回设备，所以直接先拿第一条数据来用，以后要把下面的代码释放
                    for (com.danale.video.sdk.platform.entity.Device item : ret.getDeviceList()) {
                        if (item.getDeviceId().equals(oDevice.getUid())) {
                            danaleDevice = item;
                            break;
                        }
                    }
                    if (danaleDevice != null) {
                        connection = danaleDevice.getConnection();
                        if (initDanaleDeviceListener != null) {
                            initDanaleDeviceListener.initOk(danaleDevice);
                        }

                    } else {

                        if (initDanaleDeviceListener != null) {
                            initDanaleDeviceListener.error();
                        }

                    }
                }
            }

            @Override
            public void onCommandExecFailure(PlatformResult platformResult, int i) {
                if (initDanaleDeviceListener != null) {
                    initDanaleDeviceListener.error();
                }

            }

            @Override
            public void onOtherFailure(PlatformResult platformResult, HttpException e) {
                if (initDanaleDeviceListener != null) {
                    initDanaleDeviceListener.error();
                }

            }
        });
    }


    public void startVideo(final Device oDevice) {


        initDannleDevice(oDevice, new InitDanaleDeviceListener() {
            @Override
            public void initOk(com.danale.video.sdk.platform.entity.Device danaleDevice) {

                if (danaleDevice != null && danaleDevice.getOnlineType() == OnlineType.OFFLINE) {
                    close("设备" + oDevice.getDeviceName() + "离线");
                    return;
                }

                getView().showWindowView();
                // 音频收集器
                int bufferSizeInBytes = AudioRecord.getMinBufferSize(8000,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                        bufferSizeInBytes);

                // 创建DanalePlayer
                danalePlayer = new DanalePlayer(getContext(),
                        (SurfaceView) getView().getWindowView().findViewById(R.id.sv),
                        (DanaleGlSurfaceView) getView().getWindowView().findViewById(R.id.gl_sv));

                // 设置播放状态监听
                danalePlayer.setOnPlayerStateChangeListener(XiaoOuVideoPresenter.this);
                // 开始播放（播放器先开始播放，然后在发指令，这样可以让第一帧数据立即显示）
                // 第一个参数是是否硬件,第二个参数是播放设备类型
                danalePlayer.preStart(true, DeviceType.IPC);
                //是否使用缓冲区
                danalePlayer.useBuffer(false);

                //设置音频数据回调
                danalePlayer.setAudioDataCallback(new Connection.LiveAudioReceiver() {

                    @Override
                    public void onReceiveAudio(byte[] data) {
                        // 接受音频数据的回调函数
                        if (isAudioOpened == true && audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
                            audioTrack.write(data, 0, data.length);
                        }

                    }
                });
                // 发送指令：开启视频
                connection.startLiveVideo(0, channel, videoQuality, danalePlayer,
                        new DeviceResultHandler() {

                            @Override
                            public void onSuccess(DeviceResult result) {
                                isVideoOpened = true;

                            }

                            @Override
                            public void onFailure(DeviceResult result, int errorCode) {
                                isVideoOpened = false;
                                close("开启视频失败");

                            }
                        });

                // 设置连接中断的回调
                connection.setOnConnectionErrorListener(new Connection.OnConnectionErrorListener() {

                    @Override
                    public void onConnectionError() {
                        // 建议在此处进行stop视频操作后,继续start视频

                        close("视频链接中断");

                    }
                });
            }

            @Override
            public void error() {


                speechManager.startSpeak("打开摄像头失败");
            }
        });
    }

    @Override
    public void onVideoPlaying(int i) {
        isVideoOpened = true;
    }

    @Override
    public void onVideoSizeChange(int i, int i1, int i2) {

    }

    @Override
    public void onVideoTimout() {
        close("设备链接超时");
    }


    public void stopVideo(Device oDevice) {

        initDannleDevice(oDevice, new InitDanaleDeviceListener() {
            @Override
            public void initOk(com.danale.video.sdk.platform.entity.Device danaleDevice) {
                close("");
            }

            @Override
            public void error() {
                close("");
            }
        });

    }

    private void close(String text) {


        if (!TextUtils.isEmpty(text) && speechManager != null) {
            speechManager.startSpeak(text);
        }

        getView().hideWindowView();


        if (danalePlayer != null) {
            danalePlayer.stop(true);
        }

        if (connection != null) {

            connection.stopLiveVideo(0, channel, danalePlayer,
                    new DeviceResultHandler() {

                        @Override
                        public void onSuccess(DeviceResult result) {


                        }

                        @Override
                        public void onFailure(DeviceResult result, int errorCode) {
                        }
                    });

        }
    }
}
