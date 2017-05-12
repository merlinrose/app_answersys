package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/4/26.
 */

public class EventUserQuestionItemClick {
    private int mQuestionId;

    public EventUserQuestionItemClick(int mQuestionId) {
        this.mQuestionId = mQuestionId;
    }

    public int getQuestionId() {
        return mQuestionId;
    }
}
