package com.tao.answersys.presenter.news.impl;

import com.tao.answersys.biz.BizQuestion;
import com.tao.answersys.presenter.base.PresenterView;
import com.tao.answersys.presenter.news.PresenterImageShow;

/**
 * Created by LiangTao on 2017/4/16.
 */

public class PresenterImageShowImpl implements PresenterImageShow {
    private BizQuestion mBizQuestion = null;
    private PresenterView mPresenterView = null;

    public PresenterImageShowImpl(BizQuestion bizQuestion, PresenterView presenterView) {
        mBizQuestion = bizQuestion;
        mPresenterView = presenterView;
    }
}
