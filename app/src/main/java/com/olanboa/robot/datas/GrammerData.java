package com.olanboa.robot.datas;


/**
 * 语音控制的指令集
 */
public class GrammerData {


    public static final String SanPotWelcome = "欧朗博,智能家居控制系统启动成功";

    public static final String[] orderDO = new String[]{"主人,我这就处理", "马上完成主人的命令"};

    public static final String orderError = "主人,这条命令我还不会";


    /*设备开关控制*/
    public static final String openOrder = "开";
    public static final String closeOrder = "关";

    /*温度控制*/
    public final static String highState = "高";
    public final static String lowerState = "低";

    /*光线控制*/
    public final static String lightHigh = "亮";
    public final static String lightLower = "暗";


}
