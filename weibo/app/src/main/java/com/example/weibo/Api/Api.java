package com.example.weibo.Api;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.weibo.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static OkHttpClient client;     //新建Client
    private static String requestUrl;       //请求的地址：ip+接口
    private static RequestBody params;     //请求体中的参数
    public static Api api = new Api();

    public Api() {

    }

    public static Api config(String url, RequestBody formBody) {
        client = new OkHttpClient.Builder().build();    //新建Client对象
        requestUrl = ApiConfig.BASE_URl + url;      //请求接口的地址
        params = formBody;       //请求体中的参数
        return api;
    }

    //封装Post方法，输入参数为定义的回调函数
    public void postRequest(Context context, final TtitCallback callback) {
        final Request request = new Request.Builder()
                .url(requestUrl)
                .post(params)
                .build();
        Call call = client.newCall(request);
        //第五步发起请求，给入参回调函数callback传入错误信息/响应消息
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

//    public void getRequest(final TtitCallback callback) {
//        String url = getAppendUrl(requestUrl, params);
//        Request request = new Request.Builder()
//                .url(url).get().build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("onFailure", e.getMessage());
//                callback.onFailure(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String result = response.body().string();
////                try {
////                    JSONObject jsonObject = new JSONObject(result);
////                    String code = jsonObject.getString("code");
////                    if (code.equals("401")) {
////                        Intent in = new Intent(context, LoginActivity.class);
////                        context.startActivity(in);
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//                callback.onSuccess(result);
//            }
//        });
//    }
    //将参数拼接成地址
    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }
}

