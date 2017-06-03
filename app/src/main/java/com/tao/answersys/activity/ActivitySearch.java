package com.tao.answersys.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.adapter.AdapterQuestions;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.ErrorEventSearchPage;
import com.tao.answersys.event.EventUserSearchResultItemClick;
import com.tao.answersys.view.RecyclerviewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class ActivitySearch extends ActivityBase {
    private final static String TITLE_SEARCH = "搜索";
    private final static String OPERATION_TEXT = "";
    private AdapterQuestions adapter = null;

    private EditText editTextSearch = null;
    private View btnBack = null;
    private View btnSearch = null;

    private AsyncTask task;

    @Override
    protected void init() {
        setContentView(R.layout.activity_search);
        EventBus.getDefault().register(this);

        initLessonList();
        initView();
    }

    private void initView() {
        editTextSearch = (EditText) findViewById(R.id.search_edittext_keyword);
        btnBack = findViewById(R.id.search_btn_back);
        btnSearch = findViewById(R.id.search_btn_search);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySearch.this.finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = editTextSearch.getText().toString().trim();
                if(temp.equals("")) {
                } else {
                    task = new AsyncTaskSearch().execute(temp);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initLessonList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_listview_result);
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
    }

    public class AsyncTaskSearch extends AsyncTask<String, String, List<Question>> {
        @Override
        protected List<Question> doInBackground(String...params) {
            String keyWord = "";
            if(params.length == 1){
                keyWord = params[0];
            }

            return mBizQuestion.searchQuestion(1, keyWord);
        }

        @Override
        protected void onPostExecute(List<Question> data) {
            super.onPostExecute(data);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEventSearchPage error) {
        showPromptMessage(error.getMsg());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemClick(EventUserSearchResultItemClick event) {
        Intent intent = new Intent(this, ActivityQuestionDetail.class);
        intent.putExtra("questionId", event.getQuestionId());
        startActivity(intent);
    }
}
