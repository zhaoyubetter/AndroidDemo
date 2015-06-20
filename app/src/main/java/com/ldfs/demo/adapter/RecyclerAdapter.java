package com.ldfs.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.RecyclerAdapter.MyViewHolder;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 数据适配器
 * 
 * @author momo
 * @Date 2015/3/5
 * 
 */
public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
	private final LayoutInflater mInflater;
	private final List<String> mData;

	public RecyclerAdapter(Context context, List<String> datas) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.mData = new ArrayList<String>();
		if (null != datas) {
			this.mData.addAll(datas);
		}
	}

	@Override
	public int getItemCount() {
		return this.mData.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.mInfo.setText(mData.get(position));
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		View view = mInflater.inflate(R.layout.card_item, parent, false);
		return new MyViewHolder(view);
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		@ID(id = R.id.text_view)
		public TextView mInfo;

		public MyViewHolder(View convertView) {
			super(convertView);
			ViewInject.init(this, convertView);
		}

	}

}
