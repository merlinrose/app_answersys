package com.tao.answersys.net;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LiangTao on 2017/4/15.
 */
public class HttpClient {
    private static HttpClient instance = null;
    private static OkHttpClient httpClient = null;

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = new HttpClient();
            httpClient = new OkHttpClient();
        }

        return instance;
    }

    public Response get(String url) throws IOException {
        Log.e("get req", url);

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    public Response post(String url, HashMap<String, String> paramsMap) throws IOException {
        Log.e("post req", url);
        Log.e("post req params", paramsMap.toString());
        //处理参数
        FormBody.Builder formBuilder = new FormBody.Builder();
        for(Map.Entry<String, String> params : paramsMap.entrySet()) {
            formBuilder.add(params.getKey(), params.getValue());
        }

        RequestBody requestBody = formBuilder.build();

        //创建一个请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();

        return response;
    }
}
