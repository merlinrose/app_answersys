package com.tao.answersys.bean;

/**
 * Created by LiangTao on 2017/4/25.
 */
public class Student {
    private int stuId;
    private String num;
    private String name;
    private String gender;
    private int majorId;
    private String majorName;
    private String className;
    private int aexp;
    private int qexp;
    private String range;
    private int qnum;
    private int anum;
    private String photo;

    public int getStuId() {
        return stuId;
    }

    public String getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getMajorId() {
        return majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public String getClassName() {
        return className;
    }

    public int getAexp() {
        return aexp;
    }

    public int getQexp() {
        return qexp;
    }

    public String getRange() {
        return range;
    }

    public int getQnum() {
        return qnum;
    }

    public int getAnum() {
        return anum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setName(String name) {
        name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setAexp(int aexp) {
        this.aexp = aexp;
    }

    public void setQexp(int qexp) {
        this.qexp = qexp;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public void setQnum(int qnum) {
        this.qnum = qnum;
    }

    public void setAnum(int anum) {
        this.anum = anum;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
