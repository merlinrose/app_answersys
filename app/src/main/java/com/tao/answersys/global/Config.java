package com.tao.answersys.global;

/**
 * Created by LiangTao on 2017/4/24.
 */

public class Config {
    public final static String HOST = "http://192.168.191.1";
    public final static String PORT = "8080";
    public final static String BASE_URL = HOST+":"+PORT+"/app/";

    public final static String USER_TYPE_STU = "STU";
    public final static String USER_TYPE_TEACHER = "TEACHER";

    public final static String SHARED_PREF_FILE_NAME = "com.tao.answersys.pref";
    public final static String SHARED_PREF_KEY_MESSAGE_LOAD_DATE = "message_load_date";
}
