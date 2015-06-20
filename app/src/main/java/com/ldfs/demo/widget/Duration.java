package com.ldfs.demo.widget;

/**
 * 方向标记
 * 
 * @author momo
 */
public enum Duration {
	LEFT_T, LEFT_B, RIGTH_T, RIGTH_B, ALL, LEFT, RIGHT, TOP, BOTTOM, NONE;
	public Duration next(int flag) {
		Duration duration = null;
		boolean isClockwise = (0 == flag);// 是否为顺时针行走
		switch (this) {
		case LEFT_T:
			duration = (isClockwise ? Duration.RIGTH_T : Duration.LEFT_B);
			break;
		case LEFT_B:
			duration = (isClockwise ? Duration.LEFT_T : Duration.RIGTH_B);
			break;
		case RIGTH_T:
			duration = (isClockwise ? Duration.RIGTH_B : Duration.LEFT_T);
			break;
		case RIGTH_B:
			duration = (isClockwise ? Duration.LEFT_B : Duration.RIGTH_T);
			break;
		default:
			break;
		}
		return duration;
	}
}
