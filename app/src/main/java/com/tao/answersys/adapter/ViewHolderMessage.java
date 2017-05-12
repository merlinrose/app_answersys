package com.tao.answersys.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.bean.Message;

class ViewHolderMessage extends RecyclerView.ViewHolder{
    private TextView mTextviewTitle;
    private TextView mTextviewContent;
    private TextView mTextviewTime;

    private View mRootView;

    public ViewHolderMessage(View itemView) {
        super(itemView);
        mRootView = itemView;

        mTextviewContent = (TextView) itemView.findViewById(R.id.message_item_content);
        mTextviewTime = (TextView) itemView.findViewById(R.id.message_item_time);
        mTextviewTitle = (TextView) itemView.findViewById(R.id.message_item_title);
    }

    public void bindData(final Message data) {
        mTextviewContent.setText(data.getContent());
        mTextviewTitle.setText(data.getTitle());
        mTextviewTime.setText(data.getTime());
    }
}



