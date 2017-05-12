package com.tao.answersys.frament;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.activity.ActivityCollect;
import com.tao.answersys.activity.ActivityLogin;
import com.tao.answersys.activity.ActivityPersonData;
import com.tao.answersys.activity.ActivitySetting;
import com.tao.answersys.activity.ActivityUserAnswer;
import com.tao.answersys.activity.ActivityUserQuestion;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.Teacher;
import com.tao.answersys.frament.base.FragmentBase;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;

/**
 * Created by LiangTao on 2017/4/12.
 */

public class FragmentMe extends FragmentBase{

    @Override
    public void init() {
        initUserData();
        initButton();
    }

    private void initUserData() {
        Object currUser = CustApplication.getCurrUser();
        TextView userName = (TextView)findViewById(R.id.frag_me_user_name);
        TextView userRange = (TextView) findViewById(R.id.frag_me_range);
        TextView userExp = (TextView) findViewById(R.id.frag_me_exp);

        if(currUser instanceof Student) {
            Student student = (Student)currUser;
            userRange.setText(student.getRange());
            userName.setText(student.getName());
            userExp.setText((student.getAexp() + student.getQexp())+"");
        } else if(currUser instanceof Teacher) {
            Teacher teacher = (Teacher) currUser;
            userRange.setText(teacher.getRange());
            userExp.setText(teacher.getAexp()+"");
            userName.setText(teacher.getName());
        }
    }

    private void initButton() {
        FragMeBtnClickListener listener = new FragMeBtnClickListener();

        View buttonQuestoin = findViewById(R.id.frag_me_btn_question);

        if(CustApplication.getUserType().equals(Config.USER_TYPE_STU)) {
            buttonQuestoin.setOnClickListener(listener);
            buttonQuestoin.setVisibility(View.VISIBLE);
        } else {
            buttonQuestoin.setVisibility(View.GONE);
        }
        findViewById(R.id.frag_me_btn_answer).setOnClickListener(listener);
        findViewById(R.id.frag_me_btn_collect).setOnClickListener(listener);

        findViewById(R.id.setting_person_data).setOnClickListener(listener);
        findViewById(R.id.setting_theme).setOnClickListener(listener);
        findViewById(R.id.setting_setting).setOnClickListener(listener);

        //注销按钮
        findViewById(R.id.frag_me_logout).setOnClickListener(listener);
    }

    private class FragMeBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.frag_me_logout:
                    gotoActivity(getActivity(), ActivityLogin.class);
                    CustApplication.logout();
                    getActivity().finish();
                    break;
                case R.id.frag_me_btn_question:
                    gotoActivity(getActivity(), ActivityUserQuestion.class);
                    break;
                case R.id.frag_me_btn_answer:
                    gotoActivity(getActivity(), ActivityUserAnswer.class);
                    break;
                case R.id.frag_me_btn_collect:
                    gotoActivity(getActivity(), ActivityCollect.class);
                    break;
                case R.id.setting_person_data:
                    gotoActivity(getActivity(), ActivityPersonData.class);
                    break;
                case R.id.setting_theme:
                    break;
                case R.id.setting_setting:
                    gotoActivity(getActivity(), ActivitySetting.class);
                    break;
            }
        }
    }
}
