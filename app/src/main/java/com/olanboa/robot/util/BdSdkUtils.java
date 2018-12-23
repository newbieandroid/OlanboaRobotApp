package com.olanboa.robot.util;

import android.util.Log;

import com.baidu.aip.nlp.AipNlp;
import com.google.gson.Gson;
import com.orvibo.homemate.bo.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


    /*对语句进行词语拆分*/
    public JSONObject lexer(String text) {
        return client.lexer(text, null);
    }

    public JSONObject simnet(String userSpeak, Device device) {

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

        return client.simnet(userSpeak, device.getDeviceName(), options);
    }


    /*获取内容中的所有名词*/
    public List<String> getNArrayList(String text) {

        List<String> nArrays = new ArrayList<>();

        //把用户所说的话进行词语拆分,并且保存语句中的所有名词
        JSONObject userSpeakJson = lexer(text);


        Log.e("csl", "==百度语音拆分的结果==>" + userSpeakJson.toString());


        try {
            if (userSpeakJson.getInt("status") == 0) {

                JSONArray jsonArray = userSpeakJson.getJSONObject("results").getJSONArray("items");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //如果是名词
                    if (jsonObject.getString("pos").equals("n")) {
                        nArrays.add(jsonObject.getString("item"));
                    }

                }


                Log.e("csl", "==百度语音拆分的结果==>" + new Gson().toJson(nArrays));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nArrays;
    }


}
