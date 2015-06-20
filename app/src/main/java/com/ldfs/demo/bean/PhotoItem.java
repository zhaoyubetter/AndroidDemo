package com.ldfs.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cz on 15/6/16.
 * 图库条目
 */
public class PhotoItem implements Parcelable {
    public int id;
    public int width;
    public int height;
    public long size;
    public String path;//图片路径
    public String name;//图片名称
    public String title;//图片目录标题

    public int count;//当前目录下子条目数量



    public PhotoItem(int id, int width, int height, long size, String path, String name, String title) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.size = size;
        this.path = path;
        this.name = name;
        this.title = title;
    }


    public PhotoItem(int id, int width, int height, long size, String path, String name, String title, int count) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.size = size;
        this.path = path;
        this.name = name;
        this.title = title;
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeLong(this.size);
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeInt(this.count);
    }

    protected PhotoItem(Parcel in) {
        this.id = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.size = in.readLong();
        this.path = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        public PhotoItem createFromParcel(Parcel source) {
            return new PhotoItem(source);
        }

        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };
}
