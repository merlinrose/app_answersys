package com.tao.answersys.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.tao.answersys.event.ErrorEventMainPage;
import com.tao.answersys.global.CustApplication;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by LiangTao on 2017/4/17.
 */

public class DiskLruCache< T> {
    public final static int TYPE_IMAGE = 1;
    public final static String CACHE_DIR_IMAGE = "image";

    public int mContentType = 0;

    private File mDiskCache;

    public DiskLruCache() {
        init();
    }

    private void init() {
        mDiskCache = CustApplication.getAppContext().getCacheDir();
        Log.e("ImageLoader", mDiskCache.getPath());
        if(mDiskCache == null) {
            mDiskCache = CustApplication.getAppContext().getExternalCacheDir();
        }
    }

    public File get(String key) {
        if(mContentType == TYPE_IMAGE) {
            File file = new File(mDiskCache.getPath() + "/" + CACHE_DIR_IMAGE);

            if(!file.exists()) {
                return null;
            }

            File[] fileList = file.listFiles();
            Log.e("cache_file_size", "============="+fileList.length);

            for(File f : fileList) {
                String fName = f.getName();
                if(fName.equals(key)) {
                    return f;
                }
            }
        }

        return null;
    }

    public void putImage(String key, Bitmap bitmap) {
/*
        if (key != null && !key.equals("") && bitmap != null) {
            if(mContentType == TYPE_IMAGE) {
                File file = new File(mDiskCache.getPath() + "/" + CACHE_DIR_IMAGE, key+".jpg");
                if (!file.exists()) {

                    OutputStream fos = null;

                    try {
                        fos = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap.compress(Bitmap.CompressFormat.WEBP, 70, fos);
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        */
        try {


        if(mContentType == TYPE_IMAGE) {
            File file = new File(mDiskCache.getPath() + "/" + CACHE_DIR_IMAGE, key+".jpg");
            Log.e("============create", mDiskCache.getPath());
            if(!file.exists()) {
                Log.e("create cache file : ", ""+file.createNewFile());
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        }
        } catch (Exception e) {
            EventBus.getDefault().post(new ErrorEventMainPage(e.getMessage()));
        }
    }
}
