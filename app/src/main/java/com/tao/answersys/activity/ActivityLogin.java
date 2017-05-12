package com.tao.answersys.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.Teacher;
import com.tao.answersys.biz.BizUser;
import com.tao.answersys.event.ErrorEventLoginPage;
import com.tao.answersys.event.ErrorEventMainPage;
import com.tao.answersys.global.CustApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class ActivityLogin extends ActivityBase{
    @Override
    protected void init() {
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        final EditText edittextPwd = (EditText) findViewById(R.id.login_edittext_pwd);
        final EditText edittextAccount = (EditText) findViewById(R.id.login_edittext_account);
        findViewById(R.id.login_button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edittextAccount.getText().toString();
                String pwd = edittextPwd.getText().toString();

                if(account == null || account.equals("") || pwd == null || pwd.equals("")) {
                    showToastMessage("输入项不能为空！");
                } else {
                    AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin();

                    asyncTaskLogin.execute(account, pwd);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginError(ErrorEventLoginPage errorEvent) {
        showToastMessage(errorEvent.getMsg());
    }

    private class AsyncTaskLogin extends AsyncTask<String, Void, Object> {
        private boolean codeError = false;
        @Override
        protected Object doInBackground(String... params) {
            if(params.length == 2) {
                return  mBizUser.login(params[0], params[1]);
            } else {
                codeError = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(o != null) {
                if(o instanceof Teacher) {
                    CustApplication.setCurrentUser(false, o);
                } else if(o instanceof Student) {
                    CustApplication.setCurrentUser(true, o);
                }
                gotoActivity(ActivityLogin.this, ActivityMain.class);
                finish();
            } else {
                if(codeError) {
                    showToastMessage("程序员开小差了！！");
                }
            }
        }
    }
}
