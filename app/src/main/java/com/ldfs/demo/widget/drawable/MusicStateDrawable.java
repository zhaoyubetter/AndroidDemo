package com.ldfs.demo.widget.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by momo on 2015/3/10.
 * 音乐播放状态drawable对象
 */
public class MusicStateDrawable extends AnimDrawable {
    private Path mLeftPath;
    private Path mRightPath;
    private int mPadding;
    private int mCenterPadding;
    private float mDegrees;

    public MusicStateDrawable() {
        mLeftPath = new Path();
        mRightPath = new Path();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int width = bounds.width();
        int height = bounds.height();
        int size = Math.min(width, height) / 2;
        mPadding = size / 4;
        mCenterPadding = size / 10;
    }

    public void setDegress(float degress) {
        this.mDegrees = degress;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        int width = bounds.width();
        int height = bounds.height();
        int centerY = height / 2;
        int radius = Math.min(width, height) / 2;
        int size = (int) Math.sqrt(radius * radius + radius * radius) - mPadding * 2;// 圆内切正方形边长

        int left = (width - size) / 2;
        int top = (height - size) / 2;

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.FILL);
        //边缘圆角
        mPaint.setPathEffect(new CornerPathEffect(10f * (mDegrees * 1f / 90)));

        mLeftPath.reset();
        mRightPath.reset();


        //内边距
        float padding = mCenterPadding * mDegrees * 1f / 90;
        //上边距
        float marginRight = top * mDegrees * 1f / 90f;

        //右边止点
        float rightX = width - marginRight - mPadding * (90 - mDegrees) * 1f / 90;
        mLeftPath.moveTo(left, top);
        mLeftPath.lineTo(rightX, top + (centerY - top) * (90 - mDegrees) * 1f / 90);
        mLeftPath.lineTo(rightX, centerY - padding);
        mLeftPath.lineTo(left, centerY - padding);
        //之所以会有这段,多余的绘制,是因为想让CornerPathEffect四边生效,只有lineTo绘过之后,才能圆角化
        mLeftPath.lineTo(left, top);
        mLeftPath.lineTo(rightX, top + (centerY - top) * (90 - mDegrees) * 1f / 90);

        mRightPath.moveTo(left, top + size);
        mRightPath.lineTo(rightX, centerY + size / 2 * mDegrees * 1f / 90);
        mRightPath.lineTo(rightX, centerY + padding);
        mRightPath.lineTo(left, centerY + padding);


        mRightPath.lineTo(left, top + size);
        mRightPath.lineTo(rightX, centerY + size / 2 * mDegrees * 1f / 90);

        canvas.save();
        canvas.rotate(mDegrees, width / 2, centerY);
        canvas.drawPath(mLeftPath, mPaint);
        canvas.drawPath(mRightPath, mPaint);
        canvas.restore();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2-10, mPaint);
    }

    @Override
    public ValueAnimator.AnimatorUpdateListener getAnimatorUpdateListener() {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setDegress(Float.valueOf(valueAnimator.getAnimatedValue().toString()));
            }
        };
    }
}
