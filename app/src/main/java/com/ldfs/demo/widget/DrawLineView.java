package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 从一点个绘制到另一个点view
 * 绘制类型:
 * 1:从一个点,以另一点圆心方式绘制
 * 2:从一点,以半圆弧线方式绘制
 * 3:直线绘制
 */
public class DrawLineView extends View {
    public static final int TO_RADIUS = 0;//以另一点为半径
    public static final int TO_CIRCLE = 1;//以另一个点为直径
    public static final int TO_POINT = 2;//直线绘制
    private Paint mPaint;
    private PointF mPoint;
    private PointF mTargetPoint;
    private int mDegress;
    private int mType;

    public DrawLineView(Context context) {
        this(context, null, 0);
    }

    public DrawLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
        this.mPoint = new PointF();
        this.mTargetPoint = new PointF();
    }

    /**
     * 设置绘制类型
     *
     * @param type {@link DrawLineView#TO_RADIUS/TO_CIRCLE/TO_POINT}
     */
    public void setDrawType(int type) {
        this.mType = type;
        invalidate();
    }

    /**
     * 开始绘制动画
     */
    private void startAnim(int value) {
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(value);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mDegress = Integer.valueOf(valueAnimator.getAnimatedValue().toString());
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(10);
        float radius = 0;
        canvas.save();
        canvas.translate(Math.min(mPoint.x, mTargetPoint.x), Math.min(mPoint.y, mTargetPoint.y));
        switch (mType) {
            case TO_RADIUS:
                radius = Math.abs(mPoint.x - mTargetPoint.x) / 2;
                for (int i = 0; i < mDegress; i++) {
                    int dx = (int) (radius + Math.cos((float) i / 360f * 2 * Math.PI) * radius);
                    int dy = (int) (radius - Math.sin((float) i / 360f * 2 * Math.PI) * radius);
                    canvas.drawPoint(dx, dy, mPaint);
                }
                break;
            case TO_CIRCLE:
                radius = Math.abs(mPoint.x - mTargetPoint.x);
                for (int i = 0; i < mDegress; i++) {
                    int dx = (int) (radius + Math.cos((float) i / 360f * 2 * Math.PI) * radius);
                    int dy = (int) (radius - Math.sin((float) i / 360f * 2 * Math.PI) * radius);
                    canvas.drawPoint(dx, dy, mPaint);
                }
                break;
            case TO_POINT:
                float dx = 0, dy = 0;
                float width = Math.abs(mPoint.x - mTargetPoint.x);
                float height = Math.abs(mPoint.y - mTargetPoint.y);
                //绘制长度
                int length = (int) Math.max(width, height);
                //短点的递增点
                float value = Math.min(width, height) / length;
                //确定方位
                for (int i = 0; i < mDegress; i++) {
                    if (mPoint.x < mTargetPoint.x) {
                        if (width > height) {
                            dx += 1;
                            dy += value;
                        } else {
                            dy = mPoint.y;
                            dx += value;
                            dy += 1;
                        }
                    } else {
                        dx = mTargetPoint.x;
                        dy = mTargetPoint.y;
                        if (width > height) {
                            dx -= 1;
                            dy -= value;
                        } else {
                            dx -= value;
                            dy -= 1;
                        }
                    }
                    canvas.drawPoint(dx, dy, mPaint);
                }
                break;
            default:
                break;
        }
        canvas.restore();
        //绘查看标记
        canvas.drawCircle(mPoint.x, mPoint.y, 10f, mPaint);
        canvas.drawCircle(mTargetPoint.x, mTargetPoint.y, 10f, mPaint);

        if (TO_POINT != mType) {
            canvas.drawLine(mPoint.x, mPoint.y, mTargetPoint.x, mTargetPoint.y, mPaint);

            canvas.drawLine(mPoint.x, mPoint.y, mPoint.x, mPoint.y + radius, mPaint);
            canvas.drawLine(mTargetPoint.x, mTargetPoint.y, mTargetPoint.x, mTargetPoint.y + radius, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPoint.x = event.getX();
                mPoint.y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mTargetPoint.x = event.getX();
                mTargetPoint.y = event.getY();
                switch (mType) {
                    case TO_RADIUS:
                    case TO_CIRCLE:
                        startAnim(360);
                        break;
                    case TO_POINT:
                        //绘制长度
                        startAnim((int) Math.max(Math.abs(mPoint.x - mTargetPoint.x), Math.abs(mPoint.y - mTargetPoint.y)));
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

}
