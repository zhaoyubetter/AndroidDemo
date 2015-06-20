package com.ldfs.demo.widget.scrollview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class MyScrollerView2 extends FrameLayout {

	private float mLastMotionX;
	private int mActivePointerId;
	// 滚动阀值,x的move距离超出该值时才视为一个touch的move行为
	private int mTouchSlop;
	private boolean mIsBeingDragged;

	////////////////////////	Scroller-Fling	///////////////////////////////
	private static final int INVALID_POINTER = -1;
	private VelocityTracker mVelocityTracker;
	private Scroller mScroller;
	private int mMinimumVelocity;
	private int mMaximumVelocity;

	public MyScrollerView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initScrollView();
	}

	public MyScrollerView2(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MyScrollerView2(Context context) {
		this(context, null);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		initVelocityTrackerIfNotExists();
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();

		switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				mIsBeingDragged = getChildCount() != 0;
				if (!mIsBeingDragged) {
					return false;
				}
			
            /*
             * If being flinged and user touches, stop the fling. isFinished
             * will be false if being flinged.
             */
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
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

					// 替换了ScrollView的overScrollBy方法,
					// 即不考虑overScroll情况直接scrollTo滚动了
					scrollTo((int) (scrollX), getScrollY());
				}
				break;
			////////////////////////	Scroller-Fling	///////////////////////////////
			case MotionEvent.ACTION_UP:
				if (mIsBeingDragged) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					int initialVelocity = (int) velocityTracker
							.getXVelocity(mActivePointerId);

					if (getChildCount() > 0) {
						// 速度超过某个阀值时才视为fling
						if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
							fling(-initialVelocity);
						}
					}

					mActivePointerId = INVALID_POINTER;
					endDrag();
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



	////////////////////////	Scroller-Fling	///////////////////////////////

	private void endDrag() {
		mIsBeingDragged = false;

		recycleVelocityTracker();
	}

	private void recycleVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	/**
	 * Fling the scroll view
	 *
	 * @param velocityX
	 *            The initial velocitX in the X direction. Positive numbers mean
	 *            that the finger/cursor is moving down the screen, which means
	 *            we want to scroll towards the top.
	 */
	public void fling(int velocityX) {
		if (getChildCount() > 0) {

			mScroller.fling(getScrollX(), getScrollY(),
					velocityX, 0,
					0, getScrollRangeX(),
					0, 0);

			invalidate();
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldX = getScrollX();
			int oldY = getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();

			if (oldX != x || oldY != y) {
				scrollTo(x, y);
			}

			// Keep on drawing until the animation has finished.
			invalidate();
			return;
		}
	}

	private void initScrollView() {
		mScroller = new Scroller(getContext());
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(configuration);
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}

	private void initVelocityTrackerIfNotExists() {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
	}
}
