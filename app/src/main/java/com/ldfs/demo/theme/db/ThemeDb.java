package com.ldfs.demo.theme.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/5/13.
 */
public class ThemeDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "theme.db";
    private static final String STYLE_NAME = "style";
    private static final int CURRENT_VERSION = 1;

    public ThemeDb(Context context) {
        super(context, DB_NAME, null, CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STYLE_NAME + "(" + "_id INTEGER PRIMARY KEY,"//
                + "name TEXT," //
                + "thumb TEXT," //
                + "description TEXT," //
                + "collect BOOLEAN," //
                + "readed BOOLEAN," //
                + "version INTEGER," //
                + "container TEXT," //
                + "ut LONG," //
                + "isUse BOOLEAN DEFAULT false" //
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
