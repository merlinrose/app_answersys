package com.tao.answersys.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tao.answersys.R;
import com.tao.answersys.activity.base.ActivityBase;
import com.tao.answersys.adapter.AdapterViewPager;
import com.tao.answersys.event.ErrorEventMainPage;
import com.tao.answersys.event.EventNewsItemClick;
import com.tao.answersys.frament.base.FragmentManager;
import com.tao.answersys.global.Config;
import com.tao.answersys.global.CustApplication;

import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ActivityMain extends ActivityBase {
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;

    private View mViewNewMsg;

    private TextView mTextviewTitle;

    protected void init() {
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        initViewPager();
        //initNavigation();
        initButton();

        new AsyncTaskCheckMsg().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initButton() {
        //初始化创建问题按钮
        View tabAdd = findViewById(R.id.main_tab_add);
        View buttonAdd = findViewById(R.id.main_tab_button_add);
        if(CustApplication.getUserType().equals(Config.USER_TYPE_STU)) {
            buttonAdd.setVisibility(View.VISIBLE);
            tabAdd.setVisibility(View.VISIBLE);
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoActivity(ActivityMain.this, ActivityPublish.class);
                }
            });
        } else {
            tabAdd.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.GONE);
        }

        final TextView textviewTitle = (TextView) findViewById(R.id.toolbar_title);
        textviewTitle.setText(mFragmentManager.getTitles()[0]);
        //初始化动态按钮
        findViewById(R.id.main_tab_dynamic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                textviewTitle.setText(mFragmentManager.getTitles()[0]);
            }
        });

        //初始化我的按钮
        findViewById(R.id.main_tab_me).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                textviewTitle.setText(mFragmentManager.getTitles()[1]);
            }
        });

        mViewNewMsg = findViewById(R.id.toolbar_msg_new);
        findViewById(R.id.toolbar_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(ActivityMain.this, ActivityMessage.class);
                mViewNewMsg.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.toolbar_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(ActivityMain.this, ActivitySearch.class);
            }
        });
    }

    private void initViewPager() {
        mTextviewTitle = (TextView) findViewById(R.id.toolbar_title);
        mFragmentManager = FragmentManager.getmInstance();

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager());
        adapterViewPager.setmFragmentList(mFragmentManager.getFragmentList());
        
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTextviewTitle.setText(FragmentManager.getmInstance().getTitles()[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /* TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        int tabCount = tabLayout.getTabCount();
        int[] iconId = mFragmentManager.getIconId();
        String[] titles = mFragmentManager.getTitles();
        for(int i = 0; i < tabCount; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_icon);
            tabIcon.setImageDrawable(getResources().getDrawable(iconId[i], null));

            TextView tabTitle = (TextView) view.findViewById(R.id.tab_title);
            tabTitle.setText(titles[i]);

            tabLayout.getTabAt(i).setCustomView(view);
        }
        */
    }
/*
    private void initNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "点击了抽屉头", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
 */

   @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEventMainPage errorEvent) {
        showPromptMessage("遇到错误：" + errorEvent.getMsg());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(EventNewsItemClick event) {
        Intent intent = new Intent(ActivityMain.this, ActivityQuestionDetail.class);
        intent.putExtra("questionId", event.getQuestionId());
        startActivity(intent);
    }

    private class AsyncTaskCheckMsg extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return mBizUser.checkMsg();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result != null && result) {
                mViewNewMsg.setVisibility(View.VISIBLE);
            } else {
                mViewNewMsg.setVisibility(View.GONE);
            }
        }
    }
}
