package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/4/26.
 */

public class EventUserCollectItemClick {
    private int mQuestionId;

    public EventUserCollectItemClick(int mQuestionId) {
        this.mQuestionId = mQuestionId;
    }

    public int getQuestionId() {
        return mQuestionId;
    }
}
