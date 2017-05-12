package com.tao.answersys.event;

import com.tao.answersys.bean.Lesson;

/**
 * Created by LiangTao on 2017/4/26.
 */

public class EventLessonItemClick {
    private Lesson mLesson;

    public EventLessonItemClick(Lesson lesson) {
        this.mLesson = lesson;
    }

    public Lesson getLesson() {
        return mLesson;
    }
}
