package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.Scroller;

/**
 * Created by momo on 2015/5/10.
 */
public class MyWebView extends WebView {
    private boolean isScroll = true;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    PointF curP = new PointF();
    private boolean isFling;//是否滑动
    private OnFlingListener mListener;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller=new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isFling = false;
                isScroll = true;
                curP.x = event.getX();
                curP.y = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float lastY = event.getY(event.getPointerCount() - 1);
                if (isBottom())
                    isScroll = false;
                if (isBottom() && (curP.y - lastY < 0))
                    isScroll = true;
                if (isTop())
                    isScroll = false;
                if (isTop() && (curP.y - lastY > 0))
                    isScroll = true;
                getParent().requestDisallowInterceptTouchEvent(isScroll);
                break;
            case MotionEvent.ACTION_UP:
                isScroll = false;
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker
                        .getYVelocity(0);
                    // 速度超过某个阀值时才视为fling
                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        //代表滑动了
                        isFling = true;
                    }
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }



    @Override
    public void computeScroll() {
        super.computeScroll();
        if (isFling && isBottom() && null != mListener) {
            isFling=false;
            mListener.onFling();
        }
    }

    public void fling(int scrollRange,int velocityX) {
        mScroller.fling(getScrollX(), getScrollY(),
                0, velocityX,
                0, 0,
                0, scrollRange);
        invalidate();
    }

    private boolean isBottom() {
        float contentHeight = getContentHeight() * getScale();
        float currentHeight = getHeight() + getScrollY();
        return contentHeight - currentHeight < 1;
    }

    private boolean isTop() {
        return getScrollY() == 0;
    }


    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void setOnFlingListener(OnFlingListener listener) {
        this.mListener = listener;
    }

    public interface OnFlingListener {
        void onFling();
    }
}
