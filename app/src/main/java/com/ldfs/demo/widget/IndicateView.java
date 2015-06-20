package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;

import java.util.ArrayList;

/**
 * Created by cz on 15/6/17.
 * 带指示器的线性布局
 */
public class IndicateView extends LinearLayout {
    private Paint mPaint;
    private float mTextSize;//文字大小
    private int mTextColor;//文字颜色
    private float mPadding;//内边距
    private float mDividerSize;//分隔线宽
    private int mDividerColor;//分隔线颜色
    private int mIndicateColor;//指示器颜色
    private float mIndicateSize;
    private ArrayList<TextView> mIndicatorView;

    public IndicateView(Context context) {
        this(context, null);
    }

    public IndicateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorView = new ArrayList<>();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicateView);
        setTextColor(a.getColor(R.styleable.IndicateView_iv_text_color, Color.WHITE));
        setTextSize(a.getDimensionPixelSize(R.styleable.IndicateView_iv_text_size, UnitUtils.sp2px(context, 12)));
        setTextPadding(a.getDimension(R.styleable.IndicateView_iv_text_padding, UnitUtils.dip2px(context, 6)));
        setDividerColor(a.getColor(R.styleable.IndicateView_iv_divider_color, getResources().getColor(R.color.white)));
        setDividerSize(a.getDimension(R.styleable.IndicateView_iv_divider_size, UnitUtils.dip2px(context, 1)));
        setIndicateColor(a.getColor(R.styleable.IndicateView_iv_indicate_color, getResources().getColor(R.color.green)));
        setIndicateSize(a.getDimension(R.styleable.IndicateView_iv_indicate_size, UnitUtils.dip2px(context, 40)));
        a.recycle();
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        invalidate();
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
        invalidate();
    }

    public void setTextPadding(float padding) {
        this.mPadding = padding;
        invalidate();
    }

    public void setDividerSize(float size) {
        this.mDividerSize = size;
        invalidate();
    }

    public void setDividerColor(int color) {
        this.mDividerColor = color;
        invalidate();
    }

    /**
     * 设置指示器颜色
     *
     * @param color
     */
    public void setIndicateColor(int color) {
        this.mIndicateColor = color;
        invalidate();
    }

    private void setIndicateSize(float size) {
        this.mIndicateSize = size;
        invalidate();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //根据方向移动整体控件位置
        int childCount = getChildCount();
        int orientation = getOrientation();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            if (VERTICAL == orientation) {
                layoutParams.leftMargin = (int) mIndicateSize;
            } else if (HORIZONTAL == orientation) {
                layoutParams.topMargin = (int) mIndicateSize;
            }
            if (childView instanceof TextView) {
                mIndicatorView.add((TextView) childView);
            }
        }
        requestLayout();
    }

    private float getStartX() {
        float startX = 0;
        int orientation = getOrientation();
        if (VERTICAL == orientation) {
            startX = mIndicateSize / 2;
        }
        return startX;
    }

    private float getStartY() {
        float startY = 0;
        int orientation = getOrientation();
        if (HORIZONTAL == orientation) {
            startY = mIndicateSize / 2;
        }
        return startY;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mIndicatorView && !mIndicatorView.isEmpty()) {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            //绘分隔线
            mPaint.setStrokeWidth(mDividerSize);
            mPaint.setColor(mDividerColor);
            float x = getStartX(), y = getStartY();//绘制线的起点,x,y
            canvas.drawLine(x, y, x, getHeight(), mPaint);
            //绘背景
            Rect textRect = new Rect();//文字绘制矩阵
            Rect outRect = new Rect();
            Rect viewTextRect = new Rect();
            int size = mIndicatorView.size();
            //绘文字
            mPaint.setTextSize(mTextSize);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            for (int i = 1; i <= size; i++) {
                TextView textView = mIndicatorView.get(i - 1);
                int viewWidth = textView.getWidth();
                int viewHeight = textView.getHeight();
                CharSequence textViewValue = textView.getText();
                textView.getPaint().getTextBounds(textViewValue.toString(), 0, textViewValue.length(), viewTextRect);
                textView.getHitRect(outRect);
                String text = String.valueOf(i);
                mPaint.setColor(mIndicateColor);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.getTextBounds(text, 0, text.length(), textRect);
                float radius = mPadding + Math.max(textRect.width(), textRect.height()) / 2;
                int orientation = getOrientation();
                float textWidth = mPaint.measureText(text);
                if (VERTICAL == orientation) {
                    canvas.drawCircle(x, outRect.top + +radius- (viewWidth - viewTextRect.height()) / 2, radius, mPaint);
                    mPaint.setColor(mTextColor);
                    canvas.drawText(text, x - textWidth / 2, outRect.top + radius + textRect.height() / 2 - (viewWidth - viewTextRect.height()) / 2, mPaint);
                    canvas.drawLine(0, 0, 0, getHeight(), mPaint);
                    canvas.drawLine(mIndicateSize, 0, mIndicateSize, getHeight(), mPaint);

                    mPaint.setColor(Color.RED);
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(outRect,mPaint);
                    canvas.drawRect(x - textWidth / 2,
                            outRect.top + radius + textRect.height() / 2 - textRect.height(),
                            x - textWidth / 2 + textWidth,
                            outRect.top + radius + textRect.height() / 2, mPaint);
                } else if (HORIZONTAL == orientation) {
                    canvas.drawCircle(outRect.left + radius, y, radius, mPaint);
                    mPaint.setColor(mTextColor);
                    canvas.drawText(text, outRect.left + radius - textWidth / 2, y + textRect.height() / 2, mPaint);
                    canvas.drawLine(0, 0, getWidth(), 0, mPaint);
                    canvas.drawLine(0, mIndicateSize, getWidth(), mIndicateSize, mPaint);
                }
            }

        }
    }


}
