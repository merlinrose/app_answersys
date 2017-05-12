package com.tao.answersys.utils;

public interface BaseCache <T>{
	public void put(String key, T value);
	public T get(String key);
	public void clear();
}
