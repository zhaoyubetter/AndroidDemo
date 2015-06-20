package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ldfs.demo.util.UnitUtils;

/**
 * 绘制控件间距离控件
 * 
 * @author momo
 * @Date 2015/2/26
 * 
 */
public class DistanceLayout extends RelativeLayout {
	private int mColor;
	private int mStrokeWidth;
	private Paint mPaint;
	private int mTextColor;
	private int mTextSize;
	private int mTextPadding;
	private Unit mUnit;
	private boolean isEngineering;// 是否为工程模式

	public DistanceLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWillNotDraw(false);
		setUp(context);

	}

	private void setUp(Context context) {
		mPaint = new Paint();
		mUnit = Unit.DP;
		mColor = Color.GREEN;
		mTextSize = UnitUtils.sp2px(context, 12);
		mTextColor = Color.BLUE;
		mStrokeWidth = UnitUtils.dip2px(context, 1);
		mTextPadding = UnitUtils.dip2px(context, 2);
		isEngineering = true;
	}

	public DistanceLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DistanceLayout(Context context) {
		this(context, null, 0);
	}

	public void setUnit(Unit unit) {
		if (null == unit) {
			throw new NullPointerException("距离单位不能为null!");
		} else {
			mUnit = unit;
			invalidate();
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		View childView = null;
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			childView = getChildAt(i);
			// 判断在左侧还是右侧
			int left = childView.getLeft();
			int right = childView.getRight();
			int width = childView.getWidth();
			int height = childView.getHeight();

			if (left < right) {
				// 左侧1
				int startX = (width - mStrokeWidth) >> 1;
				int startY = (height - mStrokeWidth) >> 1;
				// 绘线
				mPaint.setColor(mColor);
				mPaint.setStrokeWidth(mStrokeWidth);
				// canvas.drawLine(0, startY, stopX, stopY, mPaint);

				// 左侧2

				// 左上1

				// 左上2
			} else {
				// 右侧
			}
		}
	}

	public enum Unit {
		DP, PX;
	}

}
