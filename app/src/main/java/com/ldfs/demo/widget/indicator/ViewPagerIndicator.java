package com.ldfs.demo.widget.indicator;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;

import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.widget.indicator.drawable.CircleIndicatorDrawable;

/**
 * ViewPagerIndicator,viewpager滑动指示器
 * 
 * @author momo
 * @Date 2015/2/9
 * 
 */
public final class ViewPagerIndicator extends View implements OnPageChangeListener {
	// 方向
	private static final int CENTER = 0;
	private static final int LEFT = 1;
	private static final int RIGHT = 2;

	private static final int DEFAULT_CIRCLE_RADIUS = 10;
	private static final int DEFAULT_PADDING = 5;
	private static final int DEFUALT_COLORRES = android.R.color.holo_green_light;
	private static final int ANIM_COLORRES = android.R.color.holo_blue_dark;
	private ViewPager mViewPager;
	private final ArrayList<Indicatorable> mDrawables;
	private OnPageChangeListener mPagerChangeListener;
	private final IndicatorConfig mConfig;
	private int mGravity;// 方向
	private int mCount;

	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDrawables = new ArrayList<Indicatorable>();
		this.mConfig = new IndicatorConfig();
		initAttrs(context, attrs);
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		Resources resources = getResources();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
		setCircleType(a.getInt(R.styleable.ViewPagerIndicator_vi_circle_type, 0));
		setRadius(a.getDimension(R.styleable.ViewPagerIndicator_vi_radius, UnitUtils.dip2px(context, DEFAULT_CIRCLE_RADIUS)));
		setGravity(a.getInt(R.styleable.ViewPagerIndicator_vi_circle_gravity, CENTER));
		setPadding(a.getDimension(R.styleable.ViewPagerIndicator_vi_padding, UnitUtils.dip2px(context, DEFAULT_PADDING)));
		setScaleSize(a.getDimension(R.styleable.ViewPagerIndicator_vi_scale, 0));
		setColor(resources.getColor(a.getResourceId(R.styleable.ViewPagerIndicator_vi_default_color, DEFUALT_COLORRES)));
		setAnimColor(resources.getColor(a.getResourceId(R.styleable.ViewPagerIndicator_vi_anim_color, ANIM_COLORRES)));
		a.recycle();
	}

	/**
	 * 设置缩放比例
	 * 
	 * @param dimension
	 */
	public void setScaleSize(float size) {
		this.mConfig.scaleSize = size;
		invalidate();
	}

	/**
	 * 设置动画绘制颜色
	 * 
	 * @param color
	 */
	public void setAnimColor(int color) {
		this.mConfig.animColor = color;
	}

	/**
	 * 设置绘制颜色
	 * 
	 * @param resourceId
	 */
	public void setColor(int color) {
		this.mConfig.defaultColor = color;
	}

	/**
	 * 设置绘制间隔
	 * 
	 * @param padding
	 */
	public void setPadding(float padding) {
		this.mConfig.padding = padding;
		invalidate();
	}

	/**
	 * 设置绘制方向
	 * 
	 * @param gravity
	 */
	public void setGravity(int gravity) {
		this.mGravity = gravity;
		invalidate();
	}

	/**
	 * 设置圆心半径
	 * 
	 * @param dimension
	 */
	public void setRadius(float radius) {
		this.mConfig.circleRadius = radius;
		this.mConfig.size = radius;
		this.mConfig.mRect.right = radius;
		this.mConfig.mRect.bottom = radius;
		invalidate();
		// TODO 执行属性动画,控件drawable变幻尺寸
	}

	public float getRadius() {
		return this.mConfig.circleRadius;
	}

	/**
	 * 设置绘制圆类型
	 * 
	 * @param type
	 */
	public void setCircleType(int type) {
		this.mConfig.circleType = type;
		invalidate();
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewPagerIndicator(Context context) {
		this(context, null, 0);
	}

	public void setViewPager(ViewPager viewPager) {
		setViewPager(viewPager, 0);
	}

	public void setViewPager(ViewPager viewPager, int count) {
		this.mViewPager = viewPager;
		if (null == mViewPager || null == mViewPager.getAdapter()) {
			throw new NullPointerException("ViewPager/Adapter怎么能为空呢~");
		}
		PagerAdapter adapter = mViewPager.getAdapter();
		mViewPager.setOnPageChangeListener(this);
		mCount = 0 == count ? adapter.getCount() : count;
		for (int i = 0; i < mCount; i++) {
			mDrawables.add(new CircleIndicatorDrawable(i, count, mConfig));
		}
	}

	@Override
	public void onPageScrollStateChanged(int status) {
		if (null != mPagerChangeListener) {
			mPagerChangeListener.onPageSelected(status);
		}
		for (int i = 0; i < mCount; i++) {
			Indicatorable indicatorable = mDrawables.get(i);
			indicatorable.onPageScrollStateChanged(status);
		}
	}

	@Override
	public void onPageScrolled(int position, float offset, int offsetValue) {
		if (null != mPagerChangeListener) {
			mPagerChangeListener.onPageScrolled(position, offset, offsetValue);
		}
		for (int i = 0; i < mCount; i++) {
			Indicatorable indicatorable = mDrawables.get(i);
			indicatorable.onPageScrolled(position, offset, offsetValue);
		}
		this.mConfig.position = position % mCount;
		this.mConfig.positionOffset = offset;
		invalidate();
	}

	@Override
	public void onPageSelected(int position) {
		if (null != mPagerChangeListener) {
			mPagerChangeListener.onPageSelected(position);
		}
		for (int i = 0; i < mCount; i++) {
			Indicatorable indicatorable = mDrawables.get(i);
			indicatorable.onPageSelected(position);
		}
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.mPagerChangeListener = listener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != mViewPager) {
			drawCircle(canvas);
		}
	}

	/**
	 * 绘制圆形
	 * 
	 * @param canvas
	 */
	private void drawCircle(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		int size = (int) (this.mConfig.circleRadius + this.mConfig.padding);
		// 计算出所用矩阵大小
		int drawWidth = size * mCount;
		float scaleSize = this.mConfig.scaleSize / 2;
		for (int i = 0; i < mCount; ++i) {
			float dx, dy = (height - this.mConfig.circleRadius) / 2;
			switch (mGravity) {
			case LEFT:
				dx = i * size + this.mConfig.padding;
				break;
			case RIGHT:
				dx = width - drawWidth + i * size;
				break;
			case CENTER:
			default:
				dx = (width - drawWidth) / 2 + i * size;
				break;
			}
			canvas.save();
			// 根据当前选中条目的scaleSize设置translate取值
			if (i == this.mConfig.position) {
				canvas.translate(dx - (scaleSize * (1f - this.mConfig.positionOffset)), dy - (scaleSize * (1f - this.mConfig.positionOffset)));
			} else if (i == (mCount - 1 == this.mConfig.position ? 0 : this.mConfig.position + 1)) {
				canvas.translate(dx - (scaleSize * this.mConfig.positionOffset), dy - (scaleSize * this.mConfig.positionOffset));
			} else {
				canvas.translate(dx, dy);
			}
			mDrawables.get(i).draw(canvas);
			canvas.restore();
		}
	}

	public static class IndicatorConfig {
		public int defaultColor;// 默认颜色
		public int animColor;// 执行动画颜色
		public float circleRadius;// 半径
		public float padding;// 绘制间隔
		public float scaleSize;// 设置缩放大小
		public float size;// 绘制尺寸
		public int circleType;
		public int position;
		public float positionOffset;
		public RectF mRect;// 当前绘制区域

		public IndicatorConfig() {
			super();
			this.mRect = new RectF();
		}

	}
}
