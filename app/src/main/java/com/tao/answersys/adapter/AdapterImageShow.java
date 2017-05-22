package com.tao.answersys.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tao.answersys.R;
import com.tao.answersys.biz.BizQuestion;
import com.tao.answersys.frament.base.FragmentBase;
import com.tao.answersys.presenter.news.PresenterImageShow;
import com.tao.answersys.presenter.news.PresenterViewImageShow;
import com.tao.answersys.presenter.news.impl.PresenterImageShowImpl;
import com.tao.answersys.utils.ImageLoader;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/15.
 */

public class AdapterImageShow extends BaseAdapter implements PresenterViewImageShow {
    private String[] mImgUrls;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private PresenterImageShow mPresenterImageShow;

    public AdapterImageShow(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public void initPresenter() {
        mPresenterImageShow = new PresenterImageShowImpl(new BizQuestion(), this);
    }

    public void setData(String[] imgUrls) {
        this.mImgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return mImgUrls == null ? 0 : mImgUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return mImgUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_news_item_image, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.news_item_image_item);
            viewHolder.setImageView(imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ImageView imageView = viewHolder.getImageView();
        ImageLoader.getInstance().loadImageAsync(mImgUrls[position], imageView);

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}


