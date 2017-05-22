package com.tao.answersys.bean;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class NbQuestionPublish {
    private int lessonId;
    private int stuId;
    private int id;
    private String title;
    private String content;
    private List<String> attchList;

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLessonId() {
        return lessonId;
    }

    public int getStuId() {
        return stuId;
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

    public List<String> getAttchList() {
        return attchList;
    }

    public void setAttchList(List<String> attchList) {
        this.attchList = attchList;
    }
}
