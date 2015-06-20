package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;

/**
 * 带遮罩层效果的选择组
 * 
 * @author momo
 * @Date 2015/2/12
 */
public class MaskRadioLayout extends RadioGridLayout {
	private static final int MASK_COLOR = 0xBB000000;
	private static final int CIRCLE_MODE = 0;
	private static final int RETANGLE_MODE = 1;
	private int mMaskMode;// 遮罩层的形状
	private float mMaskPadding;// 遮罩层内边距
	private Paint mPaint;
	private int mColor;

	public MaskRadioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint = new Paint();
		setWillNotDraw(false);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaskRadioLayout);
		setMaskMode(a.getInt(R.styleable.MaskRadioLayout_ml_mask_mode, CIRCLE_MODE));
		setMaskPadding(a.getDimension(R.styleable.MaskRadioLayout_ml_mask_padding, 0));
		setMaskColor(a.getColor(R.styleable.MaskRadioLayout_ml_mask_color, MASK_COLOR));
		setMode(SINGLE_CHOOSE);// 设置单选状态
		a.recycle();
	}

	/**
	 * 设置遮罩层颜色
	 * 
	 * @param color
	 */
	private void setMaskColor(int color) {
		this.mColor = color;
	}

	public MaskRadioLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaskRadioLayout(Context context) {
		this(context, null, 0);
	}

	/**
	 * 设置遮罩层类型
	 * 
	 * @param mode
	 */
	public void setMaskMode(int mode) {
		this.mMaskMode = mode;
		invalidate();
	}

	/**
	 * 设置遮罩层内边距
	 * 
	 * @param dimension
	 */
	public void setMaskPadding(float padding) {
		this.mMaskPadding = padding;
		invalidate();
	}

	/**
	 * 添加选中控件
	 * 
	 * @param res
	 */
	public void addImageViews(int... resid) {
		Context context = getContext();
		for (int i = 0; i < resid.length; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			int padding = UnitUtils.dip2px(context, 5);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setImageResource(resid[i]);
			addView(imageView);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setOnClickListenerByMode(v);
				}
			});
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		mPaint.setColor(mColor);
		drawMaskInView(canvas, mLastSelectView, SINGLE_CHOOSE == mMode ? Color.TRANSPARENT : MASK_COLOR);
		drawMaskInView(canvas, mCurrentSelectView, MASK_COLOR);
	}

	private void drawMaskInView(Canvas canvas, View view, int color) {
		if (null != view) {
			mPaint.setColor(color);
			int width = view.getWidth();
			int height = view.getHeight();
			int x = 0, y = 0, radius = 0;
			if (width > height) {
				radius = height / 2;
				x = (width - height) / 2;
			} else {
				radius = width / 2;
				y = (height - width) / 2;
			}
			canvas.save();
			canvas.translate(view.getLeft() + x, view.getTop() + y);
			switch (mMaskMode) {
			case CIRCLE_MODE:
				canvas.drawCircle(0, 0, radius - mMaskPadding, mPaint);
				break;
			case RETANGLE_MODE:
				Rect outRect = new Rect();
				view.getDrawingRect(outRect);
				canvas.drawRect(outRect, mPaint);
				break;
			default:
				break;
			}
			canvas.restore();
		}
	}

}
