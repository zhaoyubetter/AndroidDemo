package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ldfs.demo.R;

/**
 * Created by momo on 2015/4/3.
 */
public class SuperTextView extends View {
    public static final int CENTER = 0x00;
    public static final int LEFT = 0x01;
    public static final int TOP = 0x02;
    public static final int RIGHT = 0x04;
    public static final int BOTTOM = 0x08;

    public static final int EDGE = 0x00;
    public static final int NEAR = 0x01;

    public static final int NORMAL = 0x00;
    public static final int BOLD = 0x01;
    public static final int ITALIC = 0x02;
    public static final int UNDERLINE = 0x04;
    private int mGravity;//文字绘制方向
    private int mDivideGravity;//分隔线方向
    private int mTextStyle;//字体样式
    private int mDrawableNear;//drawable方位
    private float mDrawablePadding;//drawable靠近文字时的间距
    private float mPadding;//文字内边距
    private float mDividePadding;//分隔线边距
    private float mDrawableWidth;//drawable宽
    private float mDrawableHeight;//drawable高
    private Drawable[] mDrawables;
    private RectF[] mDrawableRects;
    private String mText;
    private Paint mTextPaint;
    private Paint mDividePaint;

    public SuperTextView(Context context) {
        this(context, null, 0);
    }


    public SuperTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawables = new Drawable[4];
        mDrawableRects = new RectF[4];
        Resources resources = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperTextView);
        setGravity(a.getInt(R.styleable.SuperTextView_tv_gravity, CENTER));
        setTextColor(a.getColor(R.styleable.SuperTextView_tv_text_color, Color.GRAY));
        setDivideGravity(a.getInt(R.styleable.SuperTextView_tv_divide_gravity, 0));
        setDivideWidth(a.getDimension(R.styleable.SuperTextView_tv_divide_width, resources.getDimension(R.dimen.divide_size)));
        setDivideColor(a.getColor(R.styleable.SuperTextView_tv_divide_color, Color.GRAY));
        setDividePadding(a.getDimension(R.styleable.SuperTextView_tv_divide_padding, 0f));
        setTextStyle(a.getInt(R.styleable.SuperTextView_tv_text_style, NORMAL));
        setDrawableNear(a.getInt(R.styleable.SuperTextView_tv_drawable_near, EDGE));
        setDrawablePadding(a.getDimension(R.styleable.SuperTextView_tv_drawable_padding, 0f));
        setTextPadding(a.getDimension(R.styleable.SuperTextView_tv_padding, 0f));
        setDrawableWidth(a.getDimension(R.styleable.SuperTextView_tv_drawable_width, 0f));
        setDrawableHeight(a.getDimension(R.styleable.SuperTextView_tv_drawable_height, 0f));
        setLeftDrawable(a.getResourceId(R.styleable.SuperTextView_tv_left_drawable, 0));
        setTopDrawable(a.getResourceId(R.styleable.SuperTextView_tv_top_drawable, 0));
        setRightDrawable(a.getResourceId(R.styleable.SuperTextView_tv_right_drawable, 0));
        setBottomDrawable(a.getResourceId(R.styleable.SuperTextView_tv_bottom_drawable, 0));
        setTextSize(a.getDimension(R.styleable.SuperTextView_tv_text_size, getTextSizeFromResource(12f)));
        setText(a.getResourceId(R.styleable.SuperTextView_tv_text, 0));
        String text = a.getNonResourceString(R.styleable.SuperTextView_tv_text);
        if (!TextUtils.isEmpty(text)) {
            setText(text);
        }
        a.recycle();
    }

    /**
     * 设置文字方向
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
        invalidate();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(float size) {
        mTextPaint.setTextSize(size);
        invalidate();
    }

    private float getTextSizeFromResource(float size) {
        Context c = getContext();
        Resources r;
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics());
    }


    private void setText(String text) {
        this.mText = text;
        invalidate();
    }

    private void setText(int resId) {
        this.mText = getContext().getString(resId);
        invalidate();
    }

    public void setLeftDrawable(int id) {
        mDrawables[0] = getDrawableFromResource(id);
        invalidate();
    }

    public void setLeftDrawable(Drawable leftDrawable) {
        mDrawables[0] = leftDrawable;
        invalidate();
    }

    public void setTopDrawable(int id) {
        mDrawables[1] = getDrawableFromResource(id);
        invalidate();
    }

    public void setTopDrawable(Drawable topDrawable) {
        mDrawables[1] = topDrawable;
        invalidate();
    }

    public void setRightDrawable(int id) {
        mDrawables[2] = getDrawableFromResource(id);
        invalidate();
    }

    public void setRightDrawable(Drawable rightDrawable) {
        mDrawables[2] = rightDrawable;
        invalidate();
    }

    public void setBottomDrawable(int id) {
        mDrawables[3] = getDrawableFromResource(id);
        invalidate();
    }

    private Drawable getDrawableFromResource(int id) {
        Drawable drawable = null;
        if (0 != id) {
            try {
                drawable = getResources().getDrawable(id);
                setDrawableBounds(drawable);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        return drawable;
    }

    private void setDrawableBounds(Drawable drawable) {
        if (null == drawable) return;
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int width = (0 != mDrawableWidth) ? (int) mDrawableWidth : intrinsicWidth;
        int height = (0 != mDrawableHeight) ? (int) mDrawableHeight : intrinsicHeight;
        drawable.setBounds(0, 0, width, height);
    }

    public void setBottomDrawable(Drawable bottomDrawable) {
        mDrawables[3] = bottomDrawable;
        invalidate();
    }

    public void setDrawableHeight(float height) {
        this.mDrawableHeight = height;
        setDrawablesBounds();
        invalidate();
    }

    public void setDrawableWidth(float width) {
        this.mDrawableWidth = width;
        setDrawablesBounds();
        invalidate();
    }

    /**
     * 重置drawable大小
     */
    private void setDrawablesBounds() {
        int length = mDrawables.length;
        for (int i = 0; i < length; i++) {
            setDrawableBounds(mDrawables[i]);
        }
    }

    public void setTextPadding(float padding) {
        this.mPadding = padding;
    }

    public void setDrawablePadding(float padding) {
        this.mDrawablePadding = padding;
        invalidate();
    }

    /**
     * 设置drawable方位
     *
     * @param near
     */
    public void setDrawableNear(int near) {
        this.mDrawableNear = near;
        invalidate();
    }

    /**
     * 设置字体样式
     *
     * @param style
     */
    public void setTextStyle(int style) {
        this.mTextStyle = style;
        invalidate();
    }

    /**
     * 设置分隔线颜色
     *
     * @param color
     */
    public void setDivideColor(int color) {
        mDividePaint.setColor(color);
        invalidate();
    }

    public void setDivideWidth(float divideWidth) {
        mDividePaint.setStrokeWidth(divideWidth);
        invalidate();
    }

    /**
     * 设置分隔线边距
     *
     * @param padding
     */
    public void setDividePadding(float padding) {
        this.mDividePadding = padding;
        invalidate();
    }

    public void setDivideGravity(int gravity) {
        this.mDivideGravity = gravity;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDrawables(canvas);
        drawText(canvas);
        drawDivide(canvas);
    }

    /**
     * 画drawable
     *
     * @param canvas
     */
    private void drawDrawables(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int length = mDrawables.length;
        for (int i = 0; i < length; i++) {
            if (null == mDrawables[i]) continue;
            float tx = 0, ty = 0;
            int intrinsicWidth = mDrawables[i].getIntrinsicWidth();
            int intrinsicHeight = mDrawables[i].getIntrinsicHeight();
            int drawableWidth = (0 != mDrawableWidth) ? (int) mDrawableWidth : intrinsicWidth;
            int drawableHeight = (0 != mDrawableHeight) ? (int) mDrawableHeight : intrinsicHeight;
            switch (i) {
                case 0:
                    tx = mPadding;
                    ty = height - drawableHeight >> 1;
                    break;
                case 1:
                    tx = width - drawableWidth >> 1;
                    ty = mPadding;
                    break;
                case 2:
                    tx = width - drawableWidth - mPadding;
                    ty = height - drawableHeight >> 1;
                    break;
                case 3:
                    tx = width - drawableWidth >> 1;
                    ty = height - drawableHeight - mPadding;
                    break;
            }
            drawDrawable(canvas, mDrawables[i], tx, ty);
        }
    }

    private void drawDrawable(Canvas canvas, Drawable drawable, float tx, float ty) {
        if (null == drawable) return;
        canvas.save();
        canvas.translate(tx, ty);
        drawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //影响文字绘制
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), textBounds);
        int textWidth = textBounds.width();
        int textHeight = textBounds.height();
        //gravity
        //padding
        float x = 0, y = 0;
        switch (mGravity) {
            case LEFT:
                x = mPadding;
                y = height - textHeight >> 1;
                break;
            case TOP:
                x = width - textWidth >> 1;
                y = mPadding + textHeight;
                break;
            case RIGHT:
                x = width - textWidth - mPadding;
                y = height - textHeight >> 1;
                break;
            case BOTTOM:
                x = width - textWidth >> 1;
                y = height - mPadding;
                break;
            case CENTER:
                x = width - textWidth >> 1;
                y = height - textHeight >> 1;
                break;
        }
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawDivide(Canvas canvas) {
        drawDivide(canvas, mGravity == (mGravity | LEFT),
                mGravity == (mGravity | TOP),
                mGravity == (mGravity | RIGHT),
                mGravity == (mGravity | BOTTOM));
    }

    private void drawDivide(Canvas canvas, boolean drawLeft, boolean drawTop, boolean drawRight, boolean drawBottom) {
        int width = getWidth();
        int height = getHeight();
        if (drawLeft) {
            canvas.drawLine(0, mDividePadding, 0, height - mDividePadding, mDividePaint);
        }
        if (drawTop) {
            canvas.drawLine(mDividePadding, 0, width - mDividePadding, 0, mDividePaint);
        }
        if (drawRight) {
            canvas.drawLine(width, mDividePadding, width, height - mDividePadding, mDividePaint);
        }
        if (drawBottom) {
            canvas.drawLine(mDividePadding, height, width - mDividePadding, height, mDividePaint);
        }
    }


}
