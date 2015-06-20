package com.ldfs.demo.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import com.ldfs.demo.R;
import com.ldfs.demo.widget.Duration;

/**
 * 创建控件状态选择器
 * 
 * @author 扑倒末末
 */
public class SelectorBuilder {

	/**
	 * 根据资源获得对应的状态选择器
	 * 
	 * @param pressDrawable
	 *            :按下显示的drawable
	 * @param defaultDrawable
	 *            :默认显示的drawable
	 * @return
	 */
	public static StateListDrawable getSelector(Drawable pressDrawable, Drawable selectDrawable, Drawable defaultDrawable) {
		StateListDrawable stateListDrawable = new StateListDrawable();
		Field pressState;
		Field selectState;
		Field emptyState;
		try {
			pressState = View.class.getDeclaredField("PRESSED_ENABLED_STATE_SET");
			pressState.setAccessible(true);
			selectState = View.class.getDeclaredField("ENABLED_SELECTED_STATE_SET");
			selectState.setAccessible(true);
			emptyState = View.class.getDeclaredField("EMPTY_STATE_SET");
			emptyState.setAccessible(true);
			if (null != pressState && null != emptyState) {
				// 设置选中状态
				Object select = selectState.get(null);
				if (null != select && null != selectDrawable) {
					stateListDrawable.addState((int[]) select, selectDrawable);
				}
				// 设置按下效果
				Object press = pressState.get(null);
				if (null != press && null != pressDrawable) {
					stateListDrawable.addState((int[]) press, pressDrawable);
				}
				// 设置未按下效果
				Object empty = emptyState.get(null);
				if (null != empty && null != defaultDrawable) {
					stateListDrawable.addState((int[]) empty, defaultDrawable);
				}
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return stateListDrawable;
	}

	public static StateListDrawable getOvalSelector(Context context, int pressColor, int defaultColor) {
		return getSelector(DrawableBuilder.createOvalGradientDrawable(context, pressColor), null, DrawableBuilder.createOvalGradientDrawable(context, defaultColor));
	}

	/**
	 * 将res资源转为selector
	 * 
	 * @param pressRes
	 *            :按下的图片资源
	 * @param defaultRes
	 *            :默认的图片资源
	 */
	public static StateListDrawable getResSelector(Context context, int pressRes, int defaultRes) {
		return getSelector(context.getResources().getDrawable(pressRes), null, context.getResources().getDrawable(defaultRes));
	}

	/**
	 * 获得选中的圆角矩形selector
	 * 
	 * @param context
	 * @param pressRes
	 * @param selectRes
	 * @param defaultRes
	 * @return
	 */
	public static StateListDrawable getRoundRectSelector(Context context, int pressColor, int selectColor, int defaultColor) {
		return getSelector(context.getResources().getDrawable(pressColor), context.getResources().getDrawable(selectColor), context.getResources().getDrawable(defaultColor));
	}

	/**
	 * 获得选中的圆角矩形selector
	 * 
	 * @param context
	 * @param pressRes
	 * @param selectRes
	 * @param defaultRes
	 * @return
	 */
	public static StateListDrawable getRoundRectSelector(Context context, int pressColor, int selectColor, int defaultColor, int bound) {
		int lineColor = context.getResources().getColor(R.color.line);
		return getSelector(DrawableBuilder.createRoundRectsGradientDrawable(context, UnitUtils.dip2px(context, 0.8f), lineColor, pressColor, Duration.NONE, bound),
				DrawableBuilder.createRoundRectsGradientDrawable(context, UnitUtils.dip2px(context, 0.8f), lineColor, selectColor, Duration.NONE, bound),
				DrawableBuilder.createRoundRectsGradientDrawable(context, UnitUtils.dip2px(context, 0.8f), lineColor, defaultColor, Duration.NONE, bound));
	}

}
