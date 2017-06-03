package com.tao.answersys.presenter.news;

/**
 * Created by LiangTao on 2017/4/13.
 */

public interface PresenterNews {
    public void loadData(int page);

    public void loadMoreData();

    public boolean hasMore();
}
