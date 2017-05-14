package com.tao.answersys.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.adapter.AdapterAnswers;
import com.tao.answersys.bean.AnswerItem;
import com.tao.answersys.bean.Question;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.Teacher;
import com.tao.answersys.biz.BizQuestion;
import com.tao.answersys.event.ErrorEventQuestionDetailPage;
import com.tao.answersys.event.EventUserReqUpdateAnswer;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/18.
 */

public class ActivityQuestionDetail extends ActivityBase {
    private final static String TITLE_DETAIL = "问题详情";
    private final static String OPERATION_TEXT = "";

    private int mQuestionId = -1;

    private TextView mTextviewLesson;
    private TextView mTextviewUser;
    private TextView mTextviewCreateDateTime;
    private TextView mTextviewTitle;
    private TextView mTextviewContent;
    private Question mQuestion;
    private TextView mTextviewReadSum;

    private AdapterAnswers mAdapterStuAnswer;
    private AdapterAnswers mAdapterTeacherAnswer;
    private View btnCollect;

    private boolean isCollect;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQ_ANSWER) {
            if (resultCode == INTENT_RESULT_SUC) {
                showToastMessage("发布成功");
                if(CustApplication.getUserType().equals(Config.USER_TYPE_STU)) {
                    new AsyncTaskStuAnswer().execute(mQuestionId);
                } else {
                    new AsyncTaskTeacherAnswer().execute(mQuestionId);
                }
            } else if (resultCode == INTENT_RESULT_CANCEL || resultCode == RESULT_CANCELED) {
                showToastMessage("取消发布");
            }
        } else if(requestCode == INTENT_REQ_UPDATE) {
            if(resultCode == INTENT_RESULT_SUC) {
                showToastMessage("保存成功");
                new AsyncTaskQuestion().execute(mQuestionId);
            }
        } else if(requestCode == INTENT_REQ_UPDATE_ANSER) {
            if(resultCode == INTENT_RESULT_SUC) {
                showToastMessage("修改成功");
                if(CustApplication.getCurrUser() instanceof Student) {
                    new AsyncTaskStuAnswer().execute(mQuestionId);
                } else if(CustApplication.getCurrUser() instanceof Teacher) {
                    new AsyncTaskTeacherAnswer().execute(mQuestionId);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_question_detail);
        mQuestionId = getIntent().getIntExtra("questionId", -1);
        EventBus.getDefault().register(this);

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TITLE_DETAIL;
            }

            @Override
            public void onButtonOperationClick() {
            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });
        initButton();
        initQuestionView();
        initRecyclerView();

        new AsyncTaskQuestion().execute(mQuestionId);
        new AsyncTaskStuAnswer().execute(mQuestionId);
        new AsyncTaskTeacherAnswer().execute(mQuestionId);
    }

    private void initRecyclerView() {
        mAdapterStuAnswer = new AdapterAnswers(this);
        RecyclerView recyclerViewStuAnswer = (RecyclerView) findViewById(R.id.qd_listview_stu_answer);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerViewStuAnswer.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 20, 0, 20);
            }
        });
        recyclerViewStuAnswer.setLayoutManager(layoutManger);
        recyclerViewStuAnswer.setAdapter(mAdapterStuAnswer);

        mAdapterTeacherAnswer = new AdapterAnswers(this);
        RecyclerView.LayoutManager layoutManger1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        RecyclerView recyclerViewTeacherAnswer = (RecyclerView) findViewById(R.id.qd_listview_teacher_answer);
        recyclerViewTeacherAnswer.setLayoutManager(layoutManger1);
        recyclerViewTeacherAnswer.setAdapter(mAdapterTeacherAnswer);
    }

    private void initQuestionView() {
        mTextviewLesson = (TextView) findViewById(R.id.qd_lesson);
        mTextviewTitle = (TextView) findViewById(R.id.qd_title);
        mTextviewContent = (TextView) findViewById(R.id.qd_content);
        mTextviewCreateDateTime = (TextView) findViewById(R.id.qd_date);
        mTextviewUser = (TextView) findViewById(R.id.qd_user);
        mTextviewReadSum = (TextView) findViewById(R.id.qd_readPeople);
    }

    private void loadQuestionData(Question question) {
        mQuestion = question;

        mTextviewLesson.setText(question.getLesson());
        mTextviewTitle.setText(question.getTitle());
        mTextviewContent.setText(question.getContent());
        mTextviewCreateDateTime.setText(question.getCreateDay() + "  " + question.getCreateTime());
        mTextviewUser.setText(question.getUserName());
        switchCollectStatus(question.isCollect());
        mTextviewReadSum.setText((question.getReadSum()+1)+"");

        initUpdateBtn();
    }

    private void initUpdateBtn() {
        View btnUpdate = findViewById(R.id.qd_btn_update);
        if(canUpdate()) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityQuestionDetail.this, ActivityQuestionUpdate.class);
                    Bundle bundle = new Bundle();
                    mQuestion.setId(mQuestionId);
                    bundle.putSerializable("question", mQuestion);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, INTENT_REQ_UPDATE);
                }
            });
        } else {
            btnUpdate.setVisibility(View.GONE);
        }
    }

    private void switchCollectStatus(boolean isCollect) {
        if(isCollect) {
            btnCollect.setBackground(getResources().getDrawable(R.drawable.ic_has_collect, null));
        } else {
            btnCollect.setBackground(getResources().getDrawable(R.drawable.ic_collect, null));
        }
    }

    private void initButton() {
        findViewById(R.id.qd_btn_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityQuestionDetail.this, ActivityAnswer.class);
                intent.putExtra("questionId", mQuestionId);
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", mQuestion);
                intent.putExtras(bundle);
                startActivityForResult(intent, INTENT_REQ_ANSWER);
            }
        });

        btnCollect = findViewById(R.id.qd_btn_collect);
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCollect) {
                    new AsyncTaskCancelCollect().execute(mQuestionId);
                } else {
                    new AsyncTaskCollect().execute(mQuestionId);
                }
            }
        });
    }

    private boolean canUpdate() {
        String userType = CustApplication.getUserType();
        if(userType.equals(Config.USER_TYPE_STU)) {
            if(mQuestion.getStuId() == CustApplication.getCurrUserId()) {
                return true;
            }
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEventQuestionDetailPage error) {
        showToastMessage(error.getMsg());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserRquestUpdateAnswer(EventUserReqUpdateAnswer event) {
        Intent intent = new Intent(ActivityQuestionDetail.this, ActivityAnswer.class);
        intent.putExtra("questionId", mQuestionId);

        Bundle bundle = new Bundle();
        bundle.putSerializable("question", mQuestion);
        intent.putExtras(bundle);

        intent.putExtra("answerId", event.getAnswerId());
        intent.putExtra("oldAnswer", event.getOldAnswer());
        startActivityForResult(intent, INTENT_REQ_UPDATE_ANSER);
    }

    public class AsyncTaskCollect extends AsyncTask<Integer, Void, Boolean> {
        private boolean codeError = false;
        @Override
        protected Boolean doInBackground(Integer... params) {
            if(params.length == 1) {
                return mBizUser.collect(params[0]);
            } else {
                codeError = true;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result == true) {
                showToastMessage("收藏成功");
                switchCollectStatus(true);
                isCollect = true;
            } else if(codeError == true){
                showToastMessage("程序员开小差了");
            } else {
                //showToastMessage("收藏失败");
            }
        }
    }

    public class AsyncTaskStuAnswer extends AsyncTask<Integer, Void, List<AnswerItem>> {
        @Override
        protected List<AnswerItem> doInBackground(Integer... params) {
            if(params.length == 1) {
                return mBizQuestion.getStuAnswer(params[0]);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<AnswerItem> answerItems) {
            super.onPostExecute(answerItems);
            if(answerItems != null) {
                mAdapterStuAnswer.setdata(answerItems);
                mAdapterStuAnswer.notifyDataSetChanged();
            } else {
                showToastMessage("没有任何数据");
            }
        }
    }

    public class AsyncTaskTeacherAnswer extends AsyncTask<Integer, Void, List<AnswerItem>> {
        @Override
        protected List<AnswerItem> doInBackground(Integer... params) {
            if(params.length == 1) {
                return mBizQuestion.getTeacherAnswer(params[0]);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<AnswerItem> answerItems) {
            super.onPostExecute(answerItems);
            if(answerItems != null) {
                mAdapterTeacherAnswer.setdata(answerItems);
                mAdapterTeacherAnswer.notifyDataSetChanged();
            } else {
                showToastMessage("没有任何数据");
            }
        }
    }

    public class AsyncTaskQuestion extends AsyncTask<Integer, String, Question> {
        @Override
        protected Question doInBackground(Integer... params) {
            int questionId = -1;
            if (params.length == 1) {
                questionId = params[0];
            }

            return mBizQuestion.getQuestion(questionId);
        }

        @Override
        protected void onPostExecute(Question question) {
            super.onPostExecute(question);
            if (question != null) {
                loadQuestionData(question);
                isCollect = question.isCollect();
            }
        }
    }

    public class AsyncTaskCancelCollect extends AsyncTask<Integer, String, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            int questionId = -1;
            if (params.length == 1) {
                questionId = params[0];
            }

            return mBizUser.cancelCollect(questionId);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result != null) {
                if(result) {
                    switchCollectStatus(false);
                    isCollect = false;
                } else {

                }
            }
        }
    }
}
