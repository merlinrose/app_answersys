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
 * Activity基类
 */
public abstract class ActivityBase extends AppCompatActivity{
    //定义了一些常量
    public final static int ITEM_DIVIDER_HEIGHT = 40;
    protected final static int INTENT_RESULT_CANCEL = 0;
    protected final static int INTENT_RESULT_SUC = 1;
    protected final static int INETNT_RESULT_FAIL = 2;

    protected final static int INTENT_REQ_ANSWER = 3;
    protected final static int INTENT_REQ_LESSONS = 4;
    protected final static int INTENT_REQ_UPDATE = 5;
    protected final static int INTENT_REQ_UPDATE_ANSER = 6;
    protected final static int INTENT_REQ_PHOTO = 7;
    private final static String TAG = "ActivityBase";

    private TopBarListener mTopBarListener;

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

    /**
     * 初始化服务类
     */
    private void initBiz() {
        mBizQuestion = new BizQuestion();
        mBizUser = new BizUser();
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar(){
        try {
            //返回按钮初始化
            findViewById(R.id.top_bar_button_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.log(TAG, "Back Button has been Clicked");
                    if (mTopBarListener != null) {
                        mTopBarListener.onButtonBackClick();
                    }
                }
            });

            //操作按钮初始化
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

    /**
     * 初始化
     */
    protected abstract  void init();

    /**
     * 设置顶部栏点击监听器
     * @param listener
     */
    public void setOnTopBarListener(TopBarListener listener) {
        this.mTopBarListener = listener;
    }

    /**
     * 跳转Activity
     * @param self
     * @param clazz
     */
    protected void gotoActivity(ActivityBase self, Class clazz) {
        Intent intent = new Intent(self, clazz);
        startActivity(intent);
    }

    /**
     * 显示提示信息
     * @param message
     */
    protected void showPromptMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 顶部栏事件监听器接口类
     */
    public interface TopBarListener{
        void onButtonBackClick();
        String onSetTitle();
        void onButtonOperationClick();
        String onSetOperationText();
    }

    /**
     * 更新进度弹框信息
     * @param msg
     */
    protected void updateProgressMessage(String msg) {
        if(mProgressDialog != null) {
            mProgressDialog.setMessage(msg);
        }
    }

    /**
     * 显示进度弹框
     * @param message
     */
    protected void showProgressDialog(String message) {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    /**
     * 消失进度弹框
     */
    protected void dismissProgressDialog() {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
