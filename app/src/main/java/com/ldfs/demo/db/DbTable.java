package com.ldfs.demo.db;

import java.util.HashMap;

import android.net.Uri;

/**
 * 数据库表帮助类
 * 
 * @author momo
 */
public class DbTable {
	public static final String AUTHORITY = "com.ldfs.demo";
	/** 本地缓存表名 */
	public static final String TABLE_NAME = "demo";

	public static final String[] _SELECTION;// 表选中列

	// 外部访问uri地址
	public static final Uri _URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final Uri _MAIN = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME + "/");
	// 数据列表
	public static final HashMap<String, String> _MAP;
	static {
		_MAP = new HashMap<String, String>();
		_MAP.put("_id", "_id");
		_MAP.put("id", "id");
		_MAP.put("name", "name");
		_MAP.put("parent", "parent");
		_MAP.put("info", "info");
		_MAP.put("clazz", "clazz");
		_MAP.put("collect", "collect");
		_MAP.put("readed", "readed");
		_MAP.put("version", "version");
		_MAP.put("container", "container");
		_MAP.put("ut", "ut");


		_SELECTION = new String[] { "_id", "id", "name", "parent", "info", "clazz", "collect","readed","version","container","ut" };

	}
}
