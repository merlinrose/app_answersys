package com.tao.answersys.presenter.news.impl;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import com.tao.answersys.bean.Question;
import com.tao.answersys.event.EventNewsUpdate;
import com.tao.answersys.presenter.news.PresenterViewNews;
import com.tao.answersys.biz.BizQuestion;
import com.tao.answersys.presenter.news.PresenterNews;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 */

public class PresenterNewsImpl implements PresenterNews {
    private PresenterViewNews mViewNews;
    private BizQuestion mBizQuestion;

    public PresenterNewsImpl(PresenterViewNews viewNews, BizQuestion modelNews) {
        this.mViewNews = viewNews;
        this.mBizQuestion = modelNews;
    }

    @Override
    public void loadData(int page) {
        AsyncTask<Integer, String, List<Question>>  task = new AsyncTaskNews();
        task.execute(page);
    }

    public class AsyncTaskNews extends AsyncTask<Integer, String, List<Question>>{
        @Override
        protected List<Question> doInBackground(Integer...params) {
            int page = 0;
            if(params.length == 1){
                page = params[0];
            }

            return mBizQuestion.getNews(page);
        }

        @Override
        protected void onPostExecute(List<Question> newsItems) {
            super.onPostExecute(newsItems);
            mViewNews.updateView(newsItems);
        }
    }
}
