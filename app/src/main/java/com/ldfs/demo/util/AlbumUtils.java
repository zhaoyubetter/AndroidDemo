package com.ldfs.demo.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.ldfs.demo.bean.PhotoItem;

import java.util.ArrayList;

/**
 * Created by cz on 15/6/16.
 * 图库操作工具类
 */
public class AlbumUtils {
    // 设置获取图片的字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID, // id
            MediaStore.Images.Media.WIDTH, // dir id 目录
            MediaStore.Images.Media.HEIGHT, // dir id 目录
            MediaStore.Images.Media.SIZE,//图片大小
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME, // 显示的名称
            MediaStore.Images.Media.BUCKET_ID, // dir id 目录
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,// dir name 目录名字
            MediaStore.Images.Media.DATE_TAKEN

    };

    /**
     * 获得图库目录信息
     *
     * @param context
     * @return
     */
    public static ArrayList<PhotoItem> getPhotoInfos(Context context) {
        int length = STORE_IMAGES.length;
        String[] selections = new String[length + 1];
        System.arraycopy(STORE_IMAGES, 0, selections, 0, length);
        selections[length] = "count(" + MediaStore.Images.Media.BUCKET_ID + ")";//为dir分组
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, "1==1 ) group by(" + MediaStore.Images.Media.BUCKET_ID, null, null);
        ArrayList<PhotoItem> items = null;
        if (null != cursor) {
            items = new ArrayList<>();
            while (cursor.moveToNext()) {
                items.add(new PhotoItem(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getLong(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(7),
                        cursor.getInt(8)));
            }
            cursor.close();
        }
        return items;
    }

    /**
     * 按相册获取指定路径图片资源
     *
     * @author: why
     * @time: 2013-10-18 下午1:35:24
     */
    public static ArrayList<PhotoItem> getPhotoItems(Context context, String albumPath) {
        ArrayList<PhotoItem> photoItems = new ArrayList<PhotoItem>();
        Cursor cursor;
        if (TextUtils.isEmpty(albumPath)) {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
        } else {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, MediaStore.Video.Media.DATA + "=?", new String[]{albumPath}, null);
        }
        if (null != cursor) {
            while (cursor.moveToNext()) {
                photoItems.add(new PhotoItem(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getLong(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(7)));
            }
            cursor.close();
        }
        return photoItems;
    }
}
