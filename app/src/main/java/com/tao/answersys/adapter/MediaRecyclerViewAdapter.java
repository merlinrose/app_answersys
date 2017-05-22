package com.tao.answersys.adapter;

import java.util.List;

import com.tao.answersys.R;
import com.tao.answersys.bean.MediaViewBean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MediaRecyclerViewAdapter extends RecyclerView.Adapter<MediaViewHolder>{
	private List<MediaViewBean> dataList;
	private LayoutInflater inflater;
	private Context context;
	private MediaViewClickListener listener;
	
	public MediaRecyclerViewAdapter(Activity context) {
		this.context = context;
		this.inflater = context.getLayoutInflater();
	}
	
	public void setData(List<MediaViewBean> data) {
		this.dataList = data;
	}
	
	public List<MediaViewBean> getData() {
		return dataList;
	}
	
	public int addData(MediaViewBean data) {
		dataList.add(0, data);
		return 1;
	}
	
	public void removeData(int position) {
		dataList.remove(position);
	}

	@Override
	public int getItemCount() {
		int count = dataList == null ? 0 : dataList.size();
		return count+1;
	}

	@Override
	public void onBindViewHolder(MediaViewHolder holder, int position) {
		MediaViewBean dataBean =  null;
		
		if(position == 0) {
			holder.getImageView().setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add));
			holder.hideDelteButton();
		} else {
			dataBean = dataList.get(position-1);
			dataBean.setPosition(position-1);
			Bitmap bitmap = dataBean.getBitmap();
			holder.showDelteButton();
			
			if(bitmap != null) {
				holder.getImageView().setImageDrawable(new BitmapDrawable(bitmap));
			}
		}
		
		holder.setData(listener, dataBean);
	}
	
	@SuppressLint("InflateParams")
	@Override
	public MediaViewHolder onCreateViewHolder(ViewGroup group, int position) {
		View view = inflater.inflate(R.layout.layout_media_item, null);
		
		return new MediaViewHolder(view);
	}
	
	public void setMediaViewClickListener(MediaViewClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 附件显示View的点击与删除回调
	 * @author LiangTao
	 *
	 */
	public interface MediaViewClickListener {
		public void onClick(MediaViewBean mediaViewBean);

		public void onDelte(MediaViewBean mediaViewBean);
	}
}

class MediaViewHolder extends RecyclerView.ViewHolder {
	private View itemView;
	private ImageView imageView;
	private View buttonDelte;

	public MediaViewHolder(View itemView) {
		super(itemView);
		this.itemView = itemView;

		imageView = (ImageView) itemView.findViewById(R.id.media_item_content);
		buttonDelte = itemView.findViewById(R.id.media_item_delete);

	}

	public void setData(final MediaRecyclerViewAdapter.MediaViewClickListener listener, final MediaViewBean bean) {
		itemView.setOnClickListener(new View.OnClickListener() {//条目点击事件
			@Override
			public void onClick(View v) {
				if(listener != null) {
					listener.onClick(bean);
				}
			}
		});

		buttonDelte.setOnClickListener(new View.OnClickListener() {//点击删除
			@Override
			public void onClick(View v) {
				if(listener != null) {
					listener.onDelte(bean);
				}
			}
		});
	}

	public void hideDelteButton() {
		buttonDelte.setVisibility(View.GONE);
	}

	public void showDelteButton() {
		buttonDelte.setVisibility(View.VISIBLE);
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public View getButtonDelte() {
		return buttonDelte;
	}

	public void setButtonDelte(View buttonDelte) {
		this.buttonDelte = buttonDelte;
	}
}
