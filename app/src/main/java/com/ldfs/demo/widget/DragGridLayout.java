package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.ldfs.demo.listener.SimpleAnimatorListener;
import com.ldfs.demo.util.Loger;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.Arrays;
import java.util.List;

/**
 * 可以拖动排序的grid控件组
 *
 * @author momo
 * @Date 2015/2/13
 */
public class DragGridLayout extends FixedGridLayout {
    private int[] mSortPositions;// 排序数组
    private View mSelectView;// 选中控件
    private int mSelectPosition;
    private boolean isDrag;// 是否正在拖动
    private boolean isMove;// 是否开始移动
    private boolean isEnable;// 是否开启拖动

    public DragGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isEnable = true;
        mSortPositions = new int[0];
    }

    public DragGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridLayout(Context context) {
        this(context, null, 0);
    }

    @Override
    public void addView(final View child, final int index, boolean showAnim) {
        super.addView(child, index, showAnim);
        child.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isEnable) {
                    isDrag = true;
                    mSelectView = v;
                    mSelectPosition = mSortPositions[indexOfChild(v)];
                    ViewPropertyAnimator.animate(v).setDuration(150).scaleX(1.3f).scaleY(1.3f).setInterpolator(new AccelerateInterpolator());
                    // TODO 执行一个动画,让控件放大后,居于平滑过度到手指中部
                }
                return true;
            }
        });
        resetPositions(getChildCount(), false);
    }

    /**
     * 重置角标位置
     *
     * @param position
     * @param isRemove
     */
    private void resetPositions(int position, boolean isRemove) {
        int length = mSortPositions.length;
        int[] newPositions = new int[isRemove ? length - 1 : length + 1];
        if (isRemove) {
            for (int i = 0; i < newPositions.length; i++) {
                newPositions[i] = i >= position ? mSortPositions[i + 1] : mSortPositions[i];
                if (newPositions[i] > mSortPositions[position]) {
                    newPositions[i]--;
                }
            }
        } else {
            System.arraycopy(mSortPositions, 0, newPositions, 0, length);
            newPositions[length] = length;
        }
        mSortPositions = newPositions;
    }

    /**
     * 拖动变更频道排序
     */
    public void exchange(int selectIndex, int index) {
        boolean isForward = selectIndex > index;
        int selectPosition = mSortPositions[selectIndex];
        // 向前移
        for (int i = selectIndex; isForward ? i > index : i < index; i = isForward ? i - 1 : i + 1) {
            mSortPositions[i] = mSortPositions[(isForward ? i - 1 : i + 1)];
        }
        mSortPositions[index] = selectPosition;
        Loger.i(this, "positions:" + Arrays.toString(mSortPositions));
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        resetPositions(getChildCount() - 1, true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (isDrag && null != mSelectView) {
                    // 1: 越界限制
                    float[] location = getCheckBorderLocation(x, y);
                    x = location[0];
                    y = location[1];
                    ViewHelper.setX(mSelectView, x);
                    ViewHelper.setY(mSelectView, y);
                    // 2: 检测当前矩阵,与几个绘制矩阵相交,检测两个矩阵之间相交最大度.取得矩阵.移动位置
                    int centerX = mSelectView.getWidth() / 2;
                    int centerY = mSelectView.getHeight() / 2;
                    RectF rect = new RectF(x - centerX, y - centerY, x + centerX, y + centerY);
                    RectF intersectionRect = getIntersectionRect(rect);
                    if (!isMove && null != intersectionRect) {
                        isMove = true;
                        // 3: 以动画移动位置
                        int position = getPosition(intersectionRect.left, intersectionRect.top);
                        if (position != mSelectPosition) {
                            exchange(mSelectPosition, position);
                            moveViews(mSelectPosition, position, mSelectPosition > position);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                this.isDrag = false;
                if (null != mSelectView) {
                    RectF newRect = getRectByPosition(mSelectPosition);
                    ViewPropertyAnimator.animate(this.mSelectView).setDuration(200).scaleX(1f).scaleY(1f).translationX(newRect.left - mSelectView.getLeft())
                            .translationY(newRect.top - mSelectView.getTop()).setInterpolator(new AccelerateInterpolator());
                }
                break;
            default:
                break;
        }

        return super.

                onInterceptTouchEvent(event);

    }

    /**
     * 移动到指定选中控件位置
     *
     * @param selectPosition
     * @param position
     * @param isForward      是否往前移
     */
    private void moveViews(int selectPosition, final int position, boolean isForward) {
        // 执行移动动画.
        List<Rect> layoutRects = getLayoutRects();
        int count = Math.max(position, selectPosition);
        int index = Math.min(position, selectPosition);
        int childCount = getChildCount();
        count = (childCount == count) ? count - 1 : count + 1;
        // 如果index==selectPostion的话,则往后偏移一个
        index = (index == selectPosition) ? index + 1 : index;
        View childView = null;
        Rect oldRect, newRect;
        int translationX, translationY;
        for (int i = index; i < count; i++) {
            childView = getChildAt(i);
            oldRect = layoutRects.get(i);
            //判断角标越界
            newRect = layoutRects.get(isForward ? i + 1 >= childCount ? i : i + 1 : i - 1);
            translationX = newRect.left - oldRect.left;
            translationY = newRect.top - oldRect.top;
            ViewPropertyAnimator.animate(childView).translationX(translationX).translationY(translationY).setDuration(200).setListener(i == count - 1 ? new SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    isMove = false;
                    mSelectPosition = position;
                }
            } : null);
        }
    }

    /**
     * 获得相交范围最大的且中心超过一半的矩阵 一个矩阵,一次只会与周边另一个矩阵相交
     *
     * @param rect
     * @return
     */
    private RectF getIntersectionRect(RectF rect) {
        int position1 = getPosition(rect.left, rect.top);
        int position2 = getPosition(rect.right, rect.top);
        int position3 = getPosition(rect.left, rect.bottom);
        int position4 = getPosition(rect.right, rect.bottom);
        RectF[] rects = new RectF[]{getRectByPosition(position1), getRectByPosition(position2), getRectByPosition(position3), getRectByPosition(position4)};
        RectF intersectionRect = null;
        for (int i = 0; i < rects.length; i++) {
            if (null != rects[i]) {
                if (null != (intersectionRect = isIntersectionRect(rects[i], rect))) {
                    break;
                }
            }
        }
        return intersectionRect;
    }

    /**
     * 比较两个矩阵是否相交
     *
     * @param newRect
     * @param rect
     */
    private RectF isIntersectionRect(RectF newRect, RectF rect) {
        // 查看当前矩阵超出其中哪个矩阵一半,返回其所以位置
        int centerWidth = getItemWidth() / 2;
        int centerHeight = getItemHeight() / 2;
        RectF intersectionRect = null;
        Loger.i(this, "newRect:" + newRect + " rect:" + rect);
        if (Math.abs(newRect.left - rect.left) < centerWidth && Math.abs(newRect.top - rect.top) < centerHeight) {
            intersectionRect = newRect;
        }
        return intersectionRect;
    }

    /**
     * 获得检测越界后的布局矩阵
     *
     * @param x
     * @param y
     * @return
     */
    private float[] getCheckBorderLocation(float x, float y) {
        float[] localtion = new float[2];
        int width = getWidth();
        int height = getHeight();
        int centerX = mSelectView.getWidth() / 2;
        int centerY = mSelectView.getHeight() / 2;
        // 越界检测
        if (x < centerX) {
            x = centerX;
        }
        if (y < centerY) {
            y = centerY;
        }
        if (x > width - centerX) {
            x = width - centerX;
        }
        if (y > height - centerY) {
            y = height - centerY;
        }
        localtion[0] = x - centerX;
        localtion[1] = y - centerY;
        return localtion;
    }

    /**
     * 根据按下位置,判断点击子控件
     *
     * @param x
     * @param y
     * @return
     */
    private int getPosition(float x, float y) {
        int columns = getColumns();
        int itemWidth = getItemWidth();
        int itemHeight = getItemHeight();
        int row = (int) (x / itemWidth);// 所在行
        int column = (int) (y / itemHeight);// 所在列
        return column * columns + row;
    }

    /**
     * 根据元素位置获得布局矩阵
     *
     * @param position
     * @return
     */
    private RectF getRectByPosition(int position) {
        RectF rect = null;
        List<Rect> layoutRects = getLayoutRects();
        if (0 <= position && position < layoutRects.size()) {
            rect = new RectF(layoutRects.get(position));
        }
        return rect;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

}
