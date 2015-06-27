package com.ldfs.demo.draw;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by cz on 15/6/27.
 * 绘制元素配置属性
 */
public class ShapeConfig {
    public int mColor;//绘线颜色
    public int mAlpha;//透明度
    public float mStrokeWidth;//边线大小
    public Paint.Style mStyle;//空心/实心
    public float mScale;//对象旋转角度
    //动画属性
    public long mDuration;//绘制时间
    public int mTargetColor;//绘制颜色

    /**
     * 不允许构造对象
     * 推荐使用ShapeConfig.Builder对象
     * @see com.ldfs.demo.draw.ShapeConfig.Builder
     */
    private ShapeConfig(){
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
            mConfig.mStrokeWidth = 1;//默认线宽
            mConfig.mStyle = Paint.Style.FILL;
            mConfig.mScale = 0;
            mConfig.mDuration = 300;
            mConfig.mTargetColor = Color.WHITE;
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
         * 创建一个ShapeConfig对象
         *
         * @return
         */
        public ShapeConfig build() {
            return mConfig;
        }

    }
}
