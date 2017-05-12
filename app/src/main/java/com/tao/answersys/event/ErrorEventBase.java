package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/4/24.
 */

public class ErrorEventBase {
    private String mMsg;
    public ErrorEventBase(String msg) {
        this.mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String mMsg) {
        this.mMsg = mMsg;
    }
}
