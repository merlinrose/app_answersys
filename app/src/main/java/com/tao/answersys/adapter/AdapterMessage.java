package com.tao.answersys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tao.answersys.R;
import com.tao.answersys.bean.Message;
import com.tao.answersys.bean.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiangTao on 2017/4/13.
 */

public class AdapterMessage extends RecyclerView.Adapter<ViewHolderMessage>{
    private LayoutInflater mLayoutInflater;
    private List<Message> mData;
    private ViewHolderMessage viewHolderMessage;

    public AdapterMessage(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderMessage onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_message_item, null);

        return new ViewHolderMessage(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderMessage holder, int position) {
        viewHolderMessage = holder;
        Message item = mData.get(position);
        holder.bindData(item);
    }

    public void addData(List<Message> data){
        if(null == this.mData) {
            this.mData = new ArrayList<Message>();
        }

        if(data != null) {
            this.mData.addAll(0, data);
        }
    }

    public void setData(List<Message> data){
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


}



