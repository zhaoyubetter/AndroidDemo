package com.ldfs.demo.widget.indicator;

import android.graphics.Canvas;
import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * 绘制偏移
 * 
 * @author momo
 * @Date 2015/2/10
 * 
 */
public interface Indicatorable extends OnPageChangeListener {

	void draw(Canvas canvas);
}
