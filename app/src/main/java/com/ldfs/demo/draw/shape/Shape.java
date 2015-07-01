package com.ldfs.demo.draw.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.ldfs.demo.draw.ShapeConfig;

/**
 * Created by cz on 15/6/23.
 * 绘图形状对象
 */
public abstract class Shape<V> {
    protected final Paint mPaint;
    public final PointF mPoint;//对象绘制位置
    public float mFraction;//绘制进度
    public final ShapeConfig mConfig;
    public final V mValue;//绘图形状值
    public final V mTarget;//绘图目标值


    public Shape(float x, float y, V v, V target, ShapeConfig config) {
        this.mPoint = new PointF(x, y);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mConfig = config;
        this.mValue = v;
        this.mTarget = target;
    }


    /**
     * 直接绘制方法
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        this.mPaint.setStrokeWidth(mConfig.mStrokeWidth);
        this.mPaint.setStyle(mConfig.mStyle);
        this.mPaint.setColor(evaluate(mFraction, mConfig.mColor, mConfig.mTargetColor));
        this.mPaint.setAlpha((int) (mConfig.mAlpha + (mConfig.mTargetAlpha - mConfig.mAlpha) * mFraction));
    }

    /**
     * 获得颜色渐进值
     *
     * @param fraction
     * @param startColor
     * @param endColor
     * @return
     */
    protected int evaluate(float fraction, int startColor, int endColor) {
        int startInt = startColor;
        int startA = (startInt >> 24);
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endColor;
        int endA = (endInt >> 24);
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }

}
