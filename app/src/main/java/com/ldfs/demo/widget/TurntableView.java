package com.ldfs.demo.widget;

import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;

/**
 * 转盘view
 * 
 * @author momo
 * @Date 2014/10/20
 */
public class TurntableView extends View implements Callback {
	private static final int SET_LOTTERY_DATA = 1;
	private static final int DATA_SIZE = 4;
	private static final int[] DATA_SORT;
	private Handler mHandler;
	private Duration duration;
	private Bitmap mBitmap;// 转盘位置
	private float strokeWidth;// 内边矩宽
	private float itemPadding;// 条目内边跑
	private int mDataLength;// 数据长度
	private int mStartPosition;// 起始滚动位置
	private int mDuration;// 滚动方向
	private int[] mRes;// 加载内容体
	private Paint paint;
	private int moveColor;// 移动颜色
	private int mStopPosition;// 指定停止位置
	private boolean showBlock;// 初始是否显示移动块
	private boolean isPlay;// 开始游戏

	static {
		// em>100话费</item>
		// <item>Iphone6</item>
		// <item>20元话费</item>
		// <item>小米平板</item>
		// <item>10元话费</item>
		// <item>5元话费</item>
		// <item></item>
		// <item>30元话费</item>
		// <item>100积分</item>
		// <item></item>
		// <item>50元话费</item>
		// <item>50积分</ite
		DATA_SORT = new int[] { 1, 3, 0, 10, 7, 2, 4, 5, 8, 11, 6, 9 };
	}

	public TurntableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public TurntableView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TurntableView(Context context) {
		this(context, null, 0);
	}

