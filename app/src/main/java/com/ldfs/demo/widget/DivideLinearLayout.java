package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ldfs.demo.util.UnitUtils;

/**
 * 分隔线布局
 *
 * @author momo
 * @Date 2014/8/8
 */
public class DivideLinearLayout extends LinearLayout {
    public final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    public static final int NONE = 0x00;
    public static final int LEFT = 0x01;
    public static final int TOP = 0x02;
    public static final int RIGHT = 0x04;
    public static final int BOTTOM = 0x08;
    private boolean isShowItemDivide;// 显示子条目分隔线

    private float mStrokeWidth;
    private float mItemStrokeWidth;

    private int mDivideColor;
    private int mItemDivideColor;

    private int mDividePadding;
    private int mItemDividePadding;
    private int mGravity;
    private Paint mPaint;

    public DivideLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        setWillNotDraw(false);
        if (null != attrs) {
            initAttribute(context, attrs);
        } else {
            setUp(context);
        }
    }

    public DivideLinearLayout(Context context) {
        this(context, null);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        // 这种方式可以定位系统,以及其他自定义的attributeSet属性.
        // context.obtainStyledAttributes这种方式,只能索取固定属性集,但可以避过attrs为null自动设置默认值情况,具体使用哪种方式,视使用情况而定
        setDivideGravity(attrs.getAttributeIntValue(MATERIALDESIGNXML, "divier_gravity", NONE));
        showItemDivide(attrs.getAttributeBooleanValue(MATERIALDESIGNXML, "divier_show", false));
        // 线宽
        setStrokeWidth(attrs.getAttributeFloatValue(MATERIALDESIGNXML, "divier_width", UnitUtils.dip2px(context, 1)));
        setItemStrokeWidth(attrs.getAttributeFloatValue(MATERIALDESIGNXML, "item_divier_width", UnitUtils.dip2px(context, 1)));
        // 绘制颜色
        setDivideColor(getResourceColor(attrs, "divier_color", Color.GRAY));
        setItemDivideColor(getResourceColor(attrs, "item_divier_color", Color.GRAY));

        // 边距
        setDividePadding((int) attrs.getAttributeFloatValue(MATERIALDESIGNXML, "divier_padding", 0));
        setItemDividePadding((int) attrs.getAttributeFloatValue(MATERIALDESIGNXML, "item_divier_padding", 0));
    }

    /**
     * 设置默认属性集
     */
    private void setUp(Context context) {
        int strokeWidth = UnitUtils.dip2px(context, 1);
        setDivideGravity(NONE);
        setStrokeWidth(strokeWidth);
        setItemStrokeWidth(strokeWidth);
        setDivideColor(Color.GRAY);
        setItemDivideColor(Color.GRAY);
    }

    private int getResourceColor(AttributeSet attrs, String attrName, int defaultColor) {
        int color = attrs.getAttributeResourceValue(MATERIALDESIGNXML, attrName, defaultColor);
        if (defaultColor == color) {
            color = attrs.getAttributeIntValue(MATERIALDESIGNXML, attrName, defaultColor);
        } else {
            color = getResources().getColor(color);
        }
        return color;
    }

    /**
     * 显示条目分隔线
     *
     * @param showItemDivide
     */
    public void showItemDivide(boolean showItemDivide) {
        this.isShowItemDivide = showItemDivide;
        invalidate();
    }

    public void setDivideGravity(int gravity) {
        this.mGravity = gravity;
        invalidate();
    }

    /**
     * 设置分隔线宽
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        invalidate();
    }

    /**
     * 设置条目分隔线宽
     *
     * @param strokeWidth
     */
    public void setItemStrokeWidth(float strokeWidth) {
        this.mItemStrokeWidth = strokeWidth;
        invalidate();
    }

    public void setDivideColor(int color) {
        this.mDivideColor = color;
        invalidate();
    }

    public void setItemDivideColor(int color) {
        this.mItemDivideColor = color;
        invalidate();
    }

    /**
     * 设置绘制线边距
     *
     * @param padding
     */
    public void setDividePadding(int padding) {
        this.mDividePadding = padding;
        invalidate();
    }

    /**
     * 设置条目分隔线边距
     *
     * @param padding
     */
    public void setItemDividePadding(int padding) {
        this.mItemDividePadding = padding;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制周边分隔线
        drawDivide(canvas);
        // 画divider
        drawItemDivide(canvas);
    }

    private void drawDivide(Canvas canvas) {
        drawDivide(canvas, mGravity == (mGravity | LEFT),
                mGravity == (mGravity | TOP),
                mGravity == (mGravity | RIGHT),
                mGravity == (mGravity | BOTTOM));
    }

    private void drawDivide(Canvas canvas, boolean drawLeft, boolean drawTop, boolean drawRight, boolean drawBottom) {
        mPaint.reset();
        mPaint.setColor(mDivideColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        int width = getWidth();
        int height = getHeight();
        if (drawLeft) {
            canvas.drawLine(0, mDividePadding, 0, height - mDividePadding, mPaint);
        }
        if (drawTop) {
            canvas.drawLine(mDividePadding, 0, width - mDividePadding, 0, mPaint);
        }
        if (drawRight) {
            canvas.drawLine(width, mDividePadding, width, height - mDividePadding, mPaint);
        }
        if (drawBottom) {
            canvas.drawLine(mDividePadding, height, width - mDividePadding, height, mPaint);
        }
    }

    /**
     * 绘制条目分隔线
     *
     * @param canvas
     */
    private void drawItemDivide(Canvas canvas) {
        if (isShowItemDivide) {
            mPaint.reset();
            mPaint.setColor(mItemDivideColor);
            mPaint.setStrokeWidth(mItemStrokeWidth);
            int childCount = getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View childView = getChildAt(i);
                int right = childView.getRight();
                canvas.drawLine(right, mItemDividePadding, right, getHeight() - mItemDividePadding, mPaint);
            }
        }
    }

}
