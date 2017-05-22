package com.tao.answersys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tao.answersys.R;
import com.tao.answersys.bean.ImgViewItem;
import com.tao.answersys.utils.ImageLoader;

import java.util.List;

/**
 * Created by LiangTao on 2017/5/21.
 */

public class ImgsRecyclerViewAdapter extends RecyclerView.Adapter<ImgsViewHolder>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<ImgViewItem> mData;

    public ImgsRecyclerViewAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<ImgViewItem> list) {
        this.mData = list;
    }

    @Override
    public ImgsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_imgs_item, null);
        return new ImgsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImgsViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}

class ImgsViewHolder extends RecyclerView.ViewHolder{
    private ImageView imageView;

    public ImgsViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imgs_item_imgview);
    }

    public void bindData(ImgViewItem item) {
        ImageLoader.getInstance().loadImageAsync(item.getUrl(), imageView);
    }
}
