package com.tao.answersys.bean;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/29.
 */

public class NbAnswerItems extends NetBean{
    private List<AnswerItem> result;

    public List<AnswerItem> getResult() {
        return result;
    }

    public void setResult(List<AnswerItem> result) {
        this.result = result;
    }
}
