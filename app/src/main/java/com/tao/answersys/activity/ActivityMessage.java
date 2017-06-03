package com.tao.answersys.activity;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.adapter.AdapterMessage;
import com.tao.answersys.bean.Message;
import com.tao.answersys.event.ErrorEventMessagePage;
import com.tao.answersys.view.RecyclerviewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class ActivityMessage extends ActivityBase {
    private final static String TiTLE_MESSAGE = "消息";
    private final static String OPERATION_TEXT = "";
    private AdapterMessage adapter;

    @Override
    protected void init() {
        setContentView(R.layout.activity_message);
        EventBus.getDefault().register(this);

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TiTLE_MESSAGE;
            }

            @Override
            public void onButtonOperationClick() {
            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });

        initListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initListView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.message_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new AdapterMessage(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerviewDivider(this, 10));
        recyclerView.setAdapter(adapter);

        new AsyncTaskMessage().execute();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEventMessagePage event) {
        showPromptMessage(event.getMsg());
    }

    public class AsyncTaskMessage extends AsyncTask<Void, String, List<Message>> {
        @Override
        protected List<Message> doInBackground(Void...params) {
            return mBizUser.getNewMessage();
        }

        @Override
        protected void onPostExecute(List<Message> data) {
            super.onPostExecute(data);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }

}
