package com.tao.answersys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.bean.Question;
import com.tao.answersys.event.EventUserAnswerItemClick;
import com.tao.answersys.event.EventUserCollectItemClick;
import com.tao.answersys.event.EventUserQuestionItemClick;
import com.tao.answersys.event.EventUserSearchResultItemClick;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 */

public class AdapterQuestions extends RecyclerView.Adapter<ViewHolderQuestion>{
    private LayoutInflater mLayoutInflater;
    private List<Question> mData;
    private ViewHolderQuestion viewHolderNews;

    public AdapterQuestions(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderQuestion onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_question_item, null);

        return new ViewHolderQuestion(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderQuestion holder, int position) {
        viewHolderNews = holder;
        Question item = mData.get(position);
        holder.bindData(item);
    }

    public void addData(List<Question> data){
        if(null == this.mData) {
            this.mData = new ArrayList<Question>();
        }

        if(data != null) {
            this.mData.addAll(data);
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

class ViewHolderQuestion extends RecyclerView.ViewHolder{
    private TextView mTextviewLesson;
    private TextView mTextviewUser;
    private TextView mTextviewCreateDateTime;
    private TextView mTextviewTitle;
    private TextView mTextviewSummary;

    private View mRootView;

    public ViewHolderQuestion(View itemView) {
        super(itemView);
        mRootView = itemView;

        mTextviewUser = (TextView) itemView.findViewById(R.id.qi_user);
        mTextviewLesson = (TextView) itemView.findViewById(R.id.qi_lesson);
        mTextviewSummary = (TextView) itemView.findViewById(R.id.qi_summary);
        mTextviewCreateDateTime = (TextView) itemView.findViewById(R.id.qi_datetime);
        mTextviewTitle = (TextView) itemView.findViewById(R.id.qi_title);
    }

    public void bindData(final Question data) {
        mTextviewUser.setText(data.getUserName());
        mTextviewLesson.setText(data.getLesson());
        mTextviewSummary.setText(data.getSummary());
        mTextviewTitle.setText(data.getTitle());
        mTextviewCreateDateTime.setText(data.getCreateDay() + "  " + data.getCreateTime());

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = data.getQuestionId();
                EventBus.getDefault().post(new EventUserQuestionItemClick(id));
                EventBus.getDefault().post(new EventUserAnswerItemClick(id));
                EventBus.getDefault().post(new EventUserCollectItemClick(id));
                EventBus.getDefault().post(new EventUserSearchResultItemClick(id));
            }
        });
    }
}

