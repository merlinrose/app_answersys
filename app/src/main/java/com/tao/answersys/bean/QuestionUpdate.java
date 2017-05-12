package com.tao.answersys.bean;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class QuestionUpdate {
    private int id;
    private String title;
    private String content;
    private int stuId;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }
}
