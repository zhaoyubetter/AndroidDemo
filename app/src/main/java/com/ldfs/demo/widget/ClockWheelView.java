package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ldfs.demo.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import static com.ldfs.demo.util.UnitUtils.dip2px;

/**
 * Created by momo on 2015/3/29.
 * 时钟进度控件
 */
public class ClockWheelView extends View {
    private Paint mPaint;
    private int mColor;
    private int mWidth;
    private int mBackgroudColor;
    private int mProgress;//当前进度
    private int max;//最大指数
    private int mCount;//旋转周数
    private int maxCount;//最大旋转周数
    private float mScaleFraction;
    private int mTextSize;//设置文字大小

    public ClockWheelView(Context context) {
        this(context, null, 0);
    }

    public ClockWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setColor(Color.WHITE);
        setStrokeWidth(dip2px(context, 2));
        setCircleBackgroudColor(Color.RED);
        setTextSize(getResources().getDimensionPixelSize(R.dimen.small_text));
        setMax(100);
    }

    private void setTextSize(int textSize) {
        this.mTextSize = textSize;
        invalidate();
    }

    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    public void setStrokeWidth(int width) {
        this.mWidth = width;
        invalidate();
    }

    private void setCircleBackgroudColor(int color) {
        this.mBackgroudColor = color;
        invalidate();
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public void setScaleFractioin(float fraction) {
        this.mScaleFraction = fraction;
        invalidate();
    }

    public void startAnim() {
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(max);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(2 * 1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = Integer.valueOf(valueAnimator.getAnimatedValue().toString());
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                mCount++;
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        int radius = Math.min(width, height) / 2;
        int left = radius / 2;
        int top = radius / 2;

        canvas.save();
        canvas.scale(0.5f + mScaleFraction * 0.5f, 0.5f + mScaleFraction * 0.5f, centerX, centerY);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroudColor);
        //画背景
        canvas.drawCircle(centerX, centerY, radius, mPaint);


        //画中间圆心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mWidth);
        mPaint.setColor(mColor);
        canvas.drawCircle(centerX, centerY, radius / 2, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, 5, mPaint);

        //画旋转圈数
        mPaint.setTextSize(mTextSize);
        Rect rect = new Rect();
        String text = String.valueOf(mCount);
        mPaint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, centerX - rect.width() / 2, centerY + rect.height() / 2, mPaint);

        //画固定指针
        canvas.drawLine(centerX, centerY, centerX, centerY / 2, mPaint);
        float angle = 90 - 360 * mProgress / max;
        int dx = (int) (radius / 2 + Math.cos(angle / 360f * 2 * Math.PI) * radius / 2);
        int dy = (int) (radius / 2 - Math.sin(angle / 360f * 2 * Math.PI) * radius / 2);
        //画指定指针
        canvas.translate(centerX / 2, centerX / 2);
        canvas.drawLine(centerX / 2, centerY / 2, dx, dy, mPaint);
        canvas.restore();
    }

}
