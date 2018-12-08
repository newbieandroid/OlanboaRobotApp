package com.olanboa.robot.datas;


/**
 * 语音控制的指令集
 */
public class GrammerData {


    public static final String SanPotWelcome = "欧朗博,智能家居控制系统启动成功";

    public static final String[] orderDO = new String[]{"主人,正在处理您的指令", "主人,我这就处理", "马上处理主人的命令", "马上完成主人交代的任务"};

    public static final String orderError = "主人,这条命令我还不会";


    public static final String openOrder = "开";
    public static final String closeOrder = "关";

    public final static String highState = "高";
    public final static String lowerState = "低";

    public static final String[] devices = new String[]{"空调", "插座", "灯", "温度", "电视"};

}
