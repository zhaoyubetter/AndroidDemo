package com.ldfs.demo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.bean.MyItem;

public class ItemAdapter extends MyBaseAdapter<MyItem> {

	public ItemAdapter(Context context, ArrayList<MyItem> ts) {
		super(context, ts);
	}

	@Override
	public View newView(int type, int position, View convertView, ViewGroup parent) {
		return setViewHolder(R.layout.demo_item, parent, new ViewHolder());
	}

	@Override
	public void initView(int type, int position, View convertView, ViewGroup parent) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		MyItem item = getItem(position);
		holder.name.setText(item.name);
		holder.info.setText(item.info);
		holder.name.setSelected(item.isRead);
		holder.collect.setVisibility((-1 == item.id) ? View.VISIBLE : View.GONE);
	}

	static class ViewHolder {
		@ID(id = R.id.tv_name)
		TextView name;
		@ID(id = R.id.tv_info)
		TextView info;
		@ID(id = R.id.iv_collect)
		ImageView collect;
	}

}
