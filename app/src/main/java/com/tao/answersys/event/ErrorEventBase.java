package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/4/24.
 * 错误事件基类
 * <br>携带了错误信息
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
