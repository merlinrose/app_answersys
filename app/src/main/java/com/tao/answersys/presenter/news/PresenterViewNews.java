package com.tao.answersys.presenter.news;

import com.tao.answersys.bean.Question;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 */

public interface PresenterViewNews {
    public void updateView(List<Question> data);
    public void addData(List<Question> data);
    public void loadError();
}
