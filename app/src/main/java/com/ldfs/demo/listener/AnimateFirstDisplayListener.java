package com.ldfs.demo.listener;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 图片加载动画监听
 * 
 * @author momo
 * @Date 2015/1/4
 * 
 */
public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
	static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
	private ProgressBar wheel;

	public AnimateFirstDisplayListener() {
		super();
	}

	public AnimateFirstDisplayListener(ProgressBar wheel) {
		super();
		this.wheel = wheel;
	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		super.onLoadingFailed(imageUri, view, failReason);
		if (null != this.wheel) {
			this.wheel.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (null != this.wheel) {
			this.wheel.setVisibility(View.GONE);
		}
		if (loadedImage != null) {
			ImageView imageView = (ImageView) view;
			boolean firstDisplay = !displayedImages.contains(imageUri);
			if (firstDisplay) {
				FadeInBitmapDisplayer.animate(imageView, 300);
				displayedImages.add(imageUri);
			}
		}
	}
}
