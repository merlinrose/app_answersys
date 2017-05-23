package com.tao.answersys.activity;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.ErrorEventAnswerPage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by LiangTao on 2017/4/29.
 */

public class ActivityAnswer extends ActivityBase{
    private final static String TITLE_ANSWER = "回答问题";
    private final static String TITLE_UPDATE_ANSWER = "修改回答";
    private final static String OPERATION_TEXT = "提交";
    private int mQuestionId;
    private String mOldAnswer;
    private int mAnswerId;

    private TextView mTextviewLesson;
    private TextView mTextviewUser;
    private TextView mTextviewCreateDateTime;
    private TextView mTextviewTitle;
    private TextView mTextviewContent;
    private EditText mEdittextContent;

    @Override
    public void onBackPressed() {
        setResult(INTENT_RESULT_CANCEL);
        super.onBackPressed();
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_answer);
        mQuestionId = getIntent().getIntExtra("questionId", -1);
        mAnswerId = getIntent().getIntExtra("answerId", -1);
        mOldAnswer = getIntent().getStringExtra("oldAnswer");

        EventBus.getDefault().register(this);

        setOnTopBarListener(new ActivityBase.TopBarListener() {
            @Override
            public void onButtonBackClick() {
                setResult(INTENT_RESULT_CANCEL);
                finish();
            }

            @Override
            public String onSetTitle() {
                if(mAnswerId != -1) {
                    return TITLE_UPDATE_ANSWER;
                } else {
                    return TITLE_ANSWER;
                }
            }

            @Override
            public void onButtonOperationClick() {
                if(mTextviewContent != null) {
                    String content = mEdittextContent.getText().toString();
                    if(mAnswerId != -1) {
                        new AsyncUpdateAnswer(content).execute(mAnswerId);
                    } else {
                        new AsyncAnswer(content).execute(mQuestionId);
                    }

                } else {
                    showToastMessage("回答内容不能为空");
                }
            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });

        initQuestionView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initQuestionView() {
        mTextviewLesson = (TextView) findViewById(R.id.answer_lesson);
        mTextviewTitle = (TextView) findViewById(R.id.answer_title);
        mTextviewContent = (TextView) findViewById(R.id.answer_question);
        mTextviewCreateDateTime = (TextView) findViewById(R.id.answer_date);
        mTextviewUser = (TextView) findViewById(R.id.answer_user);
        mEdittextContent = (EditText) findViewById(R.id.answer_content);

        loadQuestionData();
    }

    private void loadQuestionData() {
        Question question = (Question) getIntent().getExtras().getSerializable("question");

        mTextviewLesson.setText(question.getLesson());
        mTextviewTitle.setText(question.getTitle());
        mTextviewContent.setText(question.getContent());
        mTextviewCreateDateTime.setText(question.getCreateDay() + "  " + question.getCreateTime());
        mTextviewUser.setText(question.getUserName());

        if(mOldAnswer != null) {
            mEdittextContent.setText(mOldAnswer);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginError(ErrorEventAnswerPage errorEvent) {
        showToastMessage(errorEvent.getMsg());
    }

    private class AsyncUpdateAnswer extends AsyncTask<Integer, Void, Boolean>{
        private String answer;
        private boolean codeError = false;

        public AsyncUpdateAnswer(String answer) {
            this.answer = answer;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            if(params.length == 1) {
                return mBizUser.updateAnswer(answer, params[0]);
            } else {
                codeError = true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(codeError == true) {
                showToastMessage("程序员开小差了！！");
                return;
            }

            if(result) {
                setResult(INTENT_RESULT_SUC);
                finish();
            } else {
                // showToastMessage("发表失败，点击重试");
            }
        }
    }

    private class AsyncAnswer extends AsyncTask<Integer, Void, Boolean>{
        private String answer;
        private boolean codeError = false;

        public AsyncAnswer(String answer) {
            this.answer = answer;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            if(params.length == 1) {
                return mBizUser.answer(answer, params[0]);
            } else {
                codeError = true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(codeError == true) {
                showToastMessage("程序员开小差了！！");
                return;
            }

            if(result) {
                setResult(INTENT_RESULT_SUC);
                finish();
            } else {
               // showToastMessage("发表失败，点击重试");
            }
        }
    }
}
