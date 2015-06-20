package com.ldfs.demo.widget.indicator.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;

import com.ldfs.demo.widget.indicator.Indicatorable;
import com.ldfs.demo.widget.indicator.ViewPagerIndicator.IndicatorConfig;

/**
 * 圆形指示器drawable
 * 
 * @author momo
 * @Date 2015/2/9
 * 
 */
public final class CircleIndicatorDrawable extends ShapeDrawable implements Indicatorable {
	// 移动类型
	private static final int _MOVE = 0;
	private static final int _RATE = 1;
	private static final int _SIZE = 2;
	private IndicatorConfig mConfig;
	private int mIndex;// 当前位置
	private int mCount;// 当前绘制总数
	private PorterDuffXfermode mRrcAtopfermode;
	private Paint mPaint;

	/**
	 * 初始化角度
	 * 
	 * @param radio
	 */
	public CircleIndicatorDrawable(int index, int count, IndicatorConfig config) {
		super();
		this.mConfig = config;
		this.mIndex = index;
		this.mCount = count;
		setEvaluateShader(this.mConfig.defaultColor, getPaint());

		setEvaluateShader(this.mConfig.animColor, mPaint = new Paint());
		mRrcAtopfermode = new PorterDuffXfermode(Mode.SRC_ATOP);
	}

	private void setEvaluateShader(int defaultColor, Paint paint) {
		float radial = this.mConfig.size / 4;
		RadialGradient gradient = new RadialGradient(radial * 3, radial, this.mConfig.size, defaultColor, getDarkColor(defaultColor), Shader.TileMode.CLAMP);
		paint.setShader(gradient);
	}

	/**
	 * 分解颜色,获得rgb,然后计算出渐变色
	 * 
	 * @param color
	 * @return
	 */
	private int getDarkColor(int color) {
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		int darkColor = 0XFF000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
		return darkColor;
	}

	@Override
	public void draw(Canvas canvas) {
		switch (this.mConfig.circleType) {
		case _MOVE:
			drawMoveCircle(canvas);
			break;
		case _RATE:
			drawRateCircle(canvas);
			break;
		case _SIZE:
			drawSizeCircle(canvas);
			break;
		default:
			break;
		}
	}

	/**
	 * 绘制颜色,大小改变的动画
	 * 
	 * @param canvas
	 */
	private void drawSizeCircle(Canvas canvas) {
		RectF newRect = initScaleRect(this.mConfig.mRect);
		if (mIndex == this.mConfig.position) {
			// setColor无效,当使用shader绘制时,就必须使用shader改变取值
			int color = evaluate(this.mConfig.positionOffset, this.mConfig.animColor, this.mConfig.defaultColor);
			setEvaluateShader(color, mPaint);
			canvas.drawOval(newRect, mPaint);
		} else if (mIndex == (mCount - 1 == this.mConfig.position ? 0 : this.mConfig.position + 1)) {
			int color = evaluate(this.mConfig.positionOffset, this.mConfig.defaultColor, this.mConfig.animColor);
			setEvaluateShader(color, mPaint);
			canvas.drawOval(newRect, mPaint);
		} else {
			canvas.drawOval(newRect, getPaint());
		}
	}

	/**
	 * 绘制带进度圆环
	 * 
	 * @param canvas
	 */
	private void drawRateCircle(Canvas canvas) {
		RectF newRect = initScaleRect(this.mConfig.mRect);
		if (mIndex == this.mConfig.position) {
			canvas.drawOval(newRect, getPaint());
			canvas.drawArc(newRect, 0, 360f * (1f - this.mConfig.positionOffset), true, mPaint);
		} else if (mIndex == (mCount - 1 == this.mConfig.position ? 0 : this.mConfig.position + 1)) {
			canvas.drawOval(newRect, getPaint());
			canvas.drawArc(newRect, 0, 360f * this.mConfig.positionOffset, true, mPaint);
		} else {
			canvas.drawOval(newRect, getPaint());
		}
	}

	/**
	 * 绘制移动圆环
	 * 
	 * @param canvas
	 */
	private void drawMoveCircle(Canvas canvas) {
		float size = this.mConfig.size + this.mConfig.scaleSize;
		int sc = canvas.saveLayer(0, 0, size, size, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		RectF newRect = initScaleRect(this.mConfig.mRect);
		canvas.drawOval(newRect, getPaint());
		mPaint.setXfermode(mRrcAtopfermode);
		if (mIndex == this.mConfig.position) {
			canvas.drawOval(new RectF(0 + this.mConfig.positionOffset * size, 0, size + this.mConfig.positionOffset * size, size), mPaint);
		} else if (mIndex == (mCount - 1 == this.mConfig.position ? 0 : this.mConfig.position + 1)) {
			// 用position==count时取0
			canvas.drawOval(new RectF(-(1f - this.mConfig.positionOffset) * size, 0, size - (1f - this.mConfig.positionOffset) * size, size), mPaint);
		}
		mPaint.setXfermode(null);
		canvas.restoreToCount(sc);
	}

	private RectF initScaleRect(RectF rect) {
		RectF newRect = new RectF(rect);
		if (mIndex == this.mConfig.position) {
			newRect.right += (this.mConfig.scaleSize * (1f - this.mConfig.positionOffset));
			newRect.bottom += (this.mConfig.scaleSize * (1f - this.mConfig.positionOffset));
		} else if (mIndex == (mCount - 1 == this.mConfig.position ? 0 : this.mConfig.position + 1)) {
			newRect.right += (this.mConfig.scaleSize * this.mConfig.positionOffset);
			newRect.bottom += (this.mConfig.scaleSize * this.mConfig.positionOffset);
		}
		return newRect;
	}

	@Override
	public void onPageScrollStateChanged(int status) {
	}

	@Override
	public void onPageScrolled(int position, float offset, int offsetValue) {
		this.mConfig.position = position % mCount;
		this.mConfig.positionOffset = offset;
	}

	@Override
	public void onPageSelected(int position) {
	}

	/**
	 * 计算颜色渐变取值
	 * 
	 * @param fraction
	 *            偏移量
	 * @param startValue
	 * @param endValue
	 * @return
	 */
	public int evaluate(float fraction, int startValue, int endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24);
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endInt = (Integer) endValue;
		int endA = (endInt >> 24);
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		return (int) ((startA + (int) (fraction * (endA - startA))) << 24) | (int) ((startR + (int) (fraction * (endR - startR))) << 16) | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
				| (int) ((startB + (int) (fraction * (endB - startB))));
	}

}
