package com.ldfs.demo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * 测试xfermodes绘制类
 * 
 * @author momo
 * @Date 2015/3/3
 */
public class XfermodesSampleView extends View {
	private Paint mPaint;
	private Xfermode mMode;
	private int mRadius;// 绘制半径

	public XfermodesSampleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint = new Paint();
		mPaint.setFilterBitmap(false);
		mMode = new PorterDuffXfermode(PorterDuff.Mode.DST);
	}

	public XfermodesSampleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XfermodesSampleView(Context context) {
		this(context, null, 0);
	}

	public void setXfermode(PorterDuffXfermode mode) {
		this.mMode = mode;
		invalidate();
	}

	public void setRadius(int radius) {
		this.mRadius = radius;
		invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		int sc = canvas.saveLayer(0, 0, width, height, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		mPaint.reset();
		mPaint.setColor(Color.RED);
		canvas.drawOval(new RectF(0, 0, width, height), mPaint);
		mPaint.setXfermode(mMode);
		mPaint.setColor(Color.GREEN);
		canvas.drawOval(new RectF(width / 2 - mRadius, height / 2 - mRadius, width / 2 + mRadius, height / 2 + mRadius), mPaint);
		canvas.restoreToCount(sc);
	}

}
