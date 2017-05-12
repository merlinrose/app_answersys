package com.tao.answersys.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tao.answersys.R;

/**
 * Created by LiangTao on 2017/4/13.
 */

public class RecyclerviewDivider extends RecyclerView.ItemDecoration{
    private Paint mPaint;
    private int mHeight;

    public RecyclerviewDivider(Context context, int height) {
        this.mPaint = mPaint;
        mHeight = height;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.colorPrimaryLight));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCouont = parent.getChildCount();

        for(int i = 0; i < childCouont; i++) {
            final View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            final int top = childView.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mHeight;

            if(mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }

        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //设置协调尺寸，outRect应该是ITEM
        outRect.set(0, 0, 0, mHeight);
    }
}
