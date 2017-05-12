package com.tao.answersys.frament;


import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.tao.answersys.R;
import com.tao.answersys.adapter.AdapterNews;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.EventNewsUpdate;
import com.tao.answersys.frament.base.FragmentBase;
import com.tao.answersys.presenter.news.PresenterNews;
import com.tao.answersys.presenter.news.impl.PresenterNewsImpl;
import com.tao.answersys.presenter.news.PresenterViewNews;
import com.tao.answersys.biz.BizQuestion;
import com.tao.answersys.view.RecyclerviewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/12.
 */

public class FragmentNews extends FragmentBase implements PresenterViewNews {
    private PresenterNews mPresenterNews;
    private AdapterNews mNewAdapter;
    private static int ITEM_DIVIDER_HEIGHT = 40;
    private SwipeRefreshLayout refreshLayout = null;

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
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.frag_news_refresh);
        //设置下拉多少会触发更新
        refreshLayout.setDistanceToTriggerSync(400);
        //设置下拉圆圈的大小
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
    }

    @Override
    public void updateView(List<Question> data) {
        Log.e("refresh", "刷新结束");
        mNewAdapter.setData(data);

        if(refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            mNewAdapter.notifyItemRangeChanged(0, data == null ? 0 : data.size());
        } else {
            int count = mNewAdapter.getItemCount();
            mNewAdapter.notifyItemRangeInserted(count==0? 0 : count-1, data == null ? 0 : data.size());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsUpdate(EventNewsUpdate event) {
       // List<Question> data = new ArrayList<Question>();
      //  data.add(event.getQuestion());
        mPresenterNews.loadData(1);
       // Toast.makeText(getContext(), "有新动态了："+event.getQuestion().getTitle(), Toast.LENGTH_SHORT).show();
    }
}