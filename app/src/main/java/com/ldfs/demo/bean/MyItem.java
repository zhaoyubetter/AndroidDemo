package com.ldfs.demo.bean;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ldfs.demo.listener.DbInterface;

/**
 * 显示条目内容
 * 
 * @author momo
 * @Date 2015/1/25
 * 
 */
public class MyItem implements Parcelable, DbInterface<MyItem> {
	public int id;
	public int parent;
	public String name;
	public String info;
	public String clazz;
	public boolean isCollect;
	public boolean isRead;
	public int version;
	public String container;
	public long ut;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int arg1) {
		p.writeInt(id);
		p.writeInt(parent);
		p.writeString(name);
		p.writeString(info);
		p.writeString(clazz);
		p.writeInt(isCollect ? 1 : 0);
		p.writeInt(isRead ? 1 : 0);
		p.writeInt(version);
		p.writeString(container);
		p.writeLong(ut);
	}

	public static final Parcelable.Creator<MyItem> CREATOR = new Creator<MyItem>() {
		@Override
		public MyItem[] newArray(int size) {
			return new MyItem[size];
		}

		@Override
		public MyItem createFromParcel(Parcel p) {
			MyItem item = new MyItem();
			item.id = p.readInt();
			item.parent = p.readInt();
			item.name = p.readString();
			item.info = p.readString();
			item.clazz = p.readString();
			item.isCollect = 1 == p.readInt();
			item.isRead = 1 == p.readInt();
			item.container=p.readString();
			item.ut=p.readLong();
			return item;
		}
	};

	@Override
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("parent", parent);
		values.put("name", name);
		values.put("info", info);
		values.put("clazz", clazz);
		values.put("collect", isCollect);
		values.put("readed", isRead);
		values.put("version", version);
		values.put("container", container);
		values.put("ut", System.currentTimeMillis());
		return values;
	}

	@Override
	public ArrayList<MyItem> getDatas(Cursor cursor) {
		ArrayList<MyItem> items = null;
		if (null != cursor) {
			MyItem item = null;
			items = new ArrayList<MyItem>();
			while (cursor.moveToNext()) {
//				"_id", "id", "name", "parent", "info", "clazz", "collect","readed"
				item = new MyItem();
				item.id = cursor.getInt(1);
				item.name = cursor.getString(2);
				item.parent = cursor.getInt(3);
				item.info = cursor.getString(4);
				item.clazz = cursor.getString(5);
				item.isCollect = 1 == cursor.getInt(6);
				item.isRead = 1 == cursor.getInt(7);
				item.version=cursor.getInt(8);
				item.container=cursor.getString(9);
				item.ut=cursor.getLong(10);
				items.add(item);
			}
		}
		return items;
	}

}
