package com.tao.answersys.frament.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;

/**
 * Created by LiangTao on 2017/4/12.
 * Fragment基类
 */
public abstract class FragmentBase extends Fragment{
    private String mFragmentTitle = null;
    private int mLayoutId = -1;
    private View mFrgmentView = null;

    public FragmentBase() {
        super();
    }

    /**
     * 设置Fragment的Layout的id
     * @param layoutId
     */
    public void setFragmentLayout(int layoutId) {
        this.mLayoutId = layoutId;
    }

    /**
     * 设置Fragment的Title
     * @param title
     */
    public void setFragmentTitle(String title) {
        mFragmentTitle = title;
    }

    /**
     * 获取Fragment的Title
     * @return
     */
    public String getFragmentTitle() {
        return mFragmentTitle;
    }

    /**
     * 初始化
     */
    public abstract void init();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if(-1 != mLayoutId) {
           mFrgmentView = inflater.inflate(mLayoutId, null);
       }

        init();
        return mFrgmentView;
    }

    /**
     * 通过ID获取View
     * @param viewId
     * @return
     */
    public View findViewById(int viewId) {
        if(null != mFrgmentView) {
            return mFrgmentView.findViewById(viewId);
        } else {
            return null;
        }
    }

    /**
     * 跳转Activity
     * @param context
     * @param clazz
     */
    public void gotoActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }
}
