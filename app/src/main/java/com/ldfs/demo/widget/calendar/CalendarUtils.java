package com.ldfs.demo.widget.calendar;

import com.ldfs.demo.util.Loger;

import java.util.*;

/**
 * Created by cz on 15/7/16.
 * 日历工具类
 */
public class CalendarUtils {
    private static final int MONTH_COUNT = 11;//月份总数,从0开始

    /**
     * 判断指定年份是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
    }

    /**
     * 根据指定年份,月份,月份偏移量,获得指定月份天数
     *
     * @param year
     * @param month
     * @param offset
     */
    public static int getMonthDay(int year, int month, int offset) {
        if (Math.abs(offset) >= MONTH_COUNT) {
            month %= MONTH_COUNT;
            year += (offset / MONTH_COUNT);
        } else if (0 == month && 0 > offset) {
            month = MONTH_COUNT;
            year--;
        } else if (MONTH_COUNT == month && 0 < offset) {
            month = 0;
            year++;
        } else {
            month += offset;
        }
        return getMonthDay(year, month);
    }

    /**
     * 根据年,月份,获得指定月份天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDay(int year, int month) {
        int day = 0;
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day = 30;
                break;
            case 2:
                day = isLeapYear(year) ? 29 : 28;
                break;
        }
        return day;
    }

    /**
     * 根据毫秒值获得指定时间的起始天数
     *
     * @param timeMillis
     * @return
     */
    public static int getCalendarDays(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        return getCaneldarDays(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }

    /**
     * 获得指定年月日起始天数
     *
     * @param year
     * @param month 月从0开始
     * @return
     */
    public static int getCaneldarDays(int year, int month) {
        int sumDays = 0;
        //添加年份总天数
        for (int i = 1; i < year; i++) {
            sumDays += (isLeapYear(i) ? 366 : 365);
        }
        Loger.i("sum:" + sumDays);
        //添加月份总天数
        for (int i = 0; i < month; i++) {
            sumDays += (getMonthDay(year, i));
        }
        //加天数
        Loger.i("sum:" + sumDays);
        //allDays:735598   735779
        return sumDays++;
    }

    /**
     * 获得两个日历天之间的天数
     *
     * @param day1
     * @param day2
     * @return
     */
    public static ArrayList<CalendarDay> getCalendarDays(CalendarDay day1, CalendarDay day2) {
        ArrayList<CalendarDay> days = new ArrayList<>();
        boolean start = day1.hashCode() < day2.hashCode();
        CalendarDay startDay = start ? day1 : day2;
        CalendarDay stopDay = !start ? day1 : day2;
        for (int i = startDay.month; i <= stopDay.month; i++) {
            int monthDay = getMonthDay(startDay.year, i);
            for (int j = (i == startDay.month ? startDay.day : 1); j <= (i == stopDay.month ? stopDay.day : monthDay); j++) {
                days.add(new CalendarDay(startDay.year, i, j));
            }
        }
        return days;
    }
}
