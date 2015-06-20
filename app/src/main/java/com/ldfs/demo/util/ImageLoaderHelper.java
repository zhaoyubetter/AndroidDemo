package com.ldfs.demo.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ldfs.demo.R;
import com.ldfs.demo.listener.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 图片异步加载帮助
 * 
 * @author momo
 * @Date 2014/6/5
 */
public class ImageLoaderHelper {
	private final static DisplayImageOptions noneOptions;// 没有加载图片
	private final static DisplayImageOptions options;// 常规加载
	private final static DisplayImageOptions bigImageOption;//大缩略图加载模式
	private final static DisplayImageOptions roundedOptions;// 常规加载
	private final static DisplayImageOptions coverOptions;// 头像加载配置项
	private final static AnimateFirstDisplayListener animateFirstListener;

	static {
		noneOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.img_default).showImageForEmptyUri(R.drawable.img_default).showImageOnFail(R.drawable.img_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		bigImageOption = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.img_default_big).showImageForEmptyUri(R.drawable.img_default).showImageOnFail(R.drawable.img_default).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		roundedOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.img_default).showImageForEmptyUri(R.drawable.img_default).showImageOnFail(R.drawable.img_default)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(40)).bitmapConfig(Bitmap.Config.RGB_565).build();
		coverOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_user_cover).showImageForEmptyUri(R.drawable.default_user_cover)
				.showImageOnFail(R.drawable.default_user_cover).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		animateFirstListener = new AnimateFirstDisplayListener();
	}

	public static void disPlayCover(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, coverOptions, animateFirstListener);
	}

	public static void disPlayNoneImage(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, noneOptions, animateFirstListener);
	}
	
	public static void disPlayNoneImage(final ImageView imageView, final SimpleImageLoadingListener listener, ImageLoadingProgressListener progressListener, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, noneOptions, listener, progressListener);
	}

	public static void disPlayRoundedImageView(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, roundedOptions, animateFirstListener);
	}

	public static void disPlayNoAnimImage(final ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}

	public static void disPlayImage(final ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, options, animateFirstListener);
	}

	public static void disPlayBigImage(final ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, bigImageOption, animateFirstListener);
	}

	public static void disPlayImage(final ImageView imageView, final ProgressBar wheel, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, options, new AnimateFirstDisplayListener(wheel), new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String info, View imageView, int current, int total) {
				if (total != wheel.getMax()) {
					wheel.setMax(total);
				}
				wheel.setProgress(current);
			}
		});
	}

	public static void disPlayImage(final ImageView imageView, final SimpleImageLoadingListener listener, ImageLoadingProgressListener progressListener, String url) {
		ImageLoader.getInstance().displayImage(url, imageView, options, listener, progressListener);
	}
}
