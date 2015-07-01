package com.ldfs.demo.draw.shape;

import android.graphics.Canvas;

import com.ldfs.demo.draw.ShapeConfig;

/**
 * Created by cz on 15/6/27.
 */
public class Text extends Shape<Float> {
    private String mText;

    public Text(String text, float x, float y, Float textSize, Float target, ShapeConfig config) {
        super(x, y, textSize, target, config);
        this.mText = text;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //设定字体大小
        mPaint.setTextSize(mValue + (mTarget - mValue) * mFraction);
        canvas.drawText(mText, mPoint.x, mPoint.y, mPaint);
    }
}
