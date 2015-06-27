package com.ldfs.demo.draw;

import android.graphics.Canvas;

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
        canvas.drawCircle(mPoint.x, mPoint.y, mValue + (mTarget - mValue) * mFraction, mPaint);
    }
}
