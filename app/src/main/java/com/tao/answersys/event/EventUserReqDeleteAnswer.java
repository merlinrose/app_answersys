package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/5/14.
 */

public class EventUserReqDeleteAnswer {
    private int answerId;

    public EventUserReqDeleteAnswer(int answerId) {
        this.answerId = answerId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
}
