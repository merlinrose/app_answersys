package com.tao.answersys.frament.base;

import com.tao.answersys.R;
import com.tao.answersys.frament.FragmentNews;
import com.tao.answersys.frament.FragmentMe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 */
public class FragmentManager {
    private static FragmentManager mInstance;
    private static String[] mTitles = new String[]{"动态", "我的"};
    private static int[] mIconId = new int[]{R.drawable.ic_tab_news, R.drawable.ic_tab_publish};

    public static FragmentManager getmInstance() {
        if(mInstance == null) {
            mInstance = new FragmentManager();
        }
        return mInstance;
    }

    private FragmentManager() {
    }

    public String[] getTitles() {
        return mTitles;
    }

    public int[] getIconId() {
        return mIconId;
    }

    public List<FragmentBase> getFragmentList() {
        List<FragmentBase> fragmentList = new ArrayList<FragmentBase>();

        FragmentBase fragmentNews = new FragmentNews();
        fragmentNews.setFragmentLayout(R.layout.fragment_news);
        fragmentNews.setFragmentTitle(mTitles[0]);

        FragmentBase fragmentPublish = new FragmentMe();
        fragmentPublish.setFragmentLayout(R.layout.fragment_me);
        fragmentPublish.setFragmentTitle(mTitles[1]);

        fragmentList.add(fragmentNews);
        fragmentList.add(fragmentPublish);

        return fragmentList;
    }
}
