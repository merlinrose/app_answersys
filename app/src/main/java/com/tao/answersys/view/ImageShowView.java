package com.tao.answersys.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

/**
 * Created by LiangTao on 2017/4/15.
 * 失败
 */

public class ImageShowView extends AdapterView  {
    private Adapter mAdapter;

    private int mColumnCount = 3;
    private int mRowCount = 0;
    private int mColumnDividerWidth = 0;
    private int mRowDividerWidth = 0;

    public ImageShowView(Context context) {
        super(context);
        init();
    }

    public ImageShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ImageShowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }

    private void initChild() {
        int childViewCount = mAdapter == null ? 0 : mAdapter.getCount();

        for(int i = 0; i < childViewCount; i++) {
            addView(mAdapter.getView(i, null, null));
        }

        final int actuColums = childViewCount < mColumnCount ? childViewCount : mColumnCount;
        final int actuRows = childViewCount / mColumnCount + ((childViewCount % mColumnCount) == 0 ? 0 : 1);

        mColumnDividerWidth = (getMeasuredWidth() - getPaddingRight() - getPaddingLeft()) / actuColums;
        mRowDividerWidth = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / actuRows;
    }

    public void setAdapter(Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        int width = 0;
        int height = 0;
        final int actuColums = childCount < mColumnCount ? childCount : mColumnCount;

        if(actuColums > 0) {
            View childView = getChildAt(0);
            MarginLayoutParams childLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
            width = (childLayoutParams.leftMargin + childView.getMeasuredWidth() + childLayoutParams.rightMargin) * actuColums;
            width += mColumnDividerWidth * (actuColums - 1);

            final int actuRows = childCount / mColumnCount + ((childCount % mColumnCount) == 0 ? 0 : 1);
            height = (childLayoutParams.topMargin + childLayoutParams.bottomMargin + childView.getHeight()) * actuRows;
            height += mRowDividerWidth * (actuRows - 1);
        }

     /*   measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));*/
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        final int actuColums = childCount < mColumnCount ? childCount : mColumnCount;
        final int actuRows = childCount / mColumnCount + ((childCount % mColumnCount) == 0 ? 0 : 1);

        int left = 0;
        int right = 0;
        int bottom  = 0;
        int top = 0;
        for(int i = 0; i < childCount; i++){
            View childView = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            if(((i+1) % mColumnCount) == 1) {
                left = 0;
                top += layoutParams.topMargin;
                bottom += childView.getHeight();
            }

            left += layoutParams.leftMargin;
            right = left + childView.getWidth();
            childView.layout(left, top, right, bottom);
            left += childView.getWidth();

            if(((i+1) % mColumnCount) == 1) {
                top += childView.getHeight();
            }
        }
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int mColumnCount) {
        this.mColumnCount = mColumnCount;
    }

    public int getRowCount() {
        return mRowCount;
    }

    public void setRowCount(int mRowCount) {
        this.mRowCount = mRowCount;
    }

    public int getDividerWidth() {
        return mColumnDividerWidth;
    }

    public void setDividerWidth(int mDividerWidth) {
        this.mColumnDividerWidth = mDividerWidth;
    }
}
