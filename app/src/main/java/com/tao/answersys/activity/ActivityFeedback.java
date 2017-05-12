package com.tao.answersys.activity;

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.event.ErrorEventFeedbackPage;
import com.tao.answersys.view.MessageDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class ActivityFeedback extends ActivityBase {
    private final static String FEEDBACK = "用户反馈";
    private final static String OPERATION_TEXT = "提交";

    private EditText eidttextEmail = null;
    private EditText edittextTel = null;
    private EditText edittextContent = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_feedback);
        EventBus.getDefault().register(this);

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return FEEDBACK;
            }

            @Override
            public void onButtonOperationClick() {
                edittextContent = (EditText) findViewById(R.id.feedback_textview_content);
                edittextTel = (EditText) findViewById(R.id.feedback_textview_tel);
                eidttextEmail = (EditText) findViewById(R.id.feedback_textview_email);

                if(edittextContent.getText().toString().trim().equals("")) {
                    showToastMessage("反馈内容不能为空");
                }

                new AsyncTaskFeedBack().execute(edittextContent.getText().toString(),
                        eidttextEmail.getText().toString(),
                        edittextTel.getText().toString());
            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });

        initView();
    }

    private void initView() {
        edittextContent = (EditText) findViewById(R.id.feedback_textview_content);
        edittextTel = (EditText) findViewById(R.id.feedback_textview_tel);
        eidttextEmail = (EditText) findViewById(R.id.feedback_textview_email);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEventFeedbackPage error) {
        showToastMessage(error.getMsg());
    }

    private class AsyncTaskFeedBack extends AsyncTask<String, Void, Boolean> {
        private boolean codeError = false;
        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length == 3) {
                return mBizUser.feedback(params[0], params[1], params[2]);
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
                    final MessageDialog dialog =  new MessageDialog(ActivityFeedback.this).setTitle("提示").setMessage("你的反馈已经提交，我们会做的更好");
                    dialog.setOutsideCancelable(false);
                    dialog.hideCancelButton();
                    dialog.setButtonListener(new MessageDialog.OnDialogButtonClickListener() {
                        @Override
                        public void onOkButtonClick() {
                            dialog.dismiss();
                            ActivityFeedback.this.finish();
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
}
