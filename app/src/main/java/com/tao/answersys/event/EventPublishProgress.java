package com.tao.answersys.event;

/**
 * Created by LiangTao on 2017/5/23.
 */

public class EventPublishProgress {
    private String progress;

    public EventPublishProgress(String progress) {
        this.progress = progress;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
