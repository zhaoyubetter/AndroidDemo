package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * 测试旋转绘制
 * 
 * @author momo
 * @Date 2015/2/12
 * 
 */
public class RotateImageView extends View {

	public RotateImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RotateImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RotateImageView(Context context) {
		this(context, null, 0);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
