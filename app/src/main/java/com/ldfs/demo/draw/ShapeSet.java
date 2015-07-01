package com.ldfs.demo.draw;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.View;

import com.ldfs.demo.draw.shape.Arc;
import com.ldfs.demo.draw.shape.Circle;
import com.ldfs.demo.draw.shape.Line;
import com.ldfs.demo.draw.shape.Ranctangle;
import com.ldfs.demo.draw.shape.Shape;
import com.ldfs.demo.draw.shape.Text;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.HashMap;
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

    private final HashMap<Float, Shape> mShapes;
    private AnimatorSet mAnimatorSet;
    private Animator mLastAnimator;
    private boolean isStart;

    public ShapeSet() {
        mAnimatorSet = new AnimatorSet();
        mShapes = new HashMap<>();
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
        float id = x * y * new PointF(targetX, targetY).hashCode()+config.hashCode();
        Shape cacheShape = mShapes.get(id);
        if (null == cacheShape) {
            cacheShape(id, new Line(x, y, stopX, stopY, targetX, targetY, config), isAfter);
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
        valueAnimator.setRepeatCount(config.mRepeatCount);
        valueAnimator.setRepeatMode(config.mRepeatMode);
        valueAnimator.setStartDelay(config.mDelayDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Shape cacheShape = mShapes.get(id);
                if (null != cacheShape) {
                    cacheShape.mFraction = valueAnimator.getAnimatedFraction();
                    View target = mCacheViewSet.get(ShapeSet.this);
                    if (null != target) {
                        target.invalidate();
                    }
                }
            }
        });
        if (null != mLastAnimator) {
            if (isAfter) {
                mAnimatorSet.play(valueAnimator).after(mLastAnimator);
            } else {
                mAnimatorSet.play(valueAnimator).with(mLastAnimator);
            }
        }
        mLastAnimator = valueAnimator;
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
        float id = x * y * targetRadius * config.hashCode();
        Shape cacheShape = mShapes.get(id);
        if (null == cacheShape) {
            cacheShape(id, new Circle(x, y, radius, targetRadius, config), isAfter);
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
        float id = x * y * targetAngle+config.hashCode();
        Shape cacheShape = mShapes.get(id);
        if (null == cacheShape) {
            cacheShape(id, new Arc(x, y, stopX, stopY, startAngle, sweepAngle, targetAngle, useCenter, config), isAfter);
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
        return ranctangle(x, y, x, y, targetX, targetY, config, false);
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
        float id = x * y * new PointF(x, y).hashCode()+config.hashCode();
        Shape cacheShape = mShapes.get(id);
        if (null == cacheShape) {
            cacheShape(id, new Ranctangle(x, y, stopX, stopY, targetX, targetY, config), isAfter);
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
     * 绘制文字体
     *
     * @param text       绘制文件
     * @param x          绘制起点 x位置
     * @param y          绘制起点 y位置
     * @param textSize   绘制文字大小
     * @param targetSize 绘制文字最终大小
     * @param config     图片配置对象
     * @param isAfter    是否延持执行
     * @return
     */
    private ShapeSet text(String text, float x, float y, float textSize, float targetSize, ShapeConfig config, boolean isAfter) {
        float id = x * y * targetSize+config.hashCode();
        Shape cacheShape = mShapes.get(id);
        if (null == cacheShape) {
            cacheShape(id, new Text(text, x, y, textSize, targetSize, config), isAfter);
        }
        return this;
    }


    /**
     * 绘制文字体
     *
     * @param text       绘制文件
     * @param x          绘制起点 x位置
     * @param y          绘制起点 y位置
     * @param textSize   绘制文字大小
     * @param targetSize 绘制文字最终大小
     * @param config     图片配置对象
     * @return
     */
    public ShapeSet text(String text, float x, float y, float textSize, float targetSize, ShapeConfig config) {
        return text(text, x, y, textSize, targetSize, config, false);
    }


    /**
     * 绘制文字体
     *
     * @param text       绘制文件
     * @param x          绘制起点 x位置
     * @param y          绘制起点 y位置
     * @param targetSize 绘制文字最终大小
     * @param config     图片配置对象
     * @return
     */
    public ShapeSet text(String text, float x, float y, float targetSize, ShapeConfig config) {
        return text(text, x, y, targetSize, targetSize, config, false);
    }


    /**
     * 延持绘制文字体
     *
     * @param text       绘制文件
     * @param x          绘制起点 x位置
     * @param y          绘制起点 y位置
     * @param textSize   绘制文字大小
     * @param targetSize 绘制文字最终大小
     * @param config     图片配置对象
     * @return
     */
    public ShapeSet afterText(String text, float x, float y, float textSize, float targetSize, ShapeConfig config) {
        return text(text, x, y, textSize, targetSize, config, true);
    }


    /**
     * 延持绘制文字体
     *
     * @param text       绘制文件
     * @param x          绘制起点 x位置
     * @param y          绘制起点 y位置
     * @param targetSize 绘制文字最终大小
     * @param config     图片配置对象
     * @return
     */
    public ShapeSet afterText(String text, float x, float y, float targetSize, ShapeConfig config) {
        return text(text, x, y, targetSize, targetSize, config, true);
    }

    /**
     * 绘存shape对象
     *
     * @param id      shape对象的绘存标记id
     * @param shape   shape对象
     * @param isAfter 是否延持执行
     */
    private void cacheShape(float id, Shape shape, boolean isAfter) {
        mShapes.put(id, shape);
        //初始化绘制控制
        createAnimator(id, shape.mConfig, isAfter);
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
        for (Map.Entry<Float, Shape> entry : mShapes.entrySet()) {
            Shape value = entry.getValue();
            if (null != value) {
                value.draw(canvas);
            }
        }

    }
}
