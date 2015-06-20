package com.ldfs.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ldfs.demo.adapter.ImageRecyclerAdapter.ImageHolder;
import com.ldfs.demo.listener.OnRecyclerItemClickListener;

public class ImageRecyclerAdapter extends Adapter<ImageHolder> {
	private Context mContext;
	private int[] mRes;
	private OnRecyclerItemClickListener mListener;

	public ImageRecyclerAdapter(Context mContext, int[] res) {
		super();
		this.mContext = mContext;
		this.mRes = res;
	}

	@Override
	public int getItemCount() {
		return mRes.length;
	}

	@Override
	public void onBindViewHolder(final ImageHolder holder, final int position) {
		holder.mImage.setImageResource(mRes[position]);
		holder.itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					mListener.onItemClick(holder.itemView, position);
				}
			}
		});
	}

	@Override
	public ImageHolder onCreateViewHolder(ViewGroup parent, int position) {
		ImageView image = new ImageView(mContext);
		return new ImageHolder(image);
	}

	public void setOnRecyclerItemClickListener(
			OnRecyclerItemClickListener listener) {
		this.mListener = listener;
	}

	public static class ImageHolder extends RecyclerView.ViewHolder {
		public ImageView mImage;

		public ImageHolder(View view) {
			super(view);
			mImage = (ImageView) view;
		}
	}

}
