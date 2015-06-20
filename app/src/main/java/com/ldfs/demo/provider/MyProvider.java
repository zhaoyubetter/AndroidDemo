package com.ldfs.demo.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.ldfs.demo.db.DbHelper;
import com.ldfs.demo.db.DbTable;

/**
 * 内容提供者
 * 
 * @author momo
 * @Date 2014/12/22
 */
public class MyProvider extends ContentProvider {
	private static final String TAG = "MyProvider";
	private UriMatcher matcher = null;
	private List<Integer> matchIds = null;

	// uri匹配标记
	private static final int _DATA = 1;
	private static final int DATA_ID = 2;

	public static SQLiteDatabase db;// 数据库操作对象

	public MyProvider() {
		super();
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(DbTable.AUTHORITY, DbTable.TABLE_NAME, _DATA);
		matcher.addURI(DbTable.AUTHORITY, DbTable.TABLE_NAME + "/#", DATA_ID);
		// 添加匹配id
		matchIds = new ArrayList<Integer>();
		matchIds.add(_DATA);
		matchIds.add(DATA_ID);

	}

	@Override
	public boolean onCreate() {
		db = new DbHelper(getContext()).getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		int match = matcher.match(uri);
		switch (match) {
		case _DATA:
			builder.setTables(DbTable.TABLE_NAME);
			builder.setProjectionMap(DbTable._MAP);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// 判断uid
		Cursor cursor = null;
		try {
			cursor = builder.query(db, projection, selection, selectionArgs,
					null, null, sortOrder);
		} catch (Exception e) {
		}
		if (null != cursor) {
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int matchId = matcher.match(uri);
		if (!matchIds.contains(matcher.match(uri))) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		ContentValues contentValues = null;
		if (null != values) {
			contentValues = new ContentValues(values);
		} else {
			contentValues = new ContentValues();
		}
		// 插入表名
		String tableName = null;
		// 通知uri
		Uri MAIN_URI = null;
		switch (matchId) {
		case _DATA:
			tableName = DbTable.TABLE_NAME;
			MAIN_URI = DbTable._MAIN;
			break;
		default:
			break;
		}
		long rowId = -1;
		try {
			rowId = db.insert(tableName, null, contentValues);
		} catch (Exception e) {
			Log.i(TAG, "insert:"+e.toString());
		}
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(MAIN_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		return null;
		// throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int match = matcher.match(uri);
		if (!matchIds.contains(match)) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String tableName = null;
		switch (match) {
		case _DATA:
			tableName = DbTable.TABLE_NAME;
			break;
		default:
			break;
		}
		int count = -1;
		if (!TextUtils.isEmpty(tableName) && null != db) {
			try {
				count = db.delete(tableName, selection, selectionArgs);
			} catch (Exception e) {
			}
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int match = matcher.match(uri);
		if (!matchIds.contains(match)) {
			return -1;
		}
		int count = -1;
		String tableName = null;
		switch (match) {
		case _DATA:
			tableName = DbTable.TABLE_NAME;
			break;
		default:
			break;
		}
		if (!TextUtils.isEmpty(tableName)) {
			try {
				count = db.update(tableName, values, selection, selectionArgs);
			} catch (Exception e) {
			}
		}
		if (-1 != count) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int match = matcher.match(uri);
		if (!matchIds.contains(matcher.match(uri))) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (null == values) {
			throw new NullPointerException("values is null!");
		}
		String tableName = null;
		Uri MAIN_URI = null;
		switch (match) {
		case _DATA:
			tableName = DbTable.TABLE_NAME;
			MAIN_URI = DbTable._MAIN;
			break;
		default:
			break;
		}
		long lastId = -1;
		if (!TextUtils.isEmpty(tableName) && null != db) {
			db.beginTransaction();
			for (int i = 0; i < values.length; i++) {
				try {
					long rowId = db.insert(tableName, null, values[i]);
					if (i == values.length - 1) {
						lastId = rowId;
					}
				} catch (Exception e) {
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		if (lastId > 0) {
			Uri noteUri = ContentUris.withAppendedId(MAIN_URI, lastId);
			getContext().getContentResolver().notifyChange(noteUri, null);
		}
		return (int) lastId;
		// throw new SQLException("Failed to insert more row into " + uri);
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

}
