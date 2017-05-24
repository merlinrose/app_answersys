package com.tao.answersys.frament;


import android.graphics.Canvas;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tao.answersys.R;
import com.tao.answersys.adapter.AdapterNews;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.EventNewsUpdate;
import com.tao.answersys.frament.base.FragmentBase;
import com.tao.answersys.listener.RecyclerViewScrollListener;
import com.tao.answersys.presenter.news.PresenterNews;
import com.tao.answersys.presenter.news.impl.PresenterNewsImpl;
import com.tao.answersys.presenter.news.PresenterViewNews;
import com.tao.answersys.biz.BizQuestion;
import com.tao.answersys.view.RecyclerviewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/12.
 */

public class FragmentNews extends FragmentBase implements PresenterViewNews {
    private PresenterNews mPresenterNews;
    private AdapterNews mNewAdapter;
    private static int ITEM_DIVIDER_HEIGHT = 40;
    private SwipeRefreshLayout mRefreshLayout = null;
    private View loadView;

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        initNewsView();
        initRefreshView();

        mPresenterNews = new PresenterNewsImpl(this, new BizQuestion());
        mPresenterNews.loadData(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initRefreshView() {
        loadView = findViewById(R.id.frag_news_loading);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.frag_news_refresh);
        //设置下拉多少会触发更新
        mRefreshLayout.setDistanceToTriggerSync(400);
        //设置下拉圆圈的大小
        mRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenterNews.loadData(1);
            }
        });
    }

    private void initNewsView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.frag_news_content);

        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManger);

        mNewAdapter = new AdapterNews(getActivity());
        recyclerView.addItemDecoration(new RecyclerviewDivider(getActivity(), ITEM_DIVIDER_HEIGHT){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        recyclerView.setAdapter(mNewAdapter);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onLoadMoreData() {
                loadView.setVisibility(View.VISIBLE);
                mPresenterNews.loadMoreData();
            }
        });
    }

    public void loadError() {
        if(mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
            loadView.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateView(List<Question> data) {
        mNewAdapter.setData(data);

        if(mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
            mNewAdapter.notifyItemRangeChanged(0, data == null ? 0 : data.size());
        } else {
            int count = mNewAdapter.getItemCount();
            mNewAdapter.notifyItemRangeInserted(count==0? 0 : count-1, data == null ? 0 : data.size());
        }
    }

    @Override
    public void addData(List<Question> data) {
        if(mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
            mNewAdapter.notifyItemRangeChanged(0, data == null ? 0 : data.size());
            loadView.setVisibility(View.GONE);
        } else {
            int count = mNewAdapter.getItemCount();
            mNewAdapter.addData(data);
            mNewAdapter.notifyItemRangeInserted(count, data == null ? 0 : data.size());
            loadView.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsUpdate(EventNewsUpdate event) {
        mPresenterNews.loadData(1);
    }
}