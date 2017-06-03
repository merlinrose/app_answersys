package com.tao.answersys.activity;

import android.view.View;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.view.MessageDialog;

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

        findViewById(R.id.setting_btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPromptMessage("已经是最新版本了");
            }
        });

        findViewById(R.id.setting_btn_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog dialog = new MessageDialog(ActivitySetting.this);
                dialog.setTitle("关于软件").setMessage("此软件为软件1303班梁涛的毕设作品！！");
                dialog.hideCancelButton();
                dialog.show();
            }
        });
    }
}
