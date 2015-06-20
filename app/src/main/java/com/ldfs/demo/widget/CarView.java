package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.ldfs.demo.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * 按具体圆形轨迹绘制
 *
 * @author momo
 * @Date 2015/3/3
 */
public class CarView extends ImageView {

    private Bitmap mCarBitmap;
    private Matrix matrix;
    private int mCount;
    private int mDegress;

    public CarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        matrix = new Matrix();
        mCount = 1;
    }

    public CarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarView(Context context) {
        this(context, null, 0);
    }

    public void setDegress(int degress) {
        this.mDegress = degress;
        invalidate();
    }

    public void setCount(int count) {
        this.mCount = count;
        invalidate();
    }

    public void startAnim() {
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(360);
        valueAnimator.setDuration(3 * 1000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                setDegress((int) (360 * animator.getAnimatedFraction()));
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mCount; i++) {
            drawCarByDegress(canvas, (mDegress + i * (360 / mCount)) % 360);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != mCarBitmap && !mCarBitmap.isRecycled()) {
            mCarBitmap.recycle();
            mCarBitmap = null;
        }
        super.onDetachedFromWindow();
    }

    private void drawCarByDegress(Canvas canvas, int degress) {
        int carWidth = mCarBitmap.getWidth();
        int carHeight = mCarBitmap.getHeight();
        int carSize = Math.min(carWidth, carHeight);
        int radius = Math.min(getWidth(), getHeight()) / 2 - carSize / 2;
        int dx = (int) (radius + Math.cos((float) (90 - degress) / 360f * 2 * Math.PI) * radius);
        int dy = (int) (radius - Math.sin((float) (90 - degress) / 360f * 2 * Math.PI) * radius);
        matrix.reset();
        // 当同时设置setRotate与setTranslate时,setRotate将不会生效.这时应使用post
        matrix.postRotate(degress, carWidth / 2, carHeight / 2);
        matrix.postTranslate(dx, dy);
        canvas.drawBitmap(mCarBitmap, matrix, null);
    }
}
