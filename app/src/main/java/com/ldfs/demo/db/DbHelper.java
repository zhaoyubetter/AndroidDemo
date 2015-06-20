package com.ldfs.demo.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.ldfs.demo.App;
import com.ldfs.demo.listener.DbInterface;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "demo.db";
	private static final int CURRENT_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, CURRENT_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 笑话表
		db.execSQL("CREATE TABLE " + DbTable.TABLE_NAME + "(" + "_id INTEGER PRIMARY KEY,"//
				+ "id INTEGER," // id
				+ "container TEXT," // 容器id
				+ "name TEXT," // 名称
				+ "parent INTEGER," //父id
				+ "info TEXT," // 描述
				+ "clazz TEXT," // 类名
				+ "ut LONG," // 是否查看
				+ "collect BOOLEAN DEFAULT false," // 是否收藏
				+ "readed  BOOLEAN DEFAULT false," //是否查看
				+ "version INTEGER," // 当前栏版本
				+ "is_use BOOLEAN DEFAULT false" // 是否使用
				+ ")");
		onUpgrade(db, db.getVersion(), CURRENT_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.setVersion(newVersion);
	}

	/**
	 * 根据url获得列表数据
	 *
	 * @param uri
	 * @param t
	 * @param projection
	 * @param selection
	 * @param seltionArgs
	 * @return
	 */
	public static <T extends DbInterface<T>> ArrayList<T> getDatas(Uri uri, T t, String[] projection, String selection, String[] seltionArgs, String order) {
		ContentResolver resolver = App.getAppContext().getContentResolver();
		Cursor cursor = resolver.query(uri, projection, selection, seltionArgs, order);
		ArrayList<T> dataList= t.getDatas(cursor);
		if (null != cursor) {
			cursor.close();
		}
		return dataList;
	}

	/**
	 * 根据url获得列表数据
	 *
	 * @param uri
	 * @param t
	 * @param projection
	 * @param selection
	 * @param seltionArgs
	 * @return
	 */
	public static <T extends DbInterface<T>> ArrayList<T> getDatas(Uri uri, T t, String[] projection, String selection, String... seltionArgs) {
		return getDatas(uri, t, projection, selection, seltionArgs, null);
	}

	/**
	 * 根据url获得对象
	 *
	 * @param uri
	 * @param t
	 * @param projection
	 * @param selection
	 * @param seltionArgs
	 * @return
	 */
	public static <T extends DbInterface<T>> T getData(Uri uri, T t, String[] projection, String selection, String... seltionArgs) {
		return getData(uri, t, projection, selection, seltionArgs, null);
	}

	/**
	 * 根据url获得对象
	 *
	 * @param uri
	 * @param t
	 * @param projection
	 * @param selection
	 * @param seltionArgs
	 * @return
	 */
	public static <T extends DbInterface<T>> T getData(Uri uri, T t, String[] projection, String selection, String[] seltionArgs, String order) {
		ContentResolver resolver = App.getAppContext().getContentResolver();
		Cursor cursor = resolver.query(uri, projection, selection, seltionArgs, order);
		ArrayList<T> datas = t.getDatas(cursor);
		T data = null;
		if (null != datas && !datas.isEmpty()) {
			data = datas.get(0);
		}
		if (null != cursor) {
			cursor.close();
		}
		return data;
	}

	/**
	 * 更新或插入数据
	 *
	 * @param t
	 * @param uri
	 * @param selection
	 * @param seltionArgs
	 */
	public static <T extends DbInterface<T>> void repleceData(T t, Uri uri, String selection, String... seltionArgs) {
		ContentResolver resolver = App.getAppContext().getContentResolver();
		Cursor cursor = resolver.query(uri, null, selection, seltionArgs, null);
		if (null != cursor && cursor.moveToFirst()) {
			// update
			resolver.update(uri, t.getContentValues(), selection, seltionArgs);
		} else {
			// insert
			resolver.insert(uri, t.getContentValues());
		}
		if (null != cursor) {
			cursor.close();
		}
	}

	/**
	 * 插入数据
	 *
	 * @param t
	 * @param uri
	 * @param selection
	 * @param seltionArgs
	 */
	public static <T extends DbInterface<T>> void insertWithNotFound(T t, Uri uri, String selection, String... seltionArgs) {
		ContentResolver resolver = App.getResolver();
		Cursor cursor = resolver.query(uri, null, selection, seltionArgs, null);
		if (null == cursor || !cursor.moveToFirst()) {
			cursor.close();
			resolver.insert(uri, t.getContentValues());
		}
	}

	/**
	 * 插入数据
	 *
	 * @param t
	 * @param uri
	 */
	public static <T extends DbInterface<T>> void insertData(T t, Uri uri) {
		ContentResolver resolver = App.getResolver();
		resolver.insert(uri, t.getContentValues());
	}

}
