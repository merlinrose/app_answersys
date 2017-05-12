package com.tao.answersys.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tao.answersys.frament.base.FragmentBase;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/30.
 */
public class AdapterViewPager extends FragmentPagerAdapter {
    private List<FragmentBase> mFragmentList = null;
    public AdapterViewPager(FragmentManager fm) {
        super(fm);
    }

    public void setmFragmentList(List<FragmentBase> fragmentList) {
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getFragmentTitle();
    }
}
