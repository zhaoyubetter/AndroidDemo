package com.ldfs.demo.widget.scrollview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.ldfs.demo.util.Loger;

public class MyScrollerView3 extends LinearLayout {

	private static final int INVALID_POINTER = -1;

	private int mOverScrollDistance = 0;
	private int mOverflingDistance = mOverScrollDistance/2;

	private float mLastMotionY;
	private int mActivePointerId;

	private boolean mIsBeingDragged;

	private VelocityTracker mVelocityTracker;
	private OverScroller mScroller;
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private int mTouchSlop;
	private int mScrollY;
	private int mScrollX;


	public MyScrollerView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		initScrollView();
	}

	public MyScrollerView3(Context context) {
		this(context, null);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */

        /*
        * Shortcut the most recurring case: the user is in the dragging
        * state and he is moving his finger.  We want to intercept this
        * motion.
        */
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
			return true;
		}

		switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_MOVE: {
                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                * Locally do absolute value. mLastMotionY is set to the y value
                * of the down event.
                */
				final int activePointerId = mActivePointerId;
				if (activePointerId == INVALID_POINTER) {
					// If we don't have a valid id, the touch down wasn't on content.
					break;
				}

				final int pointerIndex = ev.findPointerIndex(activePointerId);
				final float y = ev.getY(pointerIndex);
				final int yDiff = (int) Math.abs(y - mLastMotionY);
				if (yDiff > mTouchSlop) {
					mIsBeingDragged = true;
					mLastMotionY = y;
				}
				break;
			}

			case MotionEvent.ACTION_DOWN: {
				final float y = ev.getY();
				if (!inChild((int) ev.getX(), (int) y)) {
					mIsBeingDragged = false;
					break;
				}

                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
				mLastMotionY = y;
				mActivePointerId = ev.getPointerId(0);

                /*
                * If being flinged and user touches the screen, initiate drag;
                * otherwise don't.  mScroller.isFinished should be false when
                * being flinged.
                */
				mIsBeingDragged = !mScroller.isFinished();
				break;
			}

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
                /* Release the drag */
				mIsBeingDragged = false;
				mActivePointerId = INVALID_POINTER;
				if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
					invalidate();
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				onSecondaryPointerUp(ev);
				break;
		}

		Loger.i(this,"onInter:"+mIsBeingDragged);
        /*
        * The only time we want to intercept motion events is if we are in the
        * drag mode.
        */
		return mIsBeingDragged;
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
				MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			// TODO: Make this decision more intelligent.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionY = ev.getY(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
			if (mVelocityTracker != null) {
				mVelocityTracker.clear();
			}
		}
	}

	private int getScrollRange() {
		int scrollRange = 0;
			int childCount = getChildCount();
		if (childCount > 0) {
			int height=0;
			for(int i=0;i<childCount;i++){
				View child = getChildAt(i);
				height+=child.getHeight();
			}
			scrollRange = Math.max(0,
					height - (getHeight() - getPaddingBottom() - getPaddingTop()));
		}
		return scrollRange;
	}

	private boolean inChild(int x, int y) {
		if (getChildCount() > 0) {
			final int scrollY = mScrollY;
			final View child = getChildAt(0);
			return !(y < child.getTop() - scrollY
					|| y >= child.getBottom() - scrollY
					|| x < child.getLeft()
					|| x >= child.getRight());
		}
		return false;
	}


	@SuppressLint({ "ClickableViewAccessibility"})
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
				mLastMotionY = (int) ev.getY();
				mActivePointerId = ev.getPointerId(0);
				break;
			}
			case MotionEvent.ACTION_MOVE:
				final int activePointerIndex = ev
						.findPointerIndex(mActivePointerId);
				final float y = ev.getY(activePointerIndex);
				int deltaY = (int) (mLastMotionY - y);

				if (!mIsBeingDragged) {
					// 判断是否为一个可用move的依据
					if (Math.abs(deltaY) > mTouchSlop) {
						mIsBeingDragged = true;
					}

				}

				if (mIsBeingDragged) {
					// Scroll to follow the motion event
					mLastMotionY = y;

					float oldScrollY = getScrollY();
					float scrollY = oldScrollY + deltaY;

					overScrollBy( 0,deltaY,  getScrollX(),(int)scrollY,  0,getScrollRange(),  0,mOverScrollDistance, true);

//				scrollTo((int) (scrollX), getScrollY());
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsBeingDragged) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					int initialVelocity = (int) velocityTracker
							.getYVelocity(mActivePointerId);

					if (getChildCount() > 0) {
						// 速度超过某个阀值时才视为fling
						if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
							fling(-initialVelocity);
						} else {
							// 没有fling时,自定义回弹
							if (mScroller.springBack(getScrollX(), getScrollY(),
									0, getScrollRange(), 0, 0)) {
								postInvalidate();
							}
						}
					}

					mActivePointerId = INVALID_POINTER;
					mActivePointerId = INVALID_POINTER;
					mIsBeingDragged = false;

					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}
					endDrag();
				}
				break;
		}
		Loger.i(this,"onTouch:"+true);
		return true;
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
								  boolean clampedY) {
		// 模拟"扯断效果",即拉到over scroll边界时,弹回去

		if (!mScroller.isFinished()) {
			mScrollX = scrollX;
			mScrollY = scrollY;
			if (clampedY) {
				mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange());
			}
		} else {
			super.scrollTo(scrollX, scrollY);
		}
	}

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
					0, velocityX,
					0, 0,
					0, getScrollRange(),
					mOverflingDistance, 0);


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
		mScroller = new OverScroller(getContext());
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(configuration);
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		setOverScrollMode(OVER_SCROLL_ALWAYS);
	}

	private void initVelocityTrackerIfNotExists() {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
	}
}
