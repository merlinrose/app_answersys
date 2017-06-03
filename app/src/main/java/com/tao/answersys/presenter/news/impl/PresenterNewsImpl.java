package com.tao.answersys.presenter.news.impl;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

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
    private final static int PAGE_SIZE = 10;
    private int page = 1;
    private boolean isLoading = false;
    private boolean hasMore = true;

    public PresenterNewsImpl(PresenterViewNews viewNews, BizQuestion modelNews) {
        this.mViewNews = viewNews;
        this.mBizQuestion = modelNews;
    }

    @Override
    public void loadData(int p) {
        AsyncTask<Integer, String, List<Question>>  task = new AsyncTaskNews();
        page = p;
        hasMore = true;
        task.execute();
    }

    @Override
    public void loadMoreData() {
        if(!isLoading && hasMore) {
            page++;

            loadData(page);
        }
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    public class AsyncTaskNews extends AsyncTask<Integer, String, List<Question>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
        }

        @Override
        protected List<Question> doInBackground(Integer...params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mBizQuestion.getNews(page);
        }

        @Override
        protected void onPostExecute(List<Question> newsItems) {
            super.onPostExecute(newsItems);
            if(newsItems == null || newsItems.size() == 0 || newsItems.size() < PAGE_SIZE) {
                hasMore = false;
            }

            mViewNews.addData(newsItems);
            isLoading = false;
        }
    }
}
