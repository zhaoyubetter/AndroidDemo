package com.ldfs.drawable.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by momo on 2015/3/11.
 * 用户图标
 */
public class PersonalDrawable extends AnimDrawable {

    @Override
    public void draw(Canvas canvas) {
        float size = Math.min(width, height) / 9;
        setPaintValue(Color.DKGRAY, Paint.Style.STROKE, size);
        //画头像 ,高一半,宽1/3
        float corverWidth = width / 3 + width / 9 * mFraction;

        float centerY = height / 2;
        //头像左距离
        float left = (width - corverWidth) / 2;
        canvas.drawArc(new RectF(left,
                        size + height / 12 * mFraction,
                        left + corverWidth,
                        size + centerY),
                0, 360, false, mPaint);

        canvas.drawArc(new RectF(size, size + centerY, width - size, height + centerY - size * 2), 180, 180, false, mPaint);


        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width - size, size, size / 2, mPaint);
    }

    public void setFraction(float fraction) {
        this.mFraction = fraction;
        invalidateSelf();
    }

    @Override
    public ValueAnimator.AnimatorUpdateListener getAnimatorUpdateListener() {
        return null;
    }
}
