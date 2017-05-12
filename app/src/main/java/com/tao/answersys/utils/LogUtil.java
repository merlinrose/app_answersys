package com.tao.answersys.utils;

import android.provider.Settings;
import android.util.Log;

import com.tao.answersys.global.Global;

import static com.tao.answersys.global.Global.DEBUG_MODE;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class LogUtil {
    public static void log(String tag, String message){
        if(DEBUG_MODE) {
            Log.e(tag, message);
        }
    }
}
