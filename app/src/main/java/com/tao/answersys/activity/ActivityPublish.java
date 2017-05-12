package com.tao.answersys.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.bean.Lesson;
import com.tao.answersys.bean.NbQuestionPublish;
import com.tao.answersys.event.ErrorEventPublishPage;
import com.tao.answersys.global.CustApplication;
import com.tao.answersys.view.MessageDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class ActivityPublish extends ActivityBase {
    private final static String TITLE_PUBLISH = "创建问题";
    private final static String OPERATION_TEXT = "提交";

    private EditText mEdittextTitle;
    private EditText mEdittextLesson;
    private EditText mEdittextContent;

    private Lesson mSelectLesson = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INTENT_REQ_LESSONS) {
            if(resultCode == RESULT_CANCELED || resultCode == INTENT_RESULT_CANCEL) {

            } else if(resultCode == INTENT_RESULT_SUC) {
                mSelectLesson = (Lesson) data.getExtras().get("lesson");
                if(mSelectLesson != null) {
                    mEdittextLesson.setText(mSelectLesson.getName());
                }
            }
        }
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_publish);
        EventBus.getDefault().register(this);
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
                String lesson = mEdittextLesson.getText().toString();
                String title = mEdittextTitle.getText().toString();
                String content = mEdittextContent.getText().toString();

                if(lesson == null ||
                        lesson.equals("") ||
                        title == null ||
                        title.equals("") ||
                        content == null ||
                        content.equals("")) {
                    showToastMessage("所有输入项不能为空！！");
                } else {
                    new AsyncTaskPublish().execute(title, content);
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
        mEdittextLesson = (EditText) findViewById(R.id.pulish_edittext_lesson);
        mEdittextContent = (EditText) findViewById(R.id.pulish_edittext_content);
        mEdittextTitle = (EditText) findViewById(R.id.pulish_edittext_title);

        mEdittextLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPublish.this, ActivityLessonSelect.class);
                startActivityForResult(intent, INTENT_REQ_LESSONS);
            }
        });
    }

    private class AsyncTaskPublish extends AsyncTask<String, Long, Boolean> {
        private boolean codeError = false;
        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length == 2) {
                NbQuestionPublish question = new NbQuestionPublish();
                question.setTitle(params[0]);
                question.setLessonId(mSelectLesson.getId());
                question.setContent(params[1]);
                question.setStuId(CustApplication.getCurrUserId());
                return mBizUser.publishQuestion(question);
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
                    final MessageDialog dialog =  new MessageDialog(ActivityPublish.this).setTitle("提示").setMessage("你的问题成功发布");
                    dialog.setOutsideCancelable(false);
                    dialog.setButtonListener(new MessageDialog.OnDialogButtonClickListener() {
                        @Override
                        public void onOkButtonClick() {
                            dialog.dismiss();
                            ActivityPublish.this.finish();
                        }

                        @Override
                        public void onCancelButtonClick() {
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPublishError(ErrorEventPublishPage event) {
        showToastMessage(event.getMsg());
    }
}
