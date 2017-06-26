package com.tao.answersys.frament.base;

import com.tao.answersys.R;
import com.tao.answersys.frament.FragmentNews;
import com.tao.answersys.frament.FragmentMe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 * Fragment的管理者
 * <br>此类为单例类
 */
public class FragmentManager {
    private static FragmentManager mInstance;
    private static String[] mTitles = new String[]{"动态", "我的"};
    private static int[] mIconId = new int[]{R.drawable.ic_tab_news, R.drawable.ic_tab_publish};

    /**
     * 获取单例对象
     * @return
     */
    public static FragmentManager getmInstance() {
        if(mInstance == null) {
            mInstance = new FragmentManager();
        }
        return mInstance;
    }

    private FragmentManager() {
    }

    /**
     * 获取Fragment的title数组
     * @return
     */
    public String[] getTitles() {
        return mTitles;
    }

    /**
     * 获取Fragment的跳转按钮图标ID数组
     * @return
     */
    public int[] getIconId() {
        return mIconId;
    }

    /**
     * 获取Fragment数组
     * @return
     */
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
