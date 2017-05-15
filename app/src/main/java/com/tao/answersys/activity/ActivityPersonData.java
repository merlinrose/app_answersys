package com.tao.answersys.activity;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.Teacher;
import com.tao.answersys.event.ErrorEventPersonDataPage;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class ActivityPersonData extends ActivityBase {
    private final static String TITLE_PERSON_DATA = "个人资料";
    private final static String OPERATION_TEXT = "";

    private EditText mEdittextOldPwd;
    private EditText mEdittextNewPwd;
    private EditText mEditetxtConfirmPwd;
    private View mBtnChangPwd;

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_person_data);

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TITLE_PERSON_DATA;
            }

            @Override
            public void onButtonOperationClick() {

            }

            @Override
            public String onSetOperationText() {
                return OPERATION_TEXT;
            }
        });
        
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        ListView listview = (ListView) findViewById(R.id.person_data_listview);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        if(CustApplication.getUserType().equals(Config.USER_TYPE_STU)) {
            Student stu = (Student)CustApplication.getCurrUser();

            Map<String, String> mapName = new HashMap<String, String>();
            mapName.put("key", "姓名");
            mapName.put("value", stu.getName());
            data.add(mapName);

            Map<String, String> mapGender = new HashMap<String, String>();
            mapGender.put("key", "性别");
            mapGender.put("value", stu.getGender());
            data.add(mapGender);

            Map<String, String> mapNum = new HashMap<String, String>();
            mapNum.put("key", "学号");
            mapNum.put("value", stu.getNum());
            data.add(mapNum);

            Map<String, String> mapClazz = new HashMap<String, String>();
            mapClazz.put("key", "班级");
            mapClazz.put("value", stu.getClassName());
            data.add(mapClazz);

            Map<String, String> mapMajor = new HashMap<String, String>();
            mapMajor.put("key", "专业");
            mapMajor.put("value", stu.getMajorName());
            data.add(mapMajor);
        } else if(CustApplication.getUserType().equals(Config.USER_TYPE_TEACHER)) {
            Teacher teacher = (Teacher)CustApplication.getCurrUser();

            Map<String, String> mapName = new HashMap<String, String>();
            mapName.put("key", "姓名");
            mapName.put("value", teacher.getName());
            data.add(mapName);

            Map<String, String> mapNum = new HashMap<String, String>();
            mapNum.put("key", "工号");
            mapNum.put("value", teacher.getNum());
            data.add(mapNum);

            Map<String, String> mapGender = new HashMap<String, String>();
            mapGender.put("key", "性别");
            mapGender.put("value", teacher.getGender());
            data.add(mapGender);

            Map<String, String> mapLessons = new HashMap<String, String>();
            mapLessons.put("key", "教授课程");
            mapLessons.put("value", teacher.getTeachLesson());
            data.add(mapLessons);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.layout_key_value, new String[]{"key", "value"}, new int[]{R.id.kv_key, R.id.kv_value});
        listview.setAdapter(adapter);

        mEdittextOldPwd = (EditText) findViewById(R.id.person_data_old_pwd);
        mEdittextNewPwd = (EditText) findViewById(R.id.person_data_new_pwd);
        mEditetxtConfirmPwd = (EditText) findViewById(R.id.person_data_confirm_pwd);
        mBtnChangPwd = findViewById(R.id.person_data_btn_change_pwd);
        mBtnChangPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = mEdittextOldPwd.getText().toString();
                String newPwd = mEdittextNewPwd.getText().toString();
                String confirmPwd = mEditetxtConfirmPwd.getText().toString().trim();

                mEditetxtConfirmPwd.setText("");
                mEdittextNewPwd.setText("");
                mEdittextOldPwd.setText("");
                if(oldPwd == null || oldPwd.trim().equals("") || newPwd == null || newPwd.trim().equals("")) {
                    showToastMessage("输入项不能为空");
                } else {
                    if(newPwd.equals(confirmPwd)) {
                        new AsyncTaskChgnePwd().execute(oldPwd, newPwd);
                    } else {
                        showToastMessage("两次密码不一致");
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEventPersonDataPage error) {
        showToastMessage(error.getMsg());
    }

    private class AsyncTaskChgnePwd extends AsyncTask<String, Void, Boolean> {
        private boolean codeError = false;
        @Override
        protected Boolean doInBackground(String... params) {
            if(params.length == 2) {
                return  mBizUser.changePwd(params[0], params[1]);
            } else {
                codeError = true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean o) {
            if(o != null && o) {
                showToastMessage("修改成功");
            } else {
                if(codeError) {
                    showToastMessage("程序员开小差了！！");
                }
            }
        }
    }
}
