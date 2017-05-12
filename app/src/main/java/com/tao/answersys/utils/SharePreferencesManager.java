package com.tao.answersys.utils;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;

public class SharePreferencesManager {
	private static SharePreferencesManager mInstance;
	private SharedPreferences sharePrefer;
	
	private SharePreferencesManager() {
		
	}
	
	public static SharePreferencesManager getInstance() {
		if(mInstance == null) {
			mInstance = new SharePreferencesManager();
			Context context = CustApplication.getAppContext();
			mInstance.sharePrefer = context.getSharedPreferences(Config.SHARED_PREF_FILE_NAME, context.MODE_PRIVATE);
		}
		
		return mInstance;
	}
	
	public boolean put(String key, int value) {
		Editor editor = sharePrefer.edit();
		editor.putInt(key, value);
		
		return editor.commit();
	}
	
	public boolean put(String key, Date value) {
		Editor editor = sharePrefer.edit();
		editor.putLong(key, value.getTime());
		
		return editor.commit();
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return δ���÷���null
	 */
	public Date getDate(String key, Date value) {
		long dateLong = sharePrefer.getLong(key, -1);
		if(dateLong != -1) {
			return new Date(dateLong);
		}
		
		return value;
	}
	
	public boolean put(String key, boolean value) {
		Editor editor = sharePrefer.edit();
		editor.putBoolean(key, value);
		
		return editor.commit();
	}
	
	public int getInt(String key, int defValue) {
		return sharePrefer.getInt(key, defValue);
	}
	
	public boolean getBoolean(String key, boolean defValue) {
		return sharePrefer.getBoolean(key, defValue);
	}
}
