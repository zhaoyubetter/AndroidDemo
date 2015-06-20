package com.ldfs.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ldfs.demo.widget.drawable.AnimDrawable;
import com.ldfs.demo.widget.drawable.IAnimDrawableInterface;

/**
 * Created by momo on 2015/3/12.
 * 操作drawable的ImageView
 */
public class DrawableImageView extends ImageView implements IAnimDrawableInterface {
    private AnimDrawable mAnimDrawable;

    public DrawableImageView(Context context) {
        this(context, null, 0);
    }

    public DrawableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 设置drawable
        // dv_drawable_name;//
    }


    @Override
    public void startAnim() {
    }

    @Override
    public void rvsAnim() {
    }

    @Override
    public void toggle() {
    }
}
