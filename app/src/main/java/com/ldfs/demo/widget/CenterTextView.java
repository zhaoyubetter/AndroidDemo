package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ldfs.demo.R;


/**
 * TextView封装类,角于赞,踩功能
 *
 * @author momo
 * @Date 2014/6/3
 */
public class CenterTextView extends TextView {
    private static final int LEFT = 1;
    private static final int TOP = 2;
    private static final int RIGHT = 3;
    private static final int BOTTOM = 4;
    private int mGravity;

    public CenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenterTextView);
        setCenterGravity(a.getInt(R.styleable.CenterTextView_cv_gravity, LEFT));
        a.recycle();
    }

    /**
     * 设置居中方向
     *
     * @param gravity
     */
    public void setCenterGravity(int gravity) {
        this.mGravity = gravity;
        invalidate();
    }

    public CenterTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterTextView(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setCanvasTranslate(canvas);
        super.onDraw(canvas);
    }

    /**
     * 移动画板位置---使用drawableLeft/drawableRight居中
     *
     * @param canvas
     */
    private void setCanvasTranslate(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[mGravity];
        if (null != drawable) {
            Rect outRect = new Rect();
            CharSequence text = getText();
            if (!TextUtils.isEmpty(text)) {
                getPaint().getTextBounds(text.toString(), 0, text.length(), outRect);
            }
            int textWidth = outRect.width();
            int textHeight = outRect.height();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            switch (mGravity) {
                case LEFT:
                    translation(canvas, textWidth, getPaddingLeft(), intrinsicWidth);
                    break;
                case TOP:
                    translation(canvas, textHeight, getPaddingTop(), intrinsicHeight);
                    break;
                case RIGHT:
                    translation(canvas, textWidth, getPaddingRight(), intrinsicWidth);
                    break;
                case BOTTOM:
                    translation(canvas, textHeight, getPaddingBottom(), intrinsicHeight);
                    break;
            }
        }
    }


    private void translation(Canvas canvas, int textSize, int padding, int drawableSize) {
        int width = getWidth();
        int height = getHeight();
        int translationX = 0, translationY = 0;
        if (LEFT == mGravity || RIGHT == mGravity) {
            //横向
            translationX = width - textSize - padding - drawableSize - getCompoundDrawablePadding() >> 1;
        } else {
            //纵向
            translationY = height - textSize - padding - drawableSize - getCompoundDrawablePadding() >> 1;
        }
        canvas.translate(translationX, translationY);
    }
}
