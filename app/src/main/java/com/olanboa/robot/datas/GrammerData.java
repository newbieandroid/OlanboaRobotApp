package com.olanboa.robot.datas;


/**
 * 语音控制的指令集
 */
public class GrammerData {


    public static final String SanPotWelcome = "欧朗博,智能家居控制系统启动成功";

    public static final String[] orderDO = new String[]{"主人,我这就处理", "马上完成主人的命令"};

    public static final String orderError = "主人,我没听清,请再说一次";

    public static final String offLine = "已离线";


    /*设备开关控制*/
    public static final String openOrder = "开";
    public static final String closeOrder = "关";

    /*温度控制*/
    public final static String highState = "高";
    public final static String lowerState = "低";


}
