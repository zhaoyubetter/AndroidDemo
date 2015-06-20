package com.ldfs.demo.preference.config;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.ldfs.demo.listener.DbInterface;

public class UIItem implements DbInterface<UIItem>{
	public int id;
	public int parent;
	public String name;
	public String info;
	public String clazz;
	public String container;
	public int version;//当前分类所属版本
	
	@Override
	public ContentValues getContentValues() {
		ContentValues values=new ContentValues();
		values.put("id", id);
		values.put("parent", parent);
		values.put("name", name);
		values.put("info", info);
		values.put("clazz", clazz);
		values.put("version", version);
		values.put("container", container);
		values.put("ut", System.currentTimeMillis());
		return values;
	}
	@Override
	public ArrayList<UIItem> getDatas(Cursor cursor) {
		return null;
	}
}