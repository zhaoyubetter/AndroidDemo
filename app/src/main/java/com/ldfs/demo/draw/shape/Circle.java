package com.ldfs.demo.draw.shape;

import android.graphics.Canvas;

import com.ldfs.demo.draw.ShapeConfig;
import com.ldfs.demo.util.Loger;

/**
 * Created by cz on 15/6/23.
 */
public class Circle extends Shape<Float> {

    public Circle(float x, float y, float radius, float targetRadius, ShapeConfig config) {
        super(x, y, radius, targetRadius, config);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Loger.i("alpha:" + mPaint.getAlpha()+" alpha:"+mConfig.mAlpha+" targetAlpha:"+mConfig.mTargetAlpha+" calc:"+((int) (mConfig.mAlpha + (mConfig.mTargetAlpha - mConfig.mAlpha) * mFraction)));
        canvas.drawCircle(mPoint.x, mPoint.y, mValue + (mTarget - mValue) * mFraction, mPaint);
    }
}
