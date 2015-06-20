package com.ldfs.demo.theme;

import android.content.ContentValues;
import android.database.Cursor;

import com.ldfs.demo.listener.DbInterface;

import java.util.ArrayList;

/**
 * Created by momo on 2015/5/13.
 */
public class Theme implements DbInterface<Theme> {
    public int id;
    public String name;
    public String thumb;
    public String description;
    public long ct;

    public boolean isUse;
    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public ArrayList<Theme> getDatas(Cursor cursor) {
        return null;
    }
}
