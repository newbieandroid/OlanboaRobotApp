package com.olanboa.robot.util;

import android.content.Context;

import com.baidu.aip.nlp.AipNlp;

import org.json.JSONObject;

import java.util.HashMap;

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

//        BOW（词包）模型
//
//        基于bag of words的BOW模型，特点是泛化性强，效率高，比较轻量级，适合任务：输入序列的 term “确切匹配”、不关心序列的词序关系，对计算效率有很高要求；
//
//        GRNN（循环神经网络）模型
//
//        基于recurrent，擅长捕捉短文本“跨片段”的序列片段关系，适合任务：对语义泛化要求很高，对输入语序比较敏感的任务；
//
//        CNN（卷积神经网络）模型
//
//        模型语义泛化能力介于 BOW/RNN 之间，对序列输入敏感，相较于 GRNN 模型的一个显著优点是计算效率会更高些。


        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("model", "CNN");

        return client.simnet(text1, text2, options);
    }
}
