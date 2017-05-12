package com.tao.answersys.bean;

import java.io.Serializable;

/**
 * Created by LiangTao on 2017/4/24.
 */
public class Question implements Serializable{
    private int id;
    private int stuId;
    private int questionId;
    private String userName;
    private String summary;
    private String content;
    private String title;
    private String lesson;
    private String createDay;
    private String createTime;
    private String[] imgUrls;
    private boolean isCollect;
    private int readSum;

    public Question() {
    }

    public Question(int stuId, int questionId, String userName, String summary, String content, String title, String lesson, String createDay, String createTime, String[] imgUrls, boolean isCollect) {
        this.stuId = stuId;
        this.questionId = questionId;
        this.userName = userName;
        this.summary = summary;
        this.content = content;
        this.title = title;
        this.lesson = lesson;
        this.createDay = createDay;
        this.createTime = createTime;
        this.imgUrls = imgUrls;
        this.isCollect = isCollect;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String[] getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String[] imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReadSum() {
        return readSum;
    }

    public void setReadSum(int readSum) {
        this.readSum = readSum;
    }
}
