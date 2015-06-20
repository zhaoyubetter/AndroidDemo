package com.ldfs.demo.widget.scrollview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class MyScrollerView1 extends FrameLayout {

	private float mLastMotionX;
	private int mActivePointerId;
	// 滚动阀值,x的move距离超出该值时才视为一个touch的move行为
	// 滚动阀值,x的move距离超出该值时才视为一个touch的move行为
	// 滚动阀值,x的move距离超出该值时才视为一个touch的move行为
	// 滚动阀值,x的move距离超出该值时才视为一个touch的move行为
	// 滚动阀值,x的move距离超出该值时才视为一个touch的move行为
	private int mTouchSlop;
	private boolean mIsBeingDragged;

	public MyScrollerView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ViewConfiguration configuration = ViewConfiguration.get(getContext());
		mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
	}

	public MyScrollerView1(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MyScrollerView1(Context context) {
		this(context, null);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();

		switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				mIsBeingDragged = getChildCount() != 0;
				if (!mIsBeingDragged) {
					return false;
				}

				// Remember where the motion event started
				mLastMotionX = (int) ev.getX();
				mActivePointerId = ev.getPointerId(0);
				break;
			}
			case MotionEvent.ACTION_MOVE:
				final int activePointerIndex = ev
						.findPointerIndex(mActivePointerId);
				final float x = ev.getX(activePointerIndex);
				final int deltaX = (int) (mLastMotionX - x);

				if (!mIsBeingDragged) {
					// 判断是否为一个move行为
					if (Math.abs(deltaX) > mTouchSlop) {
						mIsBeingDragged = true;
					}
				}

				if (mIsBeingDragged) {
					// Scroll to follow the motion event
					mLastMotionX = x;

					float oldScrollX = getScrollX();
					float scrollX = oldScrollX + deltaX;

					// Clamp values if at the limits and record
					final int left = 0;
					final int right = getScrollRangeX();
					// 防止滚动超出边界
					if(scrollX > right) {
						scrollX = right;
					} else if(scrollX < left) {
						scrollX = left;
					}

					// 替换了系统ScrollView的overScrollBy方法,
					// 即不考虑overScroll情况直接scrollTo滚动了
					scrollTo((int) (scrollX), getScrollY());
				}
				break;
		}
		return true;
	}

	/**
	 * 获取x轴向最大滚动范围
	 * @return
	 */
	private int getScrollRangeX() {
		int scrollRange = 0;
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			scrollRange = Math.max(0, child.getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()));
		}
		return scrollRange;
	}
}
