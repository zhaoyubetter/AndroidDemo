package com.ldfs.demo.widget;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * 圆形转盘控件
 * 
 * @author momo
 * @Date 2014/10/31
 */
public class TurntableWheel extends View {
	private static final int UN_RES = -1;
	private static final int DEFAULT_LINE_WIDTH = 2;// 默认线宽,将转换为dp
	private static final int DEFAULT_COLOR = Color.DKGRAY;// 默认绘制线颜色
	private static final int DEFAULT_PADDING = 25;// 默认边距
	private static final int DEFAULT_TEXT_SIZE = 15;// 默认将转换为sp

	private int mRingColor;// 环绘制颜色
	private float mRingWidth;// 外环绘制宽
	private int mRaw;// 抽奖列数
	private int mRawColor;// 分隔线颜色
	private float mRawWidth;// 分隔线宽
	private int[] mFranshapedColors;// 扇形区域背景颜色
	private float mPadding;// 边距
	private float mTextSize;// 文字显示大小
	private int mTextColor;// 文字显示颜色
	private String[] mData;// 显示文字体
	private int[] mRes;// 展示图标

	private Paint mPaint;
	private Matrix mMatrix;

	private int x, y;// 点击x,y
	private OnItemClickListener mListener;// 条目点击事件

	public TurntableWheel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(context, attrs);
	}

	public TurntableWheel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TurntableWheel(Context context) {
		this(context, null, 0);
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		mPaint = new Paint();
		mMatrix = new Matrix();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TurntableWheel);
		setRingColor(a.getColor(R.styleable.TurntableWheel_turntablewheel_ring_color, DEFAULT_COLOR));
		setRingWidth(a.getDimension(R.styleable.TurntableWheel_turntablewheel_ring_width, UnitUtils.dip2px(context, DEFAULT_LINE_WIDTH)));

		setRaw(a.getInteger(R.styleable.TurntableWheel_turntablewheel_raw, UN_RES));
		setRawColor(a.getColor(R.styleable.TurntableWheel_turntablewheel_raw_color, DEFAULT_COLOR));
		setRawWidth(a.getDimension(R.styleable.TurntableWheel_turntablewheel_raw_width, UnitUtils.dip2px(context, DEFAULT_LINE_WIDTH)));
		setFranshapedColors(a.getColor(R.styleable.TurntableWheel_turntablewheel_fanshaped_color, Color.WHITE));

		setPadding(a.getDimension(R.styleable.TurntableWheel_turntablewheel_padding, UnitUtils.dip2px(context, DEFAULT_PADDING)));
		setRawTextData(a.getResourceId(R.styleable.TurntableWheel_turntablewheel_raw_text, UN_RES));
		setTextColor(a.getColor(R.styleable.TurntableWheel_turntablewheel_text_color, DEFAULT_COLOR));
		setTextSize(a.getDimension(R.styleable.TurntableWheel_turntablewheel_text_size, UnitUtils.dip2px(context, DEFAULT_TEXT_SIZE)));

		a.recycle();
	}

	/**
	 * 设置外环显示颜色
	 * 
	 * @param color
	 */
	public void setRingColor(int color) {
		this.mRingColor = color;
		invalidate();
	}

	/**
	 * 设置外环绘制线宽
	 * 
	 * @param width
	 */
	public void setRingWidth(float width) {
		this.mRingWidth = width;
		invalidate();
	}

	/**
	 * 设置显示列表
	 * 
	 * @param raw
	 */
	public void setRaw(int raw) {
		this.mRaw = raw;
		invalidate();
	}

	/**
	 * 设置内扇形分隔线颜色
	 * 
	 * @param color
	 */
	public void setRawColor(int color) {
		this.mRawColor = color;
		invalidate();
	}

	/**
	 * 设置文字内边距
	 * 
	 * @param padding
	 */
	public void setPadding(float padding) {
		this.mPadding = padding;
		invalidate();
	}

	/**
	 * 设置内扇形分隔线宽
	 * 
	 * @param width
	 */
	public void setRawWidth(float width) {
		this.mRawWidth = width;
	}

	/**
	 * 设置扇形区域背景颜色
	 * 
	 * @param colors
	 */
	public void setFranshapedColors(int... colors) {
		this.mFranshapedColors = colors;
		invalidate();
	}

	/**
	 * 设置列展示文字
	 * 
	 * @param data
	 */
	public void setRawTextData(String[] data) {
		this.mData = data;
	}

	/**
	 * 设置列展示文字
	 * 
	 * @param arrayRes
	 */
	public void setRawTextData(int arrayRes) {
		if (UN_RES != arrayRes) {
			setRawTextData(getContext().getResources().getStringArray(arrayRes));
		}
	}

	/**
	 * 设置文字颜色
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		this.mTextColor = color;
		invalidate();
	}

	/**
	 * 设置文字尺寸
	 * 
	 * @param textSize
	 */
	public void setTextSize(float textSize) {
		this.mTextSize = textSize;
		invalidate();
	}

	/**
	 * 设置展示图片
	 * 
	 * @param res
	 */
	public void setDrawableRes(int[] res) {
		this.mRes = res;
		invalidate();
	}

	@Override
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		// 获得半径
		float radius = (Math.min(width, height) >> 1);
		// 整个绘制区域
		RectF oval = new RectF(0, 0, width, height);
		// 绘制显示背景
		drawFranshapeBackGround(canvas, oval);
		// 绘外层环颜色宽
		drawRing(canvas, radius);
		// 绘制中间分隔线
		drawCircleDivier(canvas, height, radius);
		// 绘制展示文字
		drawText(canvas, oval, width, height, radius);
		// 绘制展示图片
		drawImage(canvas, oval, radius - mPadding * 2);
	}

	private void drawFranshapeBackGround(Canvas canvas, RectF oval) {
		// 根据环宽减少绘制距离
		oval.left += mRingWidth;
		oval.top += mRingWidth;
		oval.right -= mRingWidth;
		oval.bottom -= mRingWidth;
		for (int i = 0; i < mRaw; i++) {
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setColor(mFranshapedColors[i % mFranshapedColors.length]);
			canvas.drawArc(oval, i * 360 / mRaw, 360 / mRaw, true, mPaint);
		}
	}

	/**
	 * 绘制展示文字
	 * 
	 * @param canvas
	 * @param width
	 * @param height
	 * @param radius
	 */
	private void drawText(Canvas canvas, RectF oval, int width, int height, float radius) {
		mPaint.reset();
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG)); // 设置图形、图片的抗锯齿。可用于线条等。按位或.
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);
		if (null == mData) {
			mData = new String[mRaw];
			for (int i = 0; i < mRaw; i++) {
				mData[i] = "abcde" + String.valueOf(i + 1);
			}
		}
		Path path = null;
		for (int i = 0, degrees = 360 / mRaw; i < mRaw; i++) {
			path = new Path();
			path.addArc(oval, i * degrees, (i + 1) * degrees - 1);
			canvas.drawTextOnPath(mData[i], path, getDrawTextOffsetX(mData[i], radius - mRingWidth, mPaint), mPadding, mPaint);
		}
	}

	/**
	 * 获得绘制文字x轨位置
	 * 
	 * @param text
	 */
	private float getDrawTextOffsetX(String text, float radius, Paint paint) {
		// a²+b²-2abcosC
		int shortLength = (int) Math.sqrt(Math.pow(radius, 2) + Math.pow(radius, 2) - (2 * radius * radius * Math.cos(360 / mRaw * Math.PI / 180)));
		return (shortLength - paint.measureText(text)) / 2;
	}

	/**
	 * 绘制扇形分隔线
	 * 
	 * @param canvas
	 * @param height
	 * @param radius
	 */
	private void drawCircleDivier(Canvas canvas, int height, float radius) {
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mRawWidth);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mRawColor);
		for (int i = 0, degrees = 360 / mRaw; i < mRaw; i++) {
			int dx = (int) (radius + Math.cos((float) (degrees * i) / 360f * 2 * Math.PI) * radius);
			int dy = (int) (radius - Math.sin((float) (degrees * i) / 360f * 2 * Math.PI) * radius);
			// 从0开始绘制中间线体
			canvas.drawLine(dx, dy, radius, height / 2, mPaint);
		}
	}

	/**
	 * 绘制图形
	 * 
	 * @param canvas
	 * @param oval
	 * @param radius
	 */
	private void drawImage(Canvas canvas, RectF oval, float radius) {
		for (int i = 0, degrees = 360 / mRaw; i < mRaw; i++) {
			int dx = (int) (radius + Math.cos((float) (degrees * i + degrees / 2) / 360f * 2 * Math.PI) * radius);
			int dy = (int) (radius - Math.sin((float) (degrees * i + degrees / 2) / 360f * 2 * Math.PI) * radius);
			// 从0开始绘制中间线体
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mRes[i % mRes.length]);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			canvas.save();
			// canvas translate来移动位图,通过matrix 旋转,
			// 如果同时使用canvas的translate,再使用matrix的translate,rotate.计算会混乱
			canvas.translate(dx - width / 2 + 2 * mPadding, dy - height / 2 + 2 * mPadding);
			mPaint.setColor(Color.GREEN);
			mMatrix.reset();
			mMatrix.setRotate(90 - (degrees * i + degrees / 2), width / 2, height / 2);
			canvas.drawBitmap(bitmap, mMatrix, null);
			canvas.restore();
		}
	}

	/**
	 * 绘制外环
	 * 
	 * @param canvas
	 * @param height
	 * @param radius
	 */
	private void drawRing(Canvas canvas, float radius) {
		mPaint.reset();
		// 设置抗锯齿
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mRingWidth);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mRingColor);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - mRingWidth, mPaint);
	}

	/**
	 * 执行转盘动画
	 */
	public void startThrntableAnim() {
		final int randomIndex = new Random().nextInt(mRaw);
		ValueAnimator valueAnimator = ObjectAnimator.ofFloat(this, "rotation", 5 * 360 + randomIndex * (360 / mRaw));
		// 开始,结尾处慢,中间加速
		valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		valueAnimator.setDuration(3 * 1000);
		valueAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				Toast.makeText(getContext(), String.valueOf(randomIndex), Toast.LENGTH_SHORT).show();
			}
		});
		valueAnimator.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = (int) event.getX();
			y = (int) event.getY();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			int ox = (int) event.getX();
			int oy = (int) event.getY();
			// 按下去,与弹起来,都在矩阵内,算点击
			Rect rect = new Rect();
			getDrawingRect(rect);
			if (rect.contains(x, y) && rect.contains(ox, oy) && null != mListener) {
				mListener.onItemClick(this, getClickPosition(rect, ox, oy));
			}
			break;
		default:
			break;
		}
		return true;
	}

	private int getClickPosition(Rect rect, int ox, int oy) {
		// TODO 根据按下去点,获得点击位置
		return 0;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mListener = listener;
	}

	public interface OnItemClickListener {
		void onItemClick(View v, int position);
	}
}
