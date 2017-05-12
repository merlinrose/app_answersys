package com.tao.answersys.bean;

import java.io.Serializable;

/**
 * Created by LiangTao on 2017/4/30.
 */

public class Lesson implements Serializable{
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
