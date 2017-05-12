package com.tao.answersys.bean;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/30.
 */

public class NbMessages extends NetBean {
    private List<Message> result;

    public List<Message> getResult() {
        return result;
    }

    public void setResult(List<Message> result) {
        this.result = result;
    }
}
