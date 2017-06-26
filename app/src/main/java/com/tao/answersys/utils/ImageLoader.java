package com.tao.answersys.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.tao.answersys.bean.ImageLoadTask;
import com.tao.answersys.event.ErrorEventMainPage;
import com.tao.answersys.net.HttpClient;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import okhttp3.Response;

/**
 * Created by LiangTao on 2017/4/16.
 * <br>图片加载器
 * <br>主要是从网络中获取图片，并使用了缓存
 */
public class ImageLoader {
    private int mCasheSize = (int) Runtime.getRuntime().maxMemory() / 8;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, Bitmap> mLruCache;
    private static ImageLoader mImageLoader;
    private boolean isGettingImg = false;
    private Queue<ImageLoadTask> queue = new LinkedList<ImageLoadTask>();

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
            mImageLoader.init();
        }

        return mImageLoader;
    }

    public void init() {
        mLruCache = new LruCache<String, Bitmap>(mCasheSize);
        mDiskLruCache = new DiskLruCache();
    }

    /**
     * 异步获取图片
     * @param url 请求路径
     * @param imageView
     */
    public void loadImageAsync(final String url, final ImageView imageView) {
        if(isGettingImg) {
            queue.add(new ImageLoadTask(url, imageView));
            return;
        }

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                isGettingImg = true;
                Bitmap bitmap = null;
                bitmap = getImage(url);
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
                super.onPostExecute(bitmap);
                isGettingImg = false;
                if(queue.size() > 0) {
                    ImageLoadTask task = queue.poll();
                    loadImageAsync(task.getUrl(), task.getImageView());
                }
            }
        }.execute();
    }

    /**
     * 获取图片
     * @param url
     * @return
     */
    public Bitmap getImage(String url) {
        //从内存中获取图片
        Bitmap bitmap = mLruCache.get(url);

        if (bitmap == null) {
            //从缓存中获取图片
            File imgFile = mDiskLruCache.get(url);
            if (imgFile != null) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }

            if (bitmap == null) {
                try {
                    Response response = HttpClient.getInstance().get(url);
                    if (response.code() == 200) {
                        bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        if (bitmap != null) {
                            //放图片到内存
                            mLruCache.put(url, bitmap);
                            //放图片到缓存
                            mDiskLruCache.putImage(url, bitmap);
                        }
                    } else {
                        EventBus.getDefault().post(new ErrorEventMainPage("http:" + response.code()));
                    }
                } catch (Exception e) {
                    EventBus.getDefault().post(new ErrorEventMainPage(e.getMessage()));
                }
            }
        }

        return bitmap;
    }
}
