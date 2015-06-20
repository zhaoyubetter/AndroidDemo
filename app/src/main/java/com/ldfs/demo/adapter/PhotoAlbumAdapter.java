package com.ldfs.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.bean.PhotoItem;
import com.ldfs.demo.util.ImageLoaderHelper;
import com.ldfs.demo.util.ViewInject;

import java.util.ArrayList;

/**
 * Created by cz on 15/6/16.
 * 图库数据适配器
 */
public class PhotoAlbumAdapter extends RecyclerView.Adapter<PhotoAlbumAdapter.AlbumHolder> {
    private LayoutInflater mInflater;
    private ArrayList<PhotoItem> mItems;
    private ArrayList<PhotoItem> mSelectItems;

    public PhotoAlbumAdapter(Context context, ArrayList<PhotoItem> paths) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = new ArrayList<>();
        this.mSelectItems = new ArrayList<>();
        if (null != paths && !paths.isEmpty()) {
            this.mItems.addAll(paths);
        }
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.photo_album_item, parent, false);
        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlbumHolder holder, int position) {
        final PhotoItem photoItem = this.mItems.get(position);
        ImageLoaderHelper.disPlayImage(holder.thumb, "file://" + photoItem.path);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectItems.contains(photoItem)) {
                    mSelectItems.remove(photoItem);
                } else {
                    mSelectItems.add(photoItem);
                }
                holder.checkBox.setChecked(mSelectItems.contains(photoItem));
            }
        });
    }

    public void swapData(ArrayList<PhotoItem> newItems) {
        this.mItems.clear();
        if (null != newItems) {
            this.mItems.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    public static class AlbumHolder extends RecyclerView.ViewHolder {
        @ID(id = R.id.iv_photo_thumb)
        ImageView thumb;
        @ID(id = R.id.cb_photo_select)
        CheckBox checkBox;

        public AlbumHolder(View itemView) {
            super(itemView);
            ViewInject.init(this, itemView);
        }
    }
}
