package com.ldfs.demo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 图片处理工具
 * 
 * @author Ryan.Tang
 * 
 */
public final class ImageUtils {
	public static final int MAX_HEIGHT = 4096;

	/**
	 * Drawable转Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap转Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * 输输流对象转Bitmap
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static Bitmap inputStreamToBitmap(InputStream inputStream) throws Exception {
		return BitmapFactory.decodeStream(inputStream);
	}

	/**
	 * 字节字节数组转Bitmap
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	/**
	 * 字节数组转Drawable对象
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = null;
		if (byteArray != null) {
			ins = new ByteArrayInputStream(byteArray);
		}
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * Bitmap转字节数组
	 * 
	 * @param byteArray
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		byte[] bytes = null;
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bytes = baos.toByteArray();
		}
		return bytes;
	}

	/**
	 * Drawable转字节数组
	 * 
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		byte[] bytes = bitmapToBytes(bitmap);
		;
		return bytes;
	}

	/**
	 * Create reflection images
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 图片角圆化
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            5 10
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 位图圆角处理
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		try {

			Bitmap targetBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

			// 得到画布
			Canvas canvas = new Canvas(targetBitmap);

			// 创建画笔
			Paint paint = new Paint();
			paint.setAntiAlias(true);

			// 值越大角度越明显
			float roundPx = 30;
			float roundPy = 30;

			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectF = new RectF(rect);

			// 绘制
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawRoundRect(rectF, roundPx, roundPy, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			return targetBitmap;

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将图片圆角化,并返回Drawable对象
	 */
	public static Drawable getRoundedDrawable(Bitmap bitmap) {
		return bitmapToDrawable(getRoundedCornerBitmap(bitmap, Integer.MAX_VALUE));
	}

	/**
	 * 给定 drawable将其园角化
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundBitmap(Drawable drawable) {
		return getRoundedBitmap(drawableToBitmap(drawable));
	}

	public static Bitmap getRoundedBitmap(Bitmap bitmap) {
		return getRoundedCornerBitmap(bitmap, Integer.MAX_VALUE);
	}

	/**
	 * 将图片存入缓存目录
	 */
	public static void save2Cache(Context context, String cover, Bitmap bitmap) {
		File file = new File(context.getCacheDir(), cover + ".png");
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			if (bitmap != null) {
				if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
					fileOutputStream.flush();
				}
			}
		} catch (FileNotFoundException e) {
			file.delete();
			e.printStackTrace();
		} catch (IOException e) {
			file.delete();
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置图片颜色
	 * 
	 * @param image
	 * @param color
	 */
	public static void setDrawableScale(ImageView image, int color) {
		setDrawableScale(image, color, false);
	}

	public static void setBackGroundScale(View view, int color) {
		if (null != view && null != view.getBackground()) {
			view.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
		}
	}

	public static void setDrawableScale(ImageView image, int color, boolean isChangeAll) {
		if (null != image && null != image.getDrawable()) {
			Drawable drawable = image.getDrawable();
			if (isChangeAll) {
				drawable = drawable.mutate();
			}
			drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
			image.setImageDrawable(drawable);
		}
	}

	/**
	 * 获得指定过滤对象
	 * 
	 * @param color
	 * @return
	 */
	public static Drawable getFilterDrawable(Context context, int resId, int color) {
		Drawable drawable = context.getResources().getDrawable(resId);
		return getFilterDrawable(context, drawable, color);
	}

	public static Drawable getFilterDrawable(Context context, Drawable drawable, int color) {
		if (null != drawable) {
			drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
		}
		return drawable;
	}

	public static void setTextDrawableScale(TextView textView, int direction, int color) {
		Drawable[] drawables = textView.getCompoundDrawables();
		if (null != textView && null != drawables && direction < drawables.length && null != drawables[direction]) {
			drawables[direction].setColorFilter(color, PorterDuff.Mode.MULTIPLY);
		}
	}

	/**
	 * 使图片变灰
	 * 
	 * @param image
	 */
	public static void setGrayImage(ImageView image) {
		Drawable drawable = image.getDrawable();
		drawable.mutate();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
		drawable.setColorFilter(cf);
		image.setImageDrawable(drawable);
	}

	/**
	 * 根据图库uri拿到图片绝对路径
	 * 
	 * @param uri
	 *            图片的uri
	 * @param activity
	 *            当前activity
	 * @return 图片的绝路径
	 */
	public static String getAbsoluteImagePath(Uri uri, Activity activity) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, proj, // Which columns to
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			// 如果游标为空说明获取的已经是绝对路径了
			return uri.getPath();
		}
	}

	public static float COMPRESS_IMAGE_SIZE = 200;// 压缩图片的上限,单位是kb

}
