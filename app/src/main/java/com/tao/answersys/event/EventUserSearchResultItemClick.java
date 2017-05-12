package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/4/26.
 */

public class EventUserSearchResultItemClick {
    private int mQuestionId;

    public EventUserSearchResultItemClick(int mQuestionId) {
        this.mQuestionId = mQuestionId;
    }

    public int getQuestionId() {
        return mQuestionId;
    }
}
