package com.tao.answersys.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Created by LiangTao on 2017/4/17.
 */

public class CustTabLayout extends TabLayout{
    public CustTabLayout(Context context) {
        super(context);
    }

    public CustTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
