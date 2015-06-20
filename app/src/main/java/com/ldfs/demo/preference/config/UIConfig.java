package com.ldfs.demo.preference.config;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.ldfs.demo.listener.DbInterface;

/**
 * UI配置
 * 
 * @author momo
 * @Date 2015/1/25
 * 
 */
public class UIConfig implements DbInterface<UIConfig> {
	public String name;// 名称
	public int id;// 分类id
	public int parent;// 上一个分类id
	public int version;//当前分类所属版本
	public List<UIItem> items;// 分类下条目

	@Override
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("name", name);
		values.put("parent", parent);
		values.put("version", version);
		values.put("ut", System.currentTimeMillis());
		return values;
	}

	@Override
	public ArrayList<UIConfig> getDatas(Cursor cursor) {
		return null;
	}

}
