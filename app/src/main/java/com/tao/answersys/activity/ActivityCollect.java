package com.tao.answersys.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.adapter.AdapterQuestions;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.EventUserCollectItemClick;
import com.tao.answersys.event.EventUserQuestionItemClick;
import com.tao.answersys.view.RecyclerviewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class ActivityCollect extends ActivityBase{
    private final static String TITLE_MY_COLLECT = "我的收藏";
    private final static String OPERATION_TEXT = "";

    private AdapterQuestions adapter;

    @Override
    protected void init() {
        setContentView(R.layout.activity_collect);
        EventBus.getDefault().register(this);

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TITLE_MY_COLLECT;
            }

            @Override
            public void onButtonOperationClick() {

            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });

        initLessonList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initLessonList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.collect_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerviewDivider(this, ITEM_DIVIDER_HEIGHT){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        adapter = new AdapterQuestions(this);
        recyclerView.setAdapter(adapter);

        new ActivityCollect.AsyncTaskCollect().execute(1);
    }

    public class AsyncTaskCollect extends AsyncTask<Integer, String, List<Question>> {
        @Override
        protected List<Question> doInBackground(Integer...params) {
            int page = 0;
            if(params.length == 1){
                page = params[0];
            }

            return mBizQuestion.getUserCollect(page);
        }

        @Override
        protected void onPostExecute(List<Question> data) {
            super.onPostExecute(data);
            adapter.addData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemClick(EventUserCollectItemClick event) {
        Intent intent = new Intent(this, ActivityQuestionDetail.class);
        intent.putExtra("questionId", event.getQuestionId());
        startActivity(intent);
    }
}
