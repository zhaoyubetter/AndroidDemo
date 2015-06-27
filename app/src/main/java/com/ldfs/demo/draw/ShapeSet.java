package com.ldfs.demo.draw;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by cz on 15/6/23.
 * 绘制元素的组合控制类
 */
public class ShapeSet {
    private static final WeakHashMap<View, ShapeSet> mCacheShapeSets;
    private static final WeakHashMap<ShapeSet, View> mCacheViewSet;//用于反向取得view对象

    static {
        mCacheShapeSets = new WeakHashMap<>();
        mCacheViewSet = new WeakHashMap<>();
    }

    private final WeakHashMap<Float, WeakReference<Shape>> mShapes;
    private AnimatorSet mAnimatorSet;
    private Animator mLastAnimator;
    private boolean isStart;

    public ShapeSet() {
        mAnimatorSet = new AnimatorSet();
        mShapes = new WeakHashMap<>();
    }

    /**
     * 获取一个shapeSet对象
     *
     * @param target
     * @return
     */
    public static ShapeSet target(View target) {
        ShapeSet shapeSet = mCacheShapeSets.get(target);
        if (null == shapeSet) {
            shapeSet = new ShapeSet();
            mCacheShapeSets.put(target, shapeSet);
            mCacheViewSet.put(shapeSet, target);
        }
        return shapeSet;
    }


    /**
     * 绘制线体,从绘制起点x,y位置到目录位置targetX,targetY
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param targetX 绘制结束终点 x位置
     * @param targetY 绘制结束终点 y位置
     * @param config  绘制元素属性配置元素
     */
    public ShapeSet line(float x, float y, float targetX, float targetY, ShapeConfig config) {
        return line(x, y, x, y, targetX, targetY, config, false);
    }

    public ShapeSet line(float x, float y, float stopX, float stopY, float targetX, float targetY, ShapeConfig config) {
        return line(x, y, stopX, stopY, targetX, targetY, config, false);
    }

    /**
     * 绘制线体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param stopX   绘制开始终点 x位置
     * @param stopY   绘制开始终点 y位置
     * @param targetX 绘制结束终点 x位置
     * @param targetY 绘制结束终点 y位置
     */
    private ShapeSet line(float x, float y, float stopX, float stopY, float targetX, float targetY, ShapeConfig config, boolean isAfter) {
        PointF stopPoint = new PointF(stopX, stopY);
        float id = x * y * stopPoint.hashCode();
        WeakReference<com.ldfs.demo.draw.Shape> shapeWeakReference = mShapes.get(id);
        if (null == shapeWeakReference || null == shapeWeakReference.get()) {
            mShapes.put(id, new WeakReference<Shape>(new Line(x, y, stopX, stopY, targetX, targetY, config)));
            //初始化绘制控制
            createAnimator(id,config, isAfter);
        }
        return this;
    }

