package com.tao.answersys.activity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.Teacher;
import com.tao.answersys.event.ErrorEventLoginPage;
import com.tao.answersys.global.CustApplication;

import net.qiujuer.genius.blur.StackBlur;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;

import static android.view.WindowManager.*;

/**
 * Created by LiangTao on 2017/4/25.
 * 登录Activity
 */
public class ActivityLogin extends ActivityBase{
    /**
     * 初始化
     */
    @Override
    protected void init() {
        //设置状态栏透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);

        initBackground();
        initView();
    }

    /**
     * 初始化页面背景
     */
    private void initBackground() {
        AssetManager assetManager = getResources().getAssets();
        InputStream is = null;
        try {
            is = assetManager.open("login_bg.jpg");
            Bitmap bg = BitmapFactory.decodeStream(is);
            Bitmap newBitmap = StackBlur.blurNatively(bg, 45, false);
            getWindow().getDecorView().setBackground(new BitmapDrawable(newBitmap));
        } catch (Exception e) {
            Toast.makeText(this, "登录背景设置失败", Toast.LENGTH_SHORT).show();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化View
     */
    private void initView() {
        final EditText edittextPwd = (EditText) findViewById(R.id.login_edittext_pwd);
        final EditText edittextAccount = (EditText) findViewById(R.id.login_edittext_account);
        edittextAccount.setText("201307040313");
        edittextPwd.setText("admin");
        findViewById(R.id.login_button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edittextAccount.getText().toString();
                String pwd = edittextPwd.getText().toString();

                if(account == null || account.equals("") || pwd == null || pwd.equals("")) {
                //    AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin();
                //    asyncTaskLogin.execute("201307040313", "admin");
                    showPromptMessage("输入项不能为空！");
                } else {
                    AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin();
                    asyncTaskLogin.execute(account, pwd);
                }
            }
        });
    }

    /**
     * 错误信息事件接收者
     * @param errorEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginError(ErrorEventLoginPage errorEvent) {
        showPromptMessage(errorEvent.getMsg());
    }

    /**
     * 登录异步任务类
     */
    private class AsyncTaskLogin extends AsyncTask<String, Void, Object> {
        private boolean codeError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("正在登录中");
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(params.length == 2) {
                return  mBizUser.login(params[0], params[1]);
            } else {
                codeError = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            dismissProgressDialog();
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
                    showPromptMessage("程序员开小差了！！");
                }
            }
        }
    }
}
