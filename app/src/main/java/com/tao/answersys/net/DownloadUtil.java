package com.tao.answersys.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by LiangTao on 2017/4/15.
 */

public class DownloadUtil {
    private static DownloadUtil instance;
    private static HttpClient httpClient;

    private DownloadUtil() {

    }

    public static DownloadUtil getInstance() {
        if(instance == null) {
            instance = new DownloadUtil();
            httpClient = HttpClient.getInstance();
        }

        return instance;
    }

    public Bitmap downloadImage(String url) throws IOException {
        Response response = httpClient.get(url);

        InputStream insputsteam = response.body().byteStream();

        Bitmap bitmap = BitmapFactory.decodeStream(insputsteam);
        return bitmap;
    }
}