    /**
     * 根据id创建动画绘制控制
     *
     * @param id 确定shape对象的唯一id
     */
    private void createAnimator(final float id, ShapeConfig config, boolean isAfter) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f);
        valueAnimator.setDuration(config.mDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                WeakReference<Shape> shapeReference = mShapes.get(id);
                if (null != shapeReference && null != shapeReference.get()) {
                    Shape shape = shapeReference.get();
                    shape.mFraction = valueAnimator.getAnimatedFraction();
                    View target = mCacheViewSet.get(ShapeSet.this);
                    if (null != target) {
                        target.invalidate();
                    }
                }
            }
        });
        if (null == mLastAnimator) {
            mLastAnimator = valueAnimator;
        } else if (isAfter) {
            mAnimatorSet.play(valueAnimator).after(mLastAnimator);
        } else {
            mAnimatorSet.play(valueAnimator).with(mLastAnimator);
        }
    }

    /**
     * 延持绘制线体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param targetX 绘制结束终点 x位置
     * @param targetY 绘制结束终点 y位置
     */
    public ShapeSet afterLine(float x, float y, float targetX, float targetY, ShapeConfig config) {
        return line(x, y, x, y, targetX, targetY, config, true);
    }

    /**
     * 延持绘制线体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param stopX   绘制终点 x位置
     * @param stopY   绘制终点 y位置
     * @param targetX 绘制目标 x位置
     * @param targetY 绘制目标 y位置
     */
    public ShapeSet afterLine(float x, float y, float stopX, float stopY, float targetX, float targetY, ShapeConfig config) {
        return line(x, y, stopX, stopY, targetX, targetY, config, false);
    }

    /**
     * 绘制圆
     *
     * @param x            绘制起点 x位置
     * @param y            绘制起点 y位置
     * @param targetRadius 绘制目标圆心半径
     */
    public ShapeSet circle(float x, float y, float targetRadius, ShapeConfig config) {
        return circle(x, y, 0, targetRadius, config);
    }

    /**
     * 绘制圆
     *
     * @param x            绘制起点 x位置
     * @param y            绘制起点 y位置
     * @param radius       绘制圆心半径
     * @param targetRadius 绘制目标半径位置
     */
    public ShapeSet circle(float x, float y, float radius, float targetRadius, ShapeConfig config) {
        return circle(x, y, radius, targetRadius, config, false);
    }

    /**
     * 绘制圆
     *
     * @param x            绘制起点 x位置
     * @param y            绘制起点 y位置
     * @param radius       绘制圆心半径
     * @param targetRadius 绘制目标半径位置
     */
    private ShapeSet circle(float x, float y, float radius, float targetRadius, ShapeConfig config, boolean isAfter) {
        float id = x * y * targetRadius;
        WeakReference<Shape> shapeWeakReference = mShapes.get(id);
        if (null == shapeWeakReference || null == shapeWeakReference.get()) {
            mShapes.put(id, new WeakReference<Shape>(new Circle(x, y, radius, targetRadius, config)));
            //初始化绘制控制
            createAnimator(id,config, isAfter);
        }
        return this;
    }


    /**
     * 延持绘制圆
     *
     * @param x            绘制起点 x位置
     * @param y            绘制起点 y位置
     * @param targetRadius 绘制圆心半径
     */
    public ShapeSet afterCircle(float x, float y, float targetRadius, ShapeConfig config) {
        return circle(x, y, 0, targetRadius, config, true);
    }

    /**
     * 延持绘制圆
     *
     * @param x            绘制起点 x位置
     * @param y            绘制起点 y位置
     * @param radius       绘制圆心起点半径
     * @param targetRadius 目标绘绘制半径
     */
    public ShapeSet afterCircle(float x, float y, float radius, float targetRadius, ShapeConfig config) {
        return circle(x, y, radius, targetRadius, config, true);
    }

    /**
     * 绘制扇形体
     *
     * @param x           绘制起点 x位置
     * @param y           绘制起点 y位置
     * @param stopX       绘制终点范围 x位置
     * @param stopY       绘制终点范围 y位置
     * @param startAngle  绘制扇形起点角度
     * @param targetAngle 绘制扇形的终点角度
     * @param useCenter   是否使用中心
     */
    public ShapeSet arc(float x, float y, float stopX, float stopY, int startAngle, int targetAngle, boolean useCenter, ShapeConfig config) {
        return arc(x, y, stopX, stopY, startAngle, startAngle, targetAngle, useCenter, config, false);
    }

    /**
     * 绘制扇形体
     *
     * @param x           绘制起点 x位置
     * @param y           绘制起点 y位置
     * @param stopX       绘制终点范围 x位置
     * @param stopY       绘制终点范围 y位置
     * @param startAngle  绘制扇形起点角度
     * @param sweepAngle  绘制扇形起始绘制角度
     * @param targetAngle 绘制扇形的终点角度
     * @param useCenter   是否使用中心
     */
    public ShapeSet arc(float x, float y, float stopX, float stopY, int startAngle, int sweepAngle, int targetAngle, boolean useCenter, ShapeConfig config) {
        return arc(x, y, stopX, stopY, startAngle, sweepAngle, targetAngle, useCenter, config, false);
    }

    /**
     * 绘制扇形体
     *
     * @param x           绘制起点 x位置
     * @param y           绘制起点 y位置
     * @param stopX       绘制终点范围 x位置
     * @param stopY       绘制终点范围 y位置
     * @param startAngle  绘制扇形起点角度
     * @param sweepAngle  绘制扇形起始绘制角度
     * @param targetAngle 绘制扇形的终点角度
     * @param useCenter   是否使用中心
     */
    private ShapeSet arc(float x, float y, float stopX, float stopY, int startAngle, int sweepAngle, int targetAngle, boolean useCenter, ShapeConfig config, boolean isAfter) {
        float id = x * y * targetAngle;
        WeakReference<Shape> shapeWeakReference = mShapes.get(id);
        if (null == shapeWeakReference || null == shapeWeakReference.get()) {
            mShapes.put(id, new WeakReference<Shape>(new Arc(x, y, stopX, stopY, startAngle, sweepAngle, targetAngle, useCenter, config)));
            //初始化绘制控制
            createAnimator(id,config, isAfter);
        }
        return this;
    }

    /**
     * 延持绘制扇形体
     *
     * @param x           绘制起点 x位置
     * @param y           绘制起点 y位置
     * @param stopX       绘制终点范围 x位置
     * @param stopY       绘制终点范围 y位置
     * @param startAngle  绘制扇形起点角度
     * @param targetAngle 绘制扇形的终点角度
     * @param useCenter   是否使用中心
     */
    public ShapeSet afterArc(float x, float y, float stopX, float stopY, int startAngle, int targetAngle, boolean useCenter, ShapeConfig config) {
        return arc(x, y, stopX, stopY, startAngle, startAngle, targetAngle, useCenter, config, true);
    }

    /**
     * 延持绘制扇形体
     *
     * @param x           绘制起点 x位置
     * @param y           绘制起点 y位置
     * @param stopX       绘制终点范围 x位置
     * @param stopY       绘制终点范围 y位置
     * @param startAngle  绘制扇形起点角度
     * @param sweepAngle  绘制扇形起始绘制角度
     * @param targetAngle 绘制扇形的终点角度
     * @param useCenter   是否使用中心
     */
    public ShapeSet afterArc(float x, float y, float stopX, float stopY, int startAngle, int sweepAngle, int targetAngle, boolean useCenter, ShapeConfig config) {
        return arc(x, y, stopX, stopY, startAngle, sweepAngle, targetAngle, useCenter, config, true);
    }

    /**
     * 绘制矩阵体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param targetX 绘制目标点 x位置
     * @param targetY 绘制目标点 y位置
     */
    public ShapeSet ranctangle(float x, float y, float targetX, float targetY, ShapeConfig config) {
        return ranctangle(x, y, 0, 0, targetX, targetY, config, false);
    }

    /**
     * 绘制矩阵体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param stopX   绘制终点 x位置
     * @param stopY   绘制终点 y位置
     * @param targetX 绘制目标点 x位置
     * @param targetY 绘制目标点 y位置
     */
    public ShapeSet ranctangle(float x, float y, float stopX, float stopY, float targetX, float targetY, ShapeConfig config) {
        return ranctangle(x, y, stopX, stopY, targetX, targetY, config, false);
    }

    /**
     * 绘制矩阵体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param stopX   绘制终点 x位置
     * @param stopY   绘制终点 y位置
     * @param targetX 绘制目标点 x位置
     * @param targetY 绘制目标点 y位置
     */
    private ShapeSet ranctangle(float x, float y, float stopX, float stopY, float targetX, float targetY, ShapeConfig config, boolean isAfter) {
        PointF pointF = new PointF(targetX, targetY);
        float id = x * y * pointF.hashCode();
        WeakReference<Shape> shapeWeakReference = mShapes.get(id);
        if (null == shapeWeakReference || null == shapeWeakReference.get()) {
            mShapes.put(id, new WeakReference<Shape>(new Ranctangle(x, y, stopX, stopY, targetX, targetY, config)));
            //初始化绘制控制
            createAnimator(id,config, isAfter);
        }
        return this;
    }


    /**
     * 延持绘制矩阵体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param targetX 绘制目标点 x位置
     * @param targetY 绘制目标点 y位置
     */
    public ShapeSet afterRanctangle(float x, float y, float targetX, float targetY, ShapeConfig config) {
        return ranctangle(x, y, x, y, targetX, targetY, config, true);
    }


    /**
     * 延持绘制矩阵体
     *
     * @param x       绘制起点 x位置
     * @param y       绘制起点 y位置
     * @param stopX   绘制终点 x位置
     * @param stopY   绘制终点 y位置
     * @param targetX 绘制目标点 x位置
     * @param targetY 绘制目标点 y位置
     */
    public ShapeSet afterRanctangle(float x, float y, float stopX, float stopY, float targetX, float targetY, ShapeConfig config) {
        return ranctangle(x, y, stopX, stopY, targetX, targetY, config, true);
    }

    /**
     * 开始绘制图形
     *
     * @param canvas
     */
    public void darw(Canvas canvas) {
        if (!isStart) {
            isStart = true;
            mAnimatorSet.start();
        }
        for (Map.Entry<Float, WeakReference<Shape>> entry : mShapes.entrySet()) {
            WeakReference<Shape> value = entry.getValue();
            if (null != value && null != value.get()) {
                value.get().draw(canvas);
            }
        }

    }
}
