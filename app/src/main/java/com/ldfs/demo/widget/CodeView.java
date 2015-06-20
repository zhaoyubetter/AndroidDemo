package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义的代码展示器
 * 
 * @author momo
 * @date 2015/2/14
 */
public class CodeView extends View {
	private final String[] mCode;

	public CodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// 作用域==对应颜色
		mCode = new String[] { "private", "public", "final", "extends", "implements", "this", "super", "null", "true", "new" };
		// 注解
//		@Override @
		// 注释
		// /* */ /** */
		// 成员变量
//		private public final protected 之后元素
	}
	
	

	public CodeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CodeView(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
