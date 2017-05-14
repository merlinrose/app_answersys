package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/5/14.
 */

public class EventUserReqUpdateAnswer {
    private int answerId;
    private String oldAnswer;

    public EventUserReqUpdateAnswer(int answerId, String oldAnswer) {
        this.answerId = answerId;
        this.oldAnswer = oldAnswer;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getOldAnswer() {
        return oldAnswer;
    }

    public void setOldAnswer(String oldAnswer) {
        this.oldAnswer = oldAnswer;
    }
}
