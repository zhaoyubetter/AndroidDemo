package com.ldfs.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.ldfs.demo.util.Loger;

/**
 * Created by momo on 2015/3/23.
 * 控件触摸事件测试
 */
public class TouchView extends View {
    private VelocityTracker mTracker;//伪速度探测
    private int mScaledMaximumFlingVelocity;
    private int mPointId;
    private OnTouchListener mListener;

    public TouchView(Context context) {
        this(context, null, 0);
    }

    public TouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaledMaximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ensureVelocityTracker(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPointId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                mTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                float xVelocity = mTracker.getXVelocity(mPointId);
                float yVelocity = mTracker.getYVelocity(mPointId);
                Loger.i(this, "xVelocity:" + xVelocity + " yVelocity:" + yVelocity);
                if (null != mListener) {
                    mListener.velocityTracker(xVelocity, yVelocity);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                releaseVolocityTracker();
                break;
        }
        return true;
    }

    public void ensureVelocityTracker(MotionEvent event) {
        if (null == mTracker) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
    }

    public void releaseVolocityTracker() {
        if (null != mTracker) {
            mTracker.clear();
            mTracker.recycle();
            mTracker = null;
        }
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.mListener = listener;
    }

    public interface OnTouchListener {
        /**
         * 当前伪速度
         *
         * @param xVelocity
         * @param yVelocity
         */
        void velocityTracker(float xVelocity, float yVelocity);
    }
}
