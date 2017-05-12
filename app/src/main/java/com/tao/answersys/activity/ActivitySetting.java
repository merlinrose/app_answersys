package com.tao.answersys.activity;

import android.view.View;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;

/**
 * Created by LiangTao on 2017/4/25.
 */

public class ActivitySetting extends ActivityBase{
    private final static String TITLE_SETTING = "系统设置";
    private final static String OPERATION_TEXT = "";

    @Override
    protected void init() {
        setContentView(R.layout.activity_setting);

        setOnTopBarListener(new TopBarListener() {
            @Override
            public void onButtonBackClick() {
                finish();
            }

            @Override
            public String onSetTitle() {
                return TITLE_SETTING;
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

    private void initView() {
        findViewById(R.id.setting_btn_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(ActivitySetting.this, ActivityFeedback.class);
            }
        });
    }
}
