package com.tao.answersys.bean;

import android.graphics.Bitmap;

public class MediaViewBean {
	private Bitmap bitmap = null;
	private String path = "";
	private int position;
	
	public MediaViewBean(int position, Bitmap bitmap, String path) {
		super();
		this.position = position;
		this.bitmap = bitmap;
		this.path = path;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
