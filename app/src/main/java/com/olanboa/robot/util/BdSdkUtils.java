package com.olanboa.robot.util;

import android.content.Context;

import com.baidu.aip.nlp.AipNlp;

import org.json.JSONObject;

/**
 * 百度sdk：语言处理基础技术
 */
public class BdSdkUtils {

    private static final String APP_ID = "15135411";
    private static final String API_KEY = "t5ZZTnL7IEcabrMyeZuq7gra";
    private static final String SECRET_KEY = "gy83LYb957fdjy9U2rsORkv3cvGyRmNY";

    private AipNlp client;

    private static BdSdkUtils instance;

    private BdSdkUtils() {
        client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
    }

    public static BdSdkUtils getInstance() {

        if (instance == null) {
            instance = new BdSdkUtils();
        }

        return instance;

    }

    public JSONObject getTextItems(String text) {
        return client.lexer(text, null);
    }

    public JSONObject simnet(String text1, String text2) {
        return client.simnet(text1, text2, null);
    }
}
