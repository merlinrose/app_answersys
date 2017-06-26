package com.tao.answersys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tao.answersys.R;
import com.tao.answersys.bean.AnswerItem;
import com.tao.answersys.bean.Lesson;
import com.tao.answersys.event.EventLessonItemClick;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by LiangTao on 2017/4/29.
 * 课程Adapter
 */
public class AdapterLessons extends RecyclerView.Adapter<ViewHolderLessons>{
    private List<Lesson> mDatas;
    private LayoutInflater mLayoutInflater;

    public AdapterLessons(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 设置显示数据
     * @param data
     */
    public void setdata(List<Lesson> data) {
        this.mDatas = data;
    }

    @Override
    public ViewHolderLessons onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_lesson_item, null);
        return new ViewHolderLessons(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderLessons holder, int position) {
        holder.bindData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}

class ViewHolderLessons extends RecyclerView.ViewHolder{
    private TextView textViewLesson;
    private View mRootView;

    public ViewHolderLessons(View itemView) {
        super(itemView);
        mRootView = itemView;
        textViewLesson = (TextView) itemView.findViewById(R.id.lessons_item_name);
    }

    /**
     * 绑定数据
     * @param data
     */
    public void bindData(final Lesson data) {
        textViewLesson.setText(data.getName());
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventLessonItemClick(data));
            }
        });
    }
}
