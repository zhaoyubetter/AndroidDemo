package com.ldfs.demo.draw;

import android.graphics.Color;
import android.graphics.Paint;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by cz on 15/6/27.
 * 绘制元素配置属性
 */
public class ShapeConfig implements Cloneable {
    public static final int INFINITE = ValueAnimator.INFINITE;//执行完结束
    public static final int RESTART = ValueAnimator.RESTART;//执行完重新执行
    public static final int REVERSE = ValueAnimator.REVERSE;//执行完反向执行
    public int mColor;//绘线颜色
    public int mAlpha;//透明度
    public int mTargetAlpha;//绘制度明度
    public float mStrokeWidth;//边线大小
    public Paint.Style mStyle;//空心/实心
    public float mScale;//对象旋转角度
    //动画属性
    public long mDuration;//绘制时间
    public long mDelayDuration;//执行绘制延持时间
    public int mTargetColor;//绘制颜色
    public int mRepeatCount;//绘制重复次数
    public int mRepeatMode;//绘制模式

    /**
     * 不允许构造对象
     * 推荐使用ShapeConfig.Builder对象
     *
     * @see com.ldfs.demo.draw.ShapeConfig.Builder
     */
    private ShapeConfig() {
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public void setmAlpha(int mAlpha) {
        this.mAlpha = mAlpha;
    }

    public void setmTargetAlpha(int mTargetAlpha) {
        this.mTargetAlpha = mTargetAlpha;
    }

    public void setmStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public void setmStyle(Paint.Style mStyle) {
        this.mStyle = mStyle;
    }

    public void setmScale(float mScale) {
        this.mScale = mScale;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public void setDelayDuration(long mDelayDuration) {
        this.mDelayDuration = mDelayDuration;
    }

    public void setmTargetColor(int mTargetColor) {
        this.mTargetColor = mTargetColor;
    }

    public void setmRepeatCount(int mRepeatCount) {
        this.mRepeatCount = mRepeatCount;
    }

    public void setmRepeatMode(int mRepeatMode) {
        this.mRepeatMode = mRepeatMode;
    }

    @Override
    public ShapeConfig clone() {
        ShapeConfig config = new ShapeConfig();
        config = new ShapeConfig();
        //设置默认取值
        config.mColor = mColor;
        config.mAlpha = mAlpha;//默认为完全不透明
        config.mTargetAlpha = mTargetAlpha;//绘制目标透明度
        config.mStrokeWidth = mStrokeWidth;//默认线宽
        config.mStyle = mStyle;
        config.mScale = mScale;
        config.mDuration = mDuration;
        config.mTargetColor = mTargetColor;
        config.mRepeatMode = mRepeatMode;
        config.mRepeatCount = mRepeatCount;
        return config;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * 绘图元素builder对象
     */
    public static class Builder {

        private ShapeConfig mConfig;

        public Builder() {
            mConfig = new ShapeConfig();
            //设置默认取值
            mConfig.mColor = Color.WHITE;
            mConfig.mAlpha = 0xFF;//默认为完全不透明
            mConfig.mTargetAlpha = 0xFF;//绘制目标透明度
            mConfig.mStrokeWidth = 1;//默认线宽
            mConfig.mStyle = Paint.Style.FILL;
            mConfig.mScale = 0;
            mConfig.mDuration = 300;
            mConfig.mTargetColor = Color.WHITE;
            mConfig.mRepeatMode = INFINITE;
        }

        /**
         * 设置绘制颜色
         *
         * @param color
         * @return builder
         */
        public Builder setColor(int color) {
            mConfig.mColor = color;
            return this;
        }

        /**
         * 设置绘图透明度
         *
         * @param alpha
         * @return builder
         */
        public Builder setAlpha(int alpha) {
            mConfig.mAlpha = alpha;
            return this;
        }

        public Builder setTargetAlpha(int alpha) {
            this.mConfig.mTargetAlpha = alpha;
            return this;
        }

        /**
         * 设置绘制线宽
         *
         * @param width
         * @return builder
         */
        public Builder setStrokeWidth(float width) {
            mConfig.mStrokeWidth = width;
            return this;
        }

        /**
         * 设置绘制样式 @link{#Paint.Style}
         *
         * @param style
         * @return builder
         */
        public Builder setStyle(Paint.Style style) {
            mConfig.mStyle = style;
            return this;
        }

        /**
         * 设置绘制旋转角度
         *
         * @param scale 旋转角度
         * @return builder
         */
        public Builder setScale(float scale) {
            this.mConfig.mScale = scale;
            return this;
        }

        /**
         * 设置动画绘制时间
         *
         * @param duration 动画绘制时间
         * @return builder
         */
        public Builder setDuration(long duration) {
            this.mConfig.mDuration = duration;
            return this;
        }

        /**
         * 设置目标绘制颜色
         *
         * @param color 设置目标颜色值
         * @return builder
         */
        public Builder setTargetColor(int color) {
            this.mConfig.mTargetColor = color;
            return this;
        }

        /**
         * 设置执行动画延持时间
         *
         * @param delayDuration
         * @return
         */
        public Builder setDelayDuration(long delayDuration) {
            this.mConfig.mDelayDuration = delayDuration;
            return this;
        }

        /**
         * 设置配置动画次数
         *
         * @param count
         * @return
         */
        public Builder setRepeatCount(int count) {
            this.mConfig.mRepeatCount = count;
            return this;
        }

        /**
         * 设置配置动画模式
         *
         * @param mode
         * @return
         */
        public Builder setRepeatMode(int mode) {
            this.mConfig.mRepeatMode = mode;
            return this;
        }

        /**
         * 创建一个ShapeConfig对象
         *
         * @return
         */
        public ShapeConfig build() {
            return mConfig;
        }

    }
}
