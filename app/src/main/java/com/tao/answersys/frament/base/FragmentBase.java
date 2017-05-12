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
 */

public abstract class FragmentBase extends Fragment{
    private String mFragmentTitle = null;
    private int mLayoutId = -1;
    private View mFrgmentView = null;

    public FragmentBase() {
        super();
    }

    public void setFragmentLayout(int layoutId) {
        this.mLayoutId = layoutId;
    }

    public void setFragmentTitle(String title) {
        mFragmentTitle = title;
    }

    public String getFragmentTitle() {
        return mFragmentTitle;
    }

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

    public View findViewById(int viewId) {
        if(null != mFrgmentView) {
            return mFrgmentView.findViewById(viewId);
        } else {
            return null;
        }
    }

    public void gotoActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }
}
