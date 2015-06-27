package com.ldfs.demo.draw;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by cz on 15/6/23.
 */
public class Ranctangle extends Shape<PointF> {
    public Ranctangle(float x, float y, float stopX, float stopY, float targetX, float targetY, ShapeConfig config) {
        super(x, y, new PointF(stopX, stopY), new PointF(targetX, targetY), config);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(mPoint.x, mPoint.y, mValue.x + (mTarget.x - mValue.x) * mFraction, mValue.y + (mTarget.y - mValue.y) * mFraction, mPaint);
    }
}
