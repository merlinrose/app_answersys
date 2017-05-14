package com.tao.answersys.adapter;

import android.content.Context;
import android.graphics.Path;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tao.answersys.R;
import com.tao.answersys.bean.AnswerItem;
import com.tao.answersys.event.EventUserReqDeleteAnswer;
import com.tao.answersys.event.EventUserReqUpdateAnswer;
import com.tao.answersys.global.CustApplication;
import com.tao.answersys.view.OptionDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/29.
 */

public class AdapterAnswers extends RecyclerView.Adapter<ViewHolderAnswer>{
    private List<AnswerItem> mDatas;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AdapterAnswers(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setdata(List<AnswerItem> data) {
        this.mDatas = data;
    }
    @Override
    public ViewHolderAnswer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_answer_item, null);
        return new ViewHolderAnswer(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderAnswer holder, int position) {
        holder.bindData(mDatas.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}

class ViewHolderAnswer extends RecyclerView.ViewHolder{
    private TextView textviewUserName;
    private TextView textviewDateTime;
    private TextView textviewCotent;
    private View btnEdit;

    public ViewHolderAnswer(View itemView) {
        super(itemView);
        textviewUserName = (TextView) itemView.findViewById(R.id.answer_item_user);
        textviewCotent = (TextView) itemView.findViewById(R.id.answer_item_content);
        textviewDateTime = (TextView) itemView.findViewById(R.id.answer_item_date);
        btnEdit = itemView.findViewById(R.id.answer_item_btn_edit);
    }

    public void bindData(final AnswerItem item, final Context context) {
        textviewUserName.setText(item.getUserName());
        textviewCotent.setText(item.getContent());
        textviewDateTime.setText(item.getDateTime());

        if(item.getUserId() == CustApplication.getCurrUserId() && item.getUserType().equals(CustApplication.getUserType())) {
            btnEdit.setVisibility(View.VISIBLE);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OptionDialog optionDialog = new OptionDialog(context, new String[]{"修改", "删除"});
                    optionDialog.setTitle("编辑").show();

                    optionDialog.setOptionListener(new OptionDialog.OnOptionClickListener() {
                        @Override
                        public void onOptionButtonClick(int optionId) {
                            if(optionId == 0) {
                                optionDialog.dismiss();
                                EventBus.getDefault().post(new EventUserReqUpdateAnswer(item.getId(), item.getContent()));
                            } else if(optionId == 1){
                                optionDialog.dismiss();
                                EventBus.getDefault().post(new EventUserReqDeleteAnswer(item.getId()));
                            }
                        }
                    });
                }
            });
        } else {
            btnEdit.setVisibility(View.GONE);
        }
    }
}
