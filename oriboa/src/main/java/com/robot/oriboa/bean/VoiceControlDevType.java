package com.robot.oriboa.bean;

/**
 * 可以语音控制的设备类型
 */
public enum VoiceControlDevType {

    LIGHT("灯", new int[]{0, 1, 19, 38}),
    AirConditioner("空调", new int[]{5}),
    AirContralDevice("空调面板", new int[]{36}),
    Socket("插座", new int[]{2, 43, 29}),
    TV("电视", new int[]{6});

    private String typeName;
    private int[] typeContains;


    VoiceControlDevType(String typeName, int[] typeContains) {

        this.typeName = typeName;
        this.typeContains = typeContains;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int[] getTypeContains() {
        return typeContains;
    }

    public void setTypeContains(int[] typeContains) {
        this.typeContains = typeContains;
    }
}
