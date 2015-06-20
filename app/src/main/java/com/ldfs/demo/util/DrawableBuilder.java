package com.ldfs.demo.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.ldfs.demo.widget.Duration;

/**
 * 创建drawable
 * 
 * @author
 * 
 * @Date 2014/5/10
 */
public class DrawableBuilder {
	/**
	 * 创建一个圆形drawable,带指定色值边框
	 * 
	 * @param strokeWidth
	 *            --将自动转为dip
	 * @return
	 */
	public static GradientDrawable createOvalGradientDrawable(Context context, int shape, int fillColor, int strokeWidth, int strokeColor) {
		// 创建drawable
		GradientDrawable gradientDrawable = new GradientDrawable();
		// 绘圆
		gradientDrawable.setShape(shape);
		gradientDrawable.setColor(fillColor);
		if (-1 != strokeWidth && -1 != strokeColor) {
			gradientDrawable.setStroke(UnitUtils.dip2px(context, strokeWidth), strokeColor);
		}
		return gradientDrawable;
	}

	/**
	 * 创建圆角矩阵
	 * 
	 * @param context
	 * @param strokeWidth
	 *            边矩宽
	 * @param strokeColor
	 *            边矩颜色
	 * @param solidColor
	 *            填充色
	 * @param gravity
	 *            :圆角方向 Gravity.FILL为全部方向
	 * @return
	 */
	public static GradientDrawable createRoundRectsGradientDrawable(Context context, float strokeWidth, int strokeColor, int solidColor, Duration duration, float bound) {
		GradientDrawable gradientDrawable = new GradientDrawable();
		gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
		// 绘圆角矩阵
		gradientDrawable.setShape(GradientDrawable.RECTANGLE);
		// 设置矩阵填充色
		gradientDrawable.setColor(solidColor);
		// 设置圆角
		setCornerRadii(gradientDrawable, isGravity(duration, Duration.LEFT_T) ? bound : 0, isGravity(duration, Duration.RIGTH_T) ? bound : 0, isGravity(duration, Duration.RIGTH_B) ? bound : 0,
				isGravity(duration, Duration.LEFT_B) ? bound : 0);
		if (-1 != strokeWidth && Color.TRANSPARENT != strokeColor) {
			gradientDrawable.setStroke((int) strokeWidth, strokeColor);
		}
		return gradientDrawable;
	}

	private static void setCornerRadii(GradientDrawable drawable, float r0, float r1, float r2, float r3) {
		drawable.setCornerRadii(new float[] { r0, r0, r1, r1, r2, r2, r3, r3 });
	}

	/**
	 * 获得填充的圆角矩阵
	 * 
	 * @return
	 */
	public static GradientDrawable createFillRoundRectsGradientDrawable(Context context, int solidColor, Duration duration, float bound) {
		return createRoundRectsGradientDrawable(context, -1, Color.TRANSPARENT, solidColor, duration, bound);
	}

	/**
	 * 创建外距填充的图形
	 * 
	 * @param context
	 * @param strokeWidth
	 * @param strokeColor
	 * @param duration
	 * @param bound
	 * @return
	 */
	public static GradientDrawable createSolidRoundRectGradientDrawable(Context context, float strokeWidth, int strokeColor, Duration duration, int bound) {
		return createRoundRectsGradientDrawable(context, strokeWidth, strokeColor, Color.TRANSPARENT, duration, bound);
	}

	/**
	 * 创建外距填充的图形
	 * 
	 * @param context
	 * @param strokeWidth
	 * @param strokeColor
	 * @param duration
	 * @param bound
	 * @return
	 */
	public static GradientDrawable createSolidRoundRectGradientDrawable(Context context, float strokeWidth, int strokeColor) {
		return createRoundRectsGradientDrawable(context, strokeWidth, strokeColor, Color.TRANSPARENT, Duration.NONE, 0);
	}

	/**
	 * 创建外距填充的图形
	 * 
	 * @param context
	 * @param strokeWidth
	 * @param strokeColor
	 * @param duration
	 * @param bound
	 * @return
	 */
	public static GradientDrawable createSolidRoundRectGradientDrawable(Context context, int strokeColor) {
		return createRoundRectsGradientDrawable(context, UnitUtils.dip2px(context, 1), strokeColor, Color.TRANSPARENT, Duration.NONE, 0);
	}

	/**
	 * 判断当前是否指定方向
	 * 
	 * @param gravity
	 *            当前方向
	 * @param appointGravity
	 *            指定方向
	 * @return
	 */
	private static boolean isGravity(Duration duration, Duration appointDuration) {
		boolean result = (duration == appointDuration || duration == Duration.ALL);
		switch (duration) {
		case LEFT:
			result = (appointDuration == Duration.LEFT_T || appointDuration == Duration.LEFT_B);
			break;
		case RIGHT:
			result = (appointDuration == Duration.RIGTH_T || appointDuration == Duration.RIGTH_B);
			break;
		case TOP:
			result = (appointDuration == Duration.LEFT_T || appointDuration == Duration.RIGTH_T);
			break;
		case BOTTOM:
			result = (appointDuration == Duration.LEFT_B || appointDuration == Duration.RIGTH_B);
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 创建一个圆形,不带边框
	 */
	public static GradientDrawable createOvalGradientDrawable(Context context, int fillColor) {
		return createOvalGradientDrawable(context, GradientDrawable.OVAL, fillColor, -1, -1);
	}

	/**
	 * 创建一个方形图案,不带边框
	 */
	public static GradientDrawable createRetangleGradientDrawable(Context context, int fillColor) {
		return createOvalGradientDrawable(context, GradientDrawable.RECTANGLE, fillColor, -1, -1);
	}
}
