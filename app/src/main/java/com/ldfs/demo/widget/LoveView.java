package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 心形绘制view
 */
public class LoveView extends View {
    private Paint mPaint;
    private int width, height;

    public LoveView(Context context) {
        this(context, null, 0);
    }

    public LoveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setWidth(int width) {
        this.width = width;
        invalidate();
    }

    public void setHeight(int height) {
        this.height = height;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);
        int i, j;
        double x, y, r;

//        x = r * Math.cos(Math.PI / 45 * j)  * Math.sin(Math.PI / 45 * i) + 320 / 2;
//        y = -r * Math.sin(Math.PI / 45 * j) + 400 / 4;

        for (i = width; i <= 90;i++ ) {
            for (j = height; j <= 90;j++ ) {
                r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j))
                        * 20;
                x = r * Math.cos(Math.PI / 45 * j)
                        * Math.sin(Math.PI / 45 * i) + 320 / 2;
                y = -r * Math.sin(Math.PI / 45 * j) + 400 / 4;
                canvas.drawPoint((float) x, (float) y, mPaint);
            }
        }
    }

}
