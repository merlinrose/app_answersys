package com.tao.answersys.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.adapter.AdapterLessons;
import com.tao.answersys.bean.Lesson;
import com.tao.answersys.event.ErrorEventLessonPage;
import com.tao.answersys.event.EventLessonItemClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class ActivityLessonSelect extends ActivityBase{
    private final static String TITLE_LESSON_SELECT = "科目选择";
    private final static String OPERATION_TEXT = "";

    private RecyclerView.Adapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_lessons);
        EventBus.getDefault().register(this);

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TITLE_LESSON_SELECT;
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

    private void initLessonList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lessons_listview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterLessons(this);
        recyclerView.setAdapter(adapter);

        new AsyncTaskLesson().execute();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEventLessonPage errorEvent) {
        showPromptMessage("遇到错误：" + errorEvent.getMsg());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLessonItemSelect(EventLessonItemClick event) {
        Lesson lesson = event.getLesson();
        Intent intent = new Intent(ActivityLessonSelect.this, ActivityPublish.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lesson", lesson);
        intent.putExtras(bundle);
        setResult(INTENT_RESULT_SUC, intent);
        finish();
    }

    private class AsyncTaskLesson extends AsyncTask<Void, Void, List<Lesson>> {

        @Override
        protected List<Lesson> doInBackground(Void... params) {
            return mBizUser.getLessons();
        }

        @Override
        protected void onPostExecute(List<Lesson> lessons) {
            super.onPostExecute(lessons);
            if(lessons == null) {

            } else {
                ((AdapterLessons)adapter).setdata(lessons);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
