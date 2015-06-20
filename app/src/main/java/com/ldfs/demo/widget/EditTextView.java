package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import static com.ldfs.demo.util.UnitUtils.dip2px;

/**
 * Created by momo on 2015/3/28.
 * 演示文字控件
 */
public class EditTextView extends EditText {
    private float mWdith;
    private int mColor;
    private Paint mPaint;

    public EditTextView(Context context) {
        this(context, null, 0);
    }

    public EditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setColor(Color.RED);
        setStrokeWidth(dip2px(context, 1));
    }

    private void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    private void setStrokeWidth(int width) {
        this.mWdith = width;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getMeasuredHeight();
        //获得文字宽高
        TextPaint paint = getPaint();
        Editable text = getText();
        Rect rect = new Rect();
        paint.getTextBounds(text.toString(), 0, 10, rect);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        //绘制高度
        int h = height - top - bottom;
        int texth = rect.height();
        int lineCount = h / texth;
        int lineHeight = getLineHeight();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mWdith);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        for (int i = 0; i < lineCount; i++) {
            canvas.drawLine(paddingLeft, top + (i + 1) * lineHeight + mWdith, width - paddingRight, top + (i + 1) * lineHeight + mWdith, mPaint);
        }

    }
}
