package com.tao.answersys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;


/**
 * ��滺��
 * 
 * @author LiangTao
 *
 */
public class FileCache<T> implements BaseCache<T> {

	private FileOutputStream fos;
	private File cacheFile;

	public FileCache(Context context) {
		cacheFile = context.getExternalCacheDir();
		try {
			fos = new FileOutputStream(cacheFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void put(String key, T value) {
		if (key != null && !key.equals("") && value != null) {

			File file = new File(cacheFile.getPath() + "/" + key);
			if (!file.exists()) {

				OutputStream fos = null;

				try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				((Bitmap) value).compress(CompressFormat.WEBP, 70, fos);
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(String key) {
		File file[] = cacheFile.listFiles();
		T cacheInstance = null;
		for (File f : file) {
			if (f.getName().equals(key)) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(f);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				//cacheInstance = (T) Tool.getBitmapByInstream(fis);
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return cacheInstance;
	}

	@Override
	public void clear() {
		cacheFile.delete();
	}

}
