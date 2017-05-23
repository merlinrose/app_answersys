package com.tao.answersys.net;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by LiangTao on 2017/4/15.
 */
public class HttpClient {
    private static HttpClient instance = null;
    private static OkHttpClient httpClient = null;
    private IProgressCallBack progressCallBack;
    private float allSize = 0f;
    private float hasFinishedSize = 0f;

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = new HttpClient();
            httpClient = new OkHttpClient();
        }

        return instance;
    }

    public void setProgressCallBack(IProgressCallBack callBack) {
        this.progressCallBack = callBack;
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

    /**
     * 上传文件
     * @param url
     * @param paramNames
     * @param files
     * @return
     * @throws IOException
     */
    public Response uploadFile(String url, Map<String, String> paramNames, List<File> files) throws IOException {
        Log.e("post req", url);
        MultipartBody.Builder builder =  new MultipartBody.Builder().setType(MultipartBody.FORM);

        this.allSize = 0f;
        this.hasFinishedSize = 0f;

        for(int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody fileBody = createFileRequestBody(file, MediaType.parse("application/octet-stream"));

            this.allSize += file.length();

        //    String paramStr = "";

            builder.addFormDataPart("attach", new Date().getTime()+"" , fileBody);
        }

        if(paramNames != null) {
            // Map<String, String> map = paramNames.get(i)
            // paramStr = map.get("id") +""+ map.get("file_type");
            Log.e("upload",  paramNames.get("id").toString());
            builder.addFormDataPart("stuId", paramNames.get("id").toString());
        }


        Request request = new Request.Builder()
                .url(url)
                .method("POST", builder.build())
                .build();

        Response response = httpClient.newCall(request).execute();

        return response;
    }

    public static RequestBody createFileRequestBody(final File file, final MediaType type) {
        RequestBody reqBody = new RequestBody() {
            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    for (long readCount; (readCount = source.read(buf, 1024)) != -1; ) {
                        sink.write(buf, readCount);

                        instance.hasFinishedSize += readCount;
                        if(instance.progressCallBack != null) {
                            instance.hasFinishedSize += readCount;
                            instance.progressCallBack.onProgressUpdate(instance.hasFinishedSize , instance.allSize);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(source != null) {
                        source.close();
                    }
                }
            }

            @Override
            public MediaType contentType() {
                return type;
            }
        };

        return reqBody;
    }

    public interface IProgressCallBack {
        public void onProgressUpdate(float hasFinish, float allSize);
    }
}
