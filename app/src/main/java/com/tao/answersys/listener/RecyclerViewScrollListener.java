package com.tao.answersys.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;;

/**
 * 
 * @author LiangTao
 *
 */
public abstract class RecyclerViewScrollListener extends OnScrollListener {
	private int reachBottomRow = 1;
	private boolean isInTheBottom = false;

	@Override
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		LayoutManager layoutManager = recyclerView.getLayoutManager();
		if (layoutManager == null) { // it maybe unnecessary
			throw new RuntimeException("LayoutManager is null,Please check it!");
		}
		Adapter adapter = recyclerView.getAdapter();
		if (adapter == null) { // it maybe unnecessary
			throw new RuntimeException("Adapter is null,Please check it!");
		}

		boolean isReachBottom = false;
		// is GridLayoutManager
		if (layoutManager instanceof GridLayoutManager) {
			GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
			int rowCount = adapter.getItemCount() / gridLayoutManager.getSpanCount();
			int lastVisibleRowPosition = gridLayoutManager.findLastVisibleItemPosition()
					/ gridLayoutManager.getSpanCount();
			isReachBottom = (lastVisibleRowPosition >= rowCount - reachBottomRow);
		}
		// is LinearLayoutManager
		else if (layoutManager instanceof LinearLayoutManager) {
			int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
			int rowCount = adapter.getItemCount();
			if (reachBottomRow > rowCount)
				reachBottomRow = 1;
			isReachBottom = (lastVisibleItemPosition >= rowCount - reachBottomRow);
		}
		// is StaggeredGridLayoutManager
		else if (layoutManager instanceof StaggeredGridLayoutManager) {
			StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
			int spanCount = staggeredGridLayoutManager.getSpanCount();
			int[] into = new int[spanCount];
			int[] eachSpanListVisibleItemPosition = staggeredGridLayoutManager.findLastVisibleItemPositions(into);
			for (int i = 0; i < spanCount; i++) {
				if (eachSpanListVisibleItemPosition[i] > adapter.getItemCount() - reachBottomRow * spanCount) {
					isReachBottom = true;
					break;
				}
			}
		}

		if (!isReachBottom) {
			isInTheBottom = false;
		} else if (!isInTheBottom) {
			onLoadMoreData();
			isInTheBottom = true;
		}
	}

	public abstract void onLoadMoreData();
}