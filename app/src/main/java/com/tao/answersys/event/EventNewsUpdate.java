package com.tao.answersys.event;

import com.tao.answersys.bean.Question;

/**
 * Created by LiangTao on 2017/5/2.
 */

public class EventNewsUpdate {
    private Question question;

    public EventNewsUpdate(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
