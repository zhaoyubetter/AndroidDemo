package com.ldfs.demo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.ldfs.demo.util.ViewInject;

/**
 * 简易的数据适配器
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter implements Filterable {
	protected LayoutInflater inflater;
	protected ArrayList<T> ts;

	public MyBaseAdapter(Context context, ArrayList<T> ts) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.ts = new ArrayList<T>();
		if (null != ts && !ts.isEmpty()) {
			this.ts.addAll(ts);
		}
	}

	@Override
	public int getCount() {
		return ts.size();
	}

	@Override
	public T getItem(int position) {
		T t = null;
		try {
			t = ts.get(position);
		} catch (Exception e) {
		}
		return t;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int itemViewType = getItemViewType(position);
		if (null == convertView) {
			convertView = newView(itemViewType, position, convertView, parent);
		}
		initView(itemViewType, position, convertView, parent);
		return convertView;
	}

	/**
	 * 设置viewHolder
	 * 
	 * @param layout
	 * @param parent
	 * @param holder
	 * @param initParent
	 * @return
	 */
	protected View setViewHolder(int layout, ViewGroup parent, Object holder, boolean initParent) {
		View contentView = inflater.inflate(layout, parent, false);
		ViewInject.init(holder, contentView, initParent);
		contentView.setTag(holder);
		return contentView;
	}

	protected View setViewHolder(int layout, ViewGroup parent, Object holder) {
		return setViewHolder(layout, parent, holder, false);
	}

	public void remove(int position) {
		this.ts.remove(position);
		notifyDataSetChanged();
	}

	public void addHeaderData(ArrayList<T> ts) {
		if (null != ts && !ts.isEmpty()) {
			ArrayList<T> temp = new ArrayList<T>(ts);
			temp.addAll(this.ts);
			this.ts = temp;
			notifyDataSetChanged();
		}
	}

	public void addFootData(ArrayList<T> ts) {
		if (null != ts && !ts.isEmpty()) {
			this.ts.addAll(ts);
			notifyDataSetChanged();
		}
	}

	public void swrpDatas(ArrayList<T> datas) {
		if (null != ts) {
			ts.clear();
		}
		ts.addAll(datas);
		notifyDataSetChanged();
	}

	public void clear() {
		if (null != ts) {
			ts.clear();
			ts = null;
		}
	}

	/**
	 * 过滤关键字
	 */
	public void filterItem(CharSequence charSequence) {
		Filter filter = getFilter();
		if (null == filter) {
			throw new NullPointerException("过滤器对象不能为空!请复写getFilter()方法~");
		}
		filter.filter(charSequence);
	}

	@Override
	public Filter getFilter() {
		return null;
	}

	public ArrayList<T> getItems() {
		return ts;
	}

	/**
	 * 初始化view
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	public abstract View newView(int type, int position, View convertView, ViewGroup parent);

	public abstract void initView(int type, int position, View convertView, ViewGroup parent);

}
