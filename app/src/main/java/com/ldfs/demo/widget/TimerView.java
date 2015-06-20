package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.ldfs.demo.util.LoopList;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by momo on 2015/4/5.
 * 自定义定时控件
 */
public class TimerView extends View {
    private Paint mPaint;
    private int mColor;
    private float mWidth;
    private int angle;//角度
    private LoopList<Integer> mLoopList;
    private float mTextSize;
    private int mTextColor;

    public TimerView(Context context) {
        this(context, null, 0);
    }

    public TimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setColor(Color.RED);
        setWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics));
        setTextColor(Color.RED);
        setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, displayMetrics));

        mLoopList = new LoopList<Integer>();
        mLoopList.offer(90);
        mLoopList.offer(180);
        mLoopList.offer(270);
        mLoopList.offer(360);
    }

    private void setTextColor(int color) {
        this.mTextColor = color;
        invalidate();
    }

    private void setTextSize(float textSize) {
        this.mTextSize = textSize;
        invalidate();
    }

    private void setWidth(float width) {
        this.mWidth = width;
        invalidate();
    }

    private void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    public void setStrokeWidth(float width) {
        this.mWidth = width;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int centerX = width >> 1;
        int centerY = height >> 1;

        int radius = (Math.min(width, height) >> 1);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(new RectF(mWidth, mWidth, width - mWidth, height - mWidth), mPaint);

        int dx = (int) (radius + Math.cos((90 - angle) / 360f * 2 * Math.PI) * radius);
        int dy = (int) (radius - Math.sin((90 - angle) / 360f * 2 * Math.PI) * radius);

        canvas.drawLine(centerX, centerY, dx, dy, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, 5, mPaint);
//        90==15  180==30
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        String text = String.valueOf(angle / 6);
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, (width - bounds.width()) / 2, centerY+bounds.height()/2, mPaint);
    }

    public void startAnim() {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "angle", mLoopList.next());
        animator.setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                angle = (360 == angle) ? 0 : angle;
            }
        });
        animator.start();
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
