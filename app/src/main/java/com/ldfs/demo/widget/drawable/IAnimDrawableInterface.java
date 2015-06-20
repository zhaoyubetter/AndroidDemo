package com.ldfs.demo.widget.drawable;

/**
 * Created by momo on 2015/3/12.
 * 动画drawable操作
 */
public interface IAnimDrawableInterface {
    /**
     * 开启动画.
     */
    void startAnim();

    /**
     * 反置动画
     */
    void rvsAnim();

    /**
     * 切换动画状态
     */
    void toggle();
}
