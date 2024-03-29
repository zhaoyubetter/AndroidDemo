package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;

/**
 * ViewPager 底部页面选中圆心效果View
 * 
 * @author momo
 * @Date 2014/5/20
 * @Time 13:32
 * @Deprecated use ViewPagerIndicator <li>
 */
public class CircleNavigationBar extends LinearLayout {

	private ViewPager pager = null;
	private Paint paint = null;
	private float offset;
	private int position;
	private int count;
	private boolean showAnimation; // 是否显示移动动画
	private int circleRadius; // 绘制圆半径
	private int defaultColor; // 默认圆心颜色
	private int checkColor; // 选中圆心颜色

	public CircleNavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		paint = new Paint();
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleNavigationBar);
		showAnimation = typedArray.getBoolean(R.styleable.CircleNavigationBar_showAnimation, false);
		// 设置半径
		int radius = typedArray.getInt(R.styleable.CircleNavigationBar_circleRadius, -1);
		circleRadius = (-1 != radius) ? UnitUtils.dip2px(context, radius) : 10;

		// 设置默认圆心颜色
		int defaultColorRes = typedArray.getResourceId(R.styleable.CircleNavigationBar_defaultCircle, -1);
		defaultColor = (-1 != defaultColorRes) ? getResources().getColor(defaultColorRes) : getResources().getColor(R.color.gray);

		// 设置选中圆心颜色
		int checkColorRes = typedArray.getResourceId(R.styleable.CircleNavigationBar_checkCircle, -1);
		checkColor = (-1 != checkColorRes) ? getResources().getColor(checkColorRes) : Color.RED;
		typedArray.recycle();

	}

	public CircleNavigationBar(Context context) {
		this(context, null);
	}

	public void setContentViewPager(ViewPager pager) {
		setContentViewPager(pager, -1);
	}

	public void setContentViewPager(ViewPager pager, int count) {
		this.pager = pager;
		this.count = -1 == count ? pager.getAdapter().getCount() : count;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != pager) {
			int circleWidth = getWidth() / count;
			int circleHeight = getHeight() / 2;
			paint.setAntiAlias(true);
			int selectPosition = offset > 0.5f ? position + 1 : position;
			selectPosition = (offset > 0.5f && position == count - 1) ? 0 : selectPosition;
			for (int i = 0; i < count; i++) {
				paint.setColor(!showAnimation ? ((i == selectPosition) ? defaultColor : checkColor) : defaultColor);
				canvas.drawCircle(i * circleWidth + circleWidth / 2, circleHeight, circleRadius, paint);
			}
			// 绘制移动圆心
			if (showAnimation) {
				paint.setColor(checkColor);
				canvas.drawCircle(position * circleWidth + circleWidth / 2 + offset * (circleWidth), circleHeight, circleRadius, paint);
			}
			paint.reset();
		}
	}

	public void scrollToPosition(int position, float positionOffset) {
		this.position = position % count;
		this.offset = positionOffset;
		invalidate();
	}
}
