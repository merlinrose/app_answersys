package com.tao.answersys.activity;

import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.bean.Student;
import com.tao.answersys.bean.Teacher;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;

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

    @Override
    protected void init() {
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
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.layout_key_value, new String[]{"key", "value"}, new int[]{R.id.kv_key, R.id.kv_value});
        listview.setAdapter(adapter);
    }
}
