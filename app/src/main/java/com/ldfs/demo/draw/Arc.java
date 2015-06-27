package com.ldfs.demo.draw;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by cz on 15/6/23.
 * //扇形对象
 */
public class Arc extends Shape<Integer> {
    public RectF mRect;
    private int mSweepAngle;
    private boolean useCenter;

    public Arc(float x, float y, float stopX, float stopY, int startAngle, int sweepAngle, int targetAngle, boolean useCenter, ShapeConfig config) {
        super(x, y, startAngle, targetAngle, config);
        this.mRect = new RectF(x, y, stopX, stopY);
        this.mSweepAngle = sweepAngle;
        this.useCenter = useCenter;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawArc(mRect, mValue, (mTarget - mSweepAngle) * mFraction, useCenter, mPaint);
    }
}
