package com.tao.answersys.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tao.answersys.R;
import com.tao.answersys.biz.BizQuestion;
import com.tao.answersys.biz.BizUser;
import com.tao.answersys.utils.LogUtil;
import com.tao.answersys.view.ProgressDialog;

/**
 * Created by LiangTao on 2017/4/13.
 */

public abstract class ActivityBase extends AppCompatActivity{
    public final static int ITEM_DIVIDER_HEIGHT = 40;
    protected final static int INTENT_RESULT_CANCEL = 0;
    protected final static int INTENT_RESULT_SUC = 1;
    protected final static int INETNT_RESULT_FAIL = 2;

    protected final static int INTENT_REQ_ANSWER = 3;
    protected final static int INTENT_REQ_LESSONS = 4;
    protected final static int INTENT_REQ_UPDATE = 5;
    protected final static int INTENT_REQ_UPDATE_ANSER = 6;
    protected final static int INTENT_REQ_PHOTO = 7;

    private TopBarListener mTopBarListener;
    private final static String TAG = "ActivityBase";

    protected BizQuestion mBizQuestion;
    protected BizUser mBizUser;

    protected ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initBiz();
        init();
        initToolBar();
        super.onCreate(savedInstanceState);
    }

    private void initBiz() {
        mBizQuestion = new BizQuestion();
        mBizUser = new BizUser();
    }

    private void initToolBar(){
        try {
            findViewById(R.id.top_bar_button_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.log(TAG, "Back Button has been Clicked");
                    if (mTopBarListener != null) {
                        mTopBarListener.onButtonBackClick();
                    }
                }
            });

            findViewById(R.id.top_bar_button_operation).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTopBarListener != null) {
                        mTopBarListener.onButtonOperationClick();
                    }
                }
            });

            ((TextView) findViewById(R.id.top_bar_title)).setText(mTopBarListener != null ? mTopBarListener.onSetTitle() : "");
            ((TextView) findViewById(R.id.top_bar_button_operation)).setText(mTopBarListener != null ? mTopBarListener.onSetOperationText() : "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract  void init();

    public void setOnTopBarListener(TopBarListener listener) {
        this.mTopBarListener = listener;
    }

    protected void gotoActivity(ActivityBase self, Class clazz) {
        Intent intent = new Intent(self, clazz);
        startActivity(intent);
    }

    protected void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public interface TopBarListener{
        void onButtonBackClick();
        String onSetTitle();
        void onButtonOperationClick();
        String onSetOperationText();
    }

    protected void updateProgressMessage(String msg) {
        if(mProgressDialog != null) {
            mProgressDialog.setMessage(msg);
        }
    }

    protected void showProgressDialog(String message) {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
