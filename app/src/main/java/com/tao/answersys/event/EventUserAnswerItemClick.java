package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/4/26.
 */

public class EventUserAnswerItemClick {
    private int mQuestionId;

    public EventUserAnswerItemClick(int mQuestionId) {
        this.mQuestionId = mQuestionId;
    }

    public int getQuestionId() {
        return mQuestionId;
    }
}
