package com.ldfs.demo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间工具类
 * 
 * @author momo
 * @Date 2014/6/4
 */
public class DateUtils {
	public static final int MINUTES = 60 * 1000;// 分毫秒值
	public static final int HOUR = 60 * MINUTES;// 小时毫秒值
	public static final int DAY = 24 * HOUR;// 天毫秒值

	/**
	 * 格式化进度信息
	 * 
	 * @param duration
	 *            时间数
	 * @return
	 */
	public static String getProgressTime(long duration) {
		return getFromat("mm:ss", duration);
	}

	public static String getFormatTime(long duration) {
		return getFromat("MM天dd日 hh:mm:ss", duration);
	}

	/**
	 * 格式化时间值
	 * 
	 * @param format
	 *            格化
	 * @param duration
	 *            时间值
	 * @return
	 */
	public static String getFromat(String format, long duration) {
		return new SimpleDateFormat(format).format(duration);
	}

	/**
	 * 给定时间是否为今天时间值
	 * 
	 * @param startThrntableTime
	 * @return
	 */
	public static boolean isToday(long startThrntableTime) {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), day, 0, 0, 0);
		return startThrntableTime > calendar.getTimeInMillis();
	}
	
	/**
	 * 返回当前时间描述
	 * 
	 * @param currentTime
	 *            当前时间
	 * @return
	 */
	public static String getTimeSummary(long currentTime) {
		long timeMillis = System.currentTimeMillis() - currentTime;
		timeMillis = (0 > timeMillis) ? 0 : timeMillis;
		int scale = 60, i = 0;
		for (timeMillis /= 1000 * scale; timeMillis >= 0; timeMillis /= scale, i++) {
			switch (i) {
			case 0:
				// 分
				if (timeMillis < 3) {
					return "刚刚";
				} else if (timeMillis >= 3 && timeMillis < 60) {
					return timeMillis + "分钟前";
				}
				break;
			case 1:
				// 时
				if (timeMillis < 24) {
					return timeMillis + "小时前";
				}
				scale = 24;
				break;
			case 2:
				// 天
				if (timeMillis < 28) {
					return timeMillis < 7 ? timeMillis + "天前" : timeMillis / 7 == 0 ? "1周前" : timeMillis / 7 + "周前";
				}
				scale = 30;
				break;
			case 3:
				if (timeMillis < 12) {
					return timeMillis + "月前";
				}
				scale = 12;
				break;
			case 4:
				return timeMillis + "年前";
			}
		}
		return null;
	}
}