	/**
	 * 
	 * @param context
	 */
	private void init(Context context, AttributeSet attrs) {
		mStopPosition = -1;
		mHandler = new Handler(this);
		paint = new Paint();
		duration = Duration.LEFT_T;
		strokeWidth = UnitUtils.dip2px(context, 1);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TurntableView);
		setStartPosition(typedArray.getInteger(R.styleable.TurntableView_start_position, 0));
		setDuration(typedArray.getInt(R.styleable.TurntableView_start_direction, 0));
		setItemPadding(typedArray.getDimension(R.styleable.TurntableView_item_padding, UnitUtils.dip2px(context, 1)));
		setMoveColor(typedArray.getColor(R.styleable.TurntableView_turntable_move_color, Color.GRAY));
		typedArray.recycle();
	}

	/**
	 * 设置滚动方向
	 * 
	 * @param int1
	 */
	private void setDuration(int duration) {
		this.mDuration = duration;
		this.duration = Duration.LEFT_T;
	}

	/**
	 * 设置开始位置
	 * 
	 * @param int1
	 */
	private void setStartPosition(int position) {
		this.mStartPosition = position;
	}

	/**
	 * 设置条目内边矩
	 * 
	 * @param dimension
	 */
	private void setItemPadding(float padding) {
		this.itemPadding = padding;
		invalidate();
	}

	/**
	 * 设置移动背景
	 * 
	 * @param color
	 */
	private void setMoveColor(int color) {
		this.moveColor = color;
		invalidate();
	}

	/**
	 * 设置方形转盘数据
	 * @param res
	 */
	public void setTurntableDrawables(int... res) {
		mDataLength = res.length;
		if (0 != mDataLength && 0 == mDataLength % DATA_SIZE) {
			this.mRes = res;
			Bitmap thruableBitmap = createThruableBitmap();
			if (null != thruableBitmap) {
				if (null != mBitmap) {
					mBitmap.recycle();
					mBitmap = null;
				}
				mBitmap = thruableBitmap;
			}
		} else {
			throw new IllegalArgumentException("数据不能组成规则矩阵!数组长度%4==0");
		}
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (null == mBitmap) {
			mBitmap = createThruableBitmap();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != mRes) {
			paint.reset();
			canvas.drawBitmap(mBitmap, null, new Rect(0, 0, getWidth(), getHeight()), paint);
			if (showBlock) {
				RectF rect = getRectByPosition(mDataLength, mStartPosition);
				// 绘边缘圆角矩阵
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(getResources().getColor(R.color.turntable_bg));
				paint.setAntiAlias(true);
				canvas.drawRoundRect(rect, itemPadding, itemPadding, paint);
			}
		}
	}

	/**
	 * 获得转盘bitmap
	 * 
	 * @return
	 */
	private Bitmap createThruableBitmap() {
		Bitmap bitmap = null;
		int width = getWidth();
		int height = getHeight();
		Bitmap createBitmap = null;
		if (0 != width && 0 != height) {
			createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);// 外层位图
			Canvas canvas = new Canvas(createBitmap);
			// 边缘颜色
			for (int i = 0, length = mRes.length; i < length; i++) {
				bitmap = BitmapFactory.decodeResource(getResources(), mRes[i]);
				RectF rect = getRectByPosition(length, i);
				if (null != bitmap && null != rect) {
					// 绘图片
					canvas.drawBitmap(bitmap, null, rect, paint);
					// 绘边缘线
					paint.reset();
					// 绘边缘圆角矩阵
					paint.setStyle(Paint.Style.STROKE);
					paint.setStrokeWidth(strokeWidth);
					paint.setColor(getResources().getColor(R.color.turntable_line));
					paint.setAntiAlias(true);
					canvas.drawRoundRect(rect, itemPadding, itemPadding, paint);
				}
			}
		}
		return createBitmap;
	}

	@Override
	protected void onDetachedFromWindow() {
		// 回收位图
		if (null != mBitmap && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
		super.onDetachedFromWindow();
	}

	private RectF getRectByPosition(int length, int i) {
		int width = getWidth();
		int height = getHeight();
		int raw = length / DATA_SIZE;
		int scale = i % raw;
		int rawWidth = width / (raw + 1);
		int rawHeight = height / (raw + 1);
		// 顺时针或逆时针方位
		boolean isClockwise = (0 == mDuration);// 是否为顺时针行走
		// 跳转到下一个方向
		if (0 != i && i % raw == 0) {
			duration = duration.next(mDuration);
		}
		RectF rect = null;
		switch (duration) {
		case LEFT_T:
			rect = isClockwise ? new RectF(scale * rawWidth, 0, scale * rawWidth + rawWidth, rawHeight) : new RectF(0, scale * rawHeight, rawWidth, scale * rawHeight + rawHeight);
			break;
		case RIGTH_T:
			rect = isClockwise ? new RectF(width - rawWidth, scale * rawHeight, width, scale * rawHeight + rawHeight) : new RectF((raw - scale) * rawWidth, 0, (raw - scale) * rawWidth + rawWidth,
					rawHeight);
			break;
		case RIGTH_B:
			rect = isClockwise ? new RectF((raw - scale) * rawWidth, height - rawHeight, (raw - scale) * rawWidth + rawWidth, height) : new RectF(width - rawWidth, (raw - scale) * rawHeight, width,
					(raw - scale) * rawHeight + rawHeight);
			break;
		case LEFT_B:
			rect = isClockwise ? new RectF(0, (raw - scale) * rawHeight, rawWidth, (raw - scale) * rawHeight + rawHeight) : new RectF(scale * rawWidth, height - rawHeight,
					scale * rawWidth + rawWidth, height);
			break;
		default:
			break;
		}
		// 设置内边距
		if (null != rect) {
			rect.left += itemPadding;
			rect.top += itemPadding;
			rect.right -= itemPadding;
			rect.bottom -= itemPadding;
		}
		return rect;
	}

	/**
	 * 重置起始标记
	 */
	public void reSet(boolean reSet) {
		isPlay = false;
		mStopPosition = reSet ? -1 : DATA_SORT[DATA_SORT.length - 1];
	}

	/**
	 * 开始转盘
	 */
	public void startPlay(final PlayCallback callback) {
		if (!isPlay) {
			isPlay = showBlock = true;
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					while (isPlay) {
						SystemClock.sleep(500);
						mHandler.sendEmptyMessage(-1);
					}
					if (null != callback) {
						callback.playComplate(mStopPosition, mRes[mStopPosition]);
					}
					reSet(true);
				}
			});
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (SET_LOTTERY_DATA == msg.what) {
			mStopPosition = msg.arg1;
		} else if (isPlay) {
			if (mStartPosition++ == mDataLength) {
				mStartPosition = 0;
			}
			invalidate();
			isPlay = mStartPosition != mStopPosition;
		}
		return false;
	}

	/**
	 * 减速过程
	 * 
	 * @param input
	 * @return
	 */
	public float getInterpolation(float input) {
		return (float) (1.0f - (1.0f - input) * (1.0f - input));
	}

	public interface PlayCallback {
		void playComplate(int position, int res);
	}

}
