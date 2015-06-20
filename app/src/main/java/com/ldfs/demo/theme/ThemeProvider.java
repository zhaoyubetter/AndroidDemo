package com.ldfs.demo.theme;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.ldfs.demo.db.DbTable;
import com.ldfs.demo.theme.db.ThemeDb;

/**
 * Created by momo on 2015/5/13.
 */
public class ThemeProvider extends ContentProvider {
    private UriMatcher matcher = null;

    private static final int _DATA = 1;

    public static SQLiteDatabase db;

    public ThemeProvider() {
        super();
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DbTable.AUTHORITY, DbTable.TABLE_NAME, _DATA);
    }

    @Override
    public boolean onCreate() {
        db = new ThemeDb(getContext()).getWritableDatabase();
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
        String tableName = null;
        // ֪ͨuri
        if (_DATA == matchId) {
            tableName = DbTable.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        ContentValues contentValues = null;
        if (null != values) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }
        long rowId = -1;
        try {
            rowId = db.insert(tableName, null, contentValues);
        } catch (Exception e) {
        }
        return null;
        // throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int matchId = matcher.match(uri);
        String tableName = null;
        if (_DATA == matchId) {
            tableName = DbTable.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
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
        int matchId = matcher.match(uri);
        String tableName = null;
        if (_DATA == matchId) {
            tableName = DbTable.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int count = -1;
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
        int matchId = matcher.match(uri);
        String tableName = null;
        if (_DATA == matchId) {
            tableName = DbTable.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
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
        return (int) lastId;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
