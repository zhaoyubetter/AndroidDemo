package com.ldfs.demo.widget.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by momo on 2015/3/11.
 * 心形drawable
 */
public class LoveDrawable extends AnimDrawable {
    private Path mPath;

    public LoveDrawable() {
        mPath = new Path();
    }


    @Override
    public void draw(Canvas canvas) {
        float size = Math.min(width, height) / 9;
        //中间点, 3*size
        //左侧,右侧. width/3;
        setPaintValue(Color.DKGRAY, Paint.Style.STROKE, size + mFraction * width / 2);

        //外心路径
        mPath.reset();
        mPath.moveTo(size, size * 3);
        mPath.quadTo(width / 4, size, width / 2, size * 3);
        mPath.quadTo(width / 4 * 3, size, width - size, size * 3);
        mPath.quadTo(width, height / 2 + size * 2, width / 2, height);
        mPath.quadTo(0, height / 2 + size * 2, size, size * 3);
        mPath.quadTo(width / 4, size, width / 2, size * 3);

        canvas.save();
        canvas.translate(0, -size);
        canvas.scale(0.5f, 0.5f, width / 2, height / 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(5);
        canvas.drawRect(0, 0, width, height, mPaint);

    }

    @Override
    public ValueAnimator.AnimatorUpdateListener getAnimatorUpdateListener() {
        return null;
    }

}
