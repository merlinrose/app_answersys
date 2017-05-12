package com.tao.answersys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.EventNewsItemClick;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 */

public class AdapterNews extends RecyclerView.Adapter<ViewHolderNews>{
    private LayoutInflater mLayoutInflater;
    private List<Question> mData;
    private ViewHolderNews viewHolderNews;

    public AdapterNews(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderNews onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_news_item, null);

        return new ViewHolderNews(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderNews holder, int position) {
        viewHolderNews = holder;
        Question item = mData.get(position);
        holder.bindData(item);
    }

    public void addData(List<Question> data){
        if(null == this.mData) {
            this.mData = new ArrayList<Question>();
        }

        if(data != null) {
            this.mData.addAll(0, data);
        }
    }

    public void setData(List<Question> data){
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


}

class ViewHolderNews extends RecyclerView.ViewHolder{
    private TextView mTextviewLesson;
    private GridView mImageShowView;
    private TextView mTextviewUser;
    private TextView mTextviewCreateDateTime;
    private TextView mTextviewTitle;
    private TextView mTextviewSummary;
    private TextView mTextviewReadSum;

    private View mRootView;

    public GridView getmImageShowView() {
        return mImageShowView;
    }

    public ViewHolderNews(View itemView) {
        super(itemView);
        mRootView = itemView;

        mImageShowView = (GridView) itemView.findViewById(R.id.new_item_imgs);
        mTextviewLesson = (TextView) itemView.findViewById(R.id.news_item_lesson);
        mTextviewCreateDateTime = (TextView) itemView.findViewById(R.id.news_item_date);
        mTextviewUser = (TextView) itemView.findViewById(R.id.news_item_user);
        mTextviewTitle = (TextView) itemView.findViewById(R.id.news_item_title);
        mTextviewSummary = (TextView) itemView.findViewById(R.id.news_item_summary);
        mTextviewReadSum = (TextView) itemView.findViewById(R.id.news_item_read_sum);
    }

    public void bindData(final Question data) {
        AdapterImageShow adapter = new AdapterImageShow(itemView.getContext());
        adapter.setData(data.getImgUrls());
        mImageShowView.setNumColumns(3);
        mImageShowView.setAdapter(adapter);

        mTextviewLesson.setText(data.getLesson());
        mTextviewSummary.setText(data.getSummary());
        mTextviewTitle.setText(data.getTitle());
        mTextviewUser.setText(data.getUserName());
        mTextviewCreateDateTime.setText(data.getCreateDay()+"  " + data.getCreateTime());
        mTextviewReadSum.setText(data.getReadSum()+"");

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = data.getQuestionId();
                EventBus.getDefault().post(new EventNewsItemClick(id));
            }
        });
    }
}



