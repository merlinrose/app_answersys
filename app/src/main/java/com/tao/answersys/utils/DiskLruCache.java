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

public class DiskLruCache<T> {
    public final static int TYPE_IMAGE = 1;
    public final static String CACHE_DIR_IMAGE = "image";

    public int mContentType = 1;

    private File mDiskCache;

    public DiskLruCache() {
        init();
    }

    private void init() {
        mDiskCache = CustApplication.getAppContext().getCacheDir();
        Log.e("ImageLoader", mDiskCache.getPath());
        if (mDiskCache == null) {
            mDiskCache = CustApplication.getAppContext().getExternalCacheDir();
        }
    }

    public File get(String key) {
        if (mContentType == TYPE_IMAGE) {
            File file = new File(mDiskCache.getPath() + "/" + CACHE_DIR_IMAGE);

            if (!file.exists()) {
                return null;
            }

            File[] fileList = file.listFiles();

            for (File f : fileList) {
                String fName = f.getName();
                if (fName.equals(SafeUtil.getMD5Str(key) + ".jpg")) {
                    return f;
                }
            }
        }

        return null;
    }

    public void putImage(String key, Bitmap bitmap) {
        try {
            if (mContentType == TYPE_IMAGE) {
                File file = new File(mDiskCache.getPath() + "/" + CACHE_DIR_IMAGE, SafeUtil.getMD5Str(key) + ".jpg");
                Log.e("DiskLruCahce", file.getAbsolutePath());
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }

                if (!file.exists()) {
                    Log.e("create cache file : ", "" + file.createNewFile());
                } else {
                    Log.e("DiskLruCache", "the file exists");
                }

                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new ErrorEventMainPage(e.getMessage()));
        }
    }
}
