package com.ldfs.demo.listener;

import android.view.View;

/**
 * recyclerItem条目点击事件
 * 
 * @author momo
 * @Date 2015/3/5
 * 
 */
public interface OnRecyclerItemClickListener {
	
	void onItemClick(View convertView, int position);
}
