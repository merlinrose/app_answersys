package com.tao.answersys.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.bean.QuestionUpdate;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.ErrorEventPublishPage;
import com.tao.answersys.event.ErrorEventQuestionUpdatePage;
import com.tao.answersys.view.MessageDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class ActivityQuestionUpdate extends ActivityBase {
    private final static String TITLE_PUBLISH = "修改问题";
    private final static String OPERATION_TEXT = "保存";

    private EditText mEdittextTitle;
    private EditText mEdittextContent;

    private Question mQuestion;

    @Override
    protected void init() {
        setContentView(R.layout.activity_question_update);
        EventBus.getDefault().register(this);
        mQuestion = (Question) getIntent().getExtras().get("question");
        initView();

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TITLE_PUBLISH;
            }

            @Override
            public void onButtonOperationClick() {
                String title = mEdittextTitle.getText().toString();
                String content = mEdittextContent.getText().toString();

                if( title == null ||
                        title.equals("") ||
                        content == null ||
                        content.equals("")) {
                    showToastMessage("所有输入项不能为空！！");
                } else {
                    new AsyncTaskUpdate().execute(title, content);
                }
            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mEdittextContent = (EditText) findViewById(R.id.qupdate_edittext_content);
        mEdittextContent.setText(mQuestion.getContent());
        mEdittextTitle = (EditText) findViewById(R.id.qupdate_edittext_title);
        mEdittextTitle.setText(mQuestion.getTitle());
        ((TextView)findViewById(R.id.qupdate_textview_lesson)).setText(mQuestion.getLesson());
    }

    private class AsyncTaskUpdate extends AsyncTask<String, Long, Boolean> {
        private boolean codeError = false;
        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length == 2) {
                QuestionUpdate question = new QuestionUpdate();
                question.setTitle(params[0]);
                question.setContent(params[1]);
                question.setId(mQuestion.getId());

                return mBizUser.updateQuestion(question);
            }else {
                codeError = true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(codeError) {
                showToastMessage("程序员开小差了！！");
            } else {
                if(result) {
                    setResult(INTENT_RESULT_SUC);
                    finish();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPublishError(ErrorEventQuestionUpdatePage event) {
        showToastMessage(event.getMsg());
    }
}
