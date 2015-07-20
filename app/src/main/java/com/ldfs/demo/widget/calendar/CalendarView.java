package com.ldfs.demo.widget.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.ldfs.demo.R;
import com.ldfs.demo.util.Loger;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cz on 15/7/16.
 * 日历控件
 * 日历动态控制
 * 1:按下
 * 2:选中
 * 3:标记
 * 4:群选中
 */
public class CalendarView extends View {
    private static final int MONTH_COUNT = 11;//月份总数.从0开始
    private static final int WEEK_COUNT = 7;//一周天数
    public static final int MONTH_MODE = 0x00;//月显示模式
    public static final int WEEK_MODE = 0x01;//周显示模式

    public static final int EAST_MODE = 0x00;//东方时区模式
    public static final int WAST_MODE = 0x01;//西方时区模式

    public static final int NONE_MODE = 0x00;//无背景显示模式
    public static final int CIRCLE_MODE = 0x01;//圆形显示模式
    public static final int RECTANGLE_MODE = 0x02;//方形显示模式

    public static final int UN_CLICK_MODE = 0x00;//无法点击模式
    public static final int CLICK_MODE = 0x01;//点击模式
    public static final int MAST_MODE = 0x02;//标记模式
    public static final int SINGLE_CLICK_MODE = 0x03;//单选模式
    public static final int MULTIPLE_CHOICE = 0x04;//多选模式
    public static final int RECTANGLE_CHOICE = 0x05;//范围选择

    /**
     * 文字标题字体大小
     */
    private float mTitleTextSize;
    /**
     * 文字标题字体颜色
     */
    private int mTitleTextColor;
    /**
     * 日历天字体尺寸
     */
    private float mDayTextSize;
    /**
     * 日历天字体颜色
     */
    private int mDayTextColor;
    /**
     * 日历非当前月天字体尺寸
     */
    private float mUnDayTextSize;
    /**
     * 日历非当前月天字体颜色
     */
    private int mUnDayTextColor;
    /**
     * 日历天选中颜色
     */
    private int mSelectColor;
    /**
     * 日历天标记颜色
     */
    private int maskColor;
    /**
     * 标记大小
     */
    private float maskSize;
    /**
     * 标题字内边距大小
     */
    private float mTitleHeight;
    /**
     * 日历天内边距大小
     */
    private float mDayPadding;
    /**
     * 分隔线颜色
     */
    private int mDivideColor;
    /**
     * 分隔线尺寸
     */
    private float mDivideSize;
    /**
     * 绘制选中天外边距
     */
    private float mSelectDayPadding;
    /**
     * 日历显示模式
     */
    private int mCalendarMode;
    /**
     * 排序模式
     */
    private int mSortMode;
    /**
     * 天选中模式
     */
    private int mDayMode;
    /**
     * 当前显示时间
     */
    private CalendarDay mCalendarDay;
    /**
     * 当前点击模式
     */
    private int mClickMode;
    /**
     * 是否显示日历头
     */
    private boolean showHeader;
    /**
     * 按下动画值
     */
    public float mPressFraction;


    private float HEADER_HEIGHT;
    private final ArrayList<CalendarDay> mSelectDays;//选中集
    private final ArrayList<CalendarDay> maskDays;//标记集
    private final HashMap<RectF, CalendarDay> mDayRects;//当前日期天矩阵与位置集
    private OnCalendarDayClickListener mClickListener;//日历点击监听
    private OnCalendarDayChoiceListener mChoiceListener;//日期点选监听

    private Calendar mCalendar;
    private CalendarDay mLastDay;//上一个操作时间
    private RectF mTouchRect;//当前按下所属矩阵位置

    private String[] mEastWeekTitle;
    private String[] mWestWeekTitle;

    private Paint mPaint;

    private boolean isDebug = false;//debug模式,会打开绘图测试,以及其他.


    public CalendarView(Context context) {
        this(context, null, 0);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEastWeekTitle = getResources().getStringArray(R.array.ease_week);
        mWestWeekTitle = getResources().getStringArray(R.array.wast_week);
        mSelectDays = new ArrayList<>();
        maskDays = new ArrayList<>();
        mDayRects = new HashMap<>();
        mCalendar = Calendar.getInstance();
        mCalendarDay = new CalendarDay();
        mLastDay = new CalendarDay();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        setTitleTextSize(a.getDimension(R.styleable.CalendarView_cd_title_text_size, value2(TypedValue.COMPLEX_UNIT_SP, 12)));
        setTitleTextColor(a.getColor(R.styleable.CalendarView_cd_day_text_color, Color.WHITE));
        setDayTextSize(a.getDimension(R.styleable.CalendarView_cd_day_text_size, value2(TypedValue.COMPLEX_UNIT_SP, 12)));
        setDayTextColor(a.getColor(R.styleable.CalendarView_cd_day_text_color, Color.WHITE));

        setUnDayTextSize(a.getDimension(R.styleable.CalendarView_cd_unday_text_size, value2(TypedValue.COMPLEX_UNIT_SP, 12)));
        setUnDayTextColor(a.getColor(R.styleable.CalendarView_cd_unday_text_color, Color.WHITE));

        setDaySelectColor(a.getColor(R.styleable.CalendarView_cd_day_select_color, Color.DKGRAY));
        setDayMaskColor(a.getColor(R.styleable.CalendarView_cd_day_mark_color, Color.WHITE));
        setDayMaskSize(a.getDimension(R.styleable.CalendarView_cd_day_mark_size, value2(TypedValue.COMPLEX_UNIT_DIP, 2)));
        setTitleHeight(a.getDimension(R.styleable.CalendarView_cd_title_height, value2(TypedValue.COMPLEX_UNIT_DIP, 40)));
        setDayPadding(a.getDimension(R.styleable.CalendarView_cd_day_padding, 0));
        setDivideColor(a.getColor(R.styleable.CalendarView_cd_divide_color, Color.WHITE));
        setDivideSize(a.getDimension(R.styleable.CalendarView_cd_divide_size, value2(TypedValue.COMPLEX_UNIT_DIP, 1)));
        setSelectDayPadding(a.getDimension(R.styleable.CalendarView_cd_select_day_padding, value2(TypedValue.COMPLEX_UNIT_DIP, 2)));
        setMode(a.getInt(R.styleable.CalendarView_cd_mode, MONTH_MODE));
        setDaySortMode(a.getInt(R.styleable.CalendarView_cd_day_sort, EAST_MODE));
        setDayMode(a.getInt(R.styleable.CalendarView_cd_day_mode, NONE_MODE));
        setClickMode(a.getInt(R.styleable.CalendarView_cd_click_mode, UN_CLICK_MODE));
        setHeaderShown(a.getBoolean(R.styleable.CalendarView_cd_show_header, true));
        setTimeMillis(System.currentTimeMillis());
        a.recycle();
    }

    /**
     * 设置选中天内边距
     *
     * @param padding
     */
    private void setSelectDayPadding(float padding) {
        this.mSelectDayPadding = padding;
        invalidate();
    }

    private float value2(int typedValue, int value) {
        return TypedValue.applyDimension(typedValue, value, getResources().getDisplayMetrics());
    }


    /**
     * 是否显示标题头
     *
     * @param show
     */
    public void setHeaderShown(boolean show) {
        this.showHeader = show;
        mTitleHeight = !show ? 0 : HEADER_HEIGHT;
        invalidate();
    }

    /**
     * 设置标题字体大小
     *
     * @param textSize
     */
    public void setTitleTextSize(float textSize) {
        this.mTitleTextSize = textSize;
        invalidate();
    }

    /**
     * 设置标题字体颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color) {
        this.mTitleTextColor = color;
        invalidate();
    }

    /**
     * 设置天文字尺寸
     *
     * @param textSize
     */
    public void setDayTextSize(float textSize) {
        this.mDayTextSize = textSize;
        invalidate();
    }

    /**
     * 设置天文字颜色
     *
     * @param color
     */
    public void setDayTextColor(int color) {
        this.mDayTextColor = color;
        invalidate();
    }

    /**
     * 设置非当前月份时字体颜色
     *
     * @param color
     */
    public void setUnDayTextColor(int color) {
        this.mUnDayTextColor = color;
        invalidate();
    }

    /**
     * 设置非当前月份时字体大小
     *
     * @param textSize
     */
    public void setUnDayTextSize(float textSize) {
        this.mUnDayTextSize = textSize;
        invalidate();
    }

    /**
     * 设置天选中颜色
     *
     * @param color
     */
    public void setDaySelectColor(int color) {
        this.mSelectColor = color;
        invalidate();
    }

    /**
     * 设置天标记颜色
     *
     * @param color
     */
    public void setDayMaskColor(int color) {
        this.maskColor = color;
        invalidate();
    }

    /**
     * 设置标记大小
     *
     * @param size
     */
    private void setDayMaskSize(float size) {
        this.maskSize = size;
        invalidate();
    }


    /**
     * 设置标题栏高度
     *
     * @param height
     */
    public void setTitleHeight(float height) {
        this.mTitleHeight = height;
        this.HEADER_HEIGHT = height;
        invalidate();
    }

    /**
     * 设置天内边距
     *
     * @param padding
     */
    public void setDayPadding(float padding) {
        this.mDayPadding = padding;
        invalidate();
    }

    /**
     * 设置天分隔线颜色
     *
     * @param color
     */
    public void setDivideColor(int color) {
        this.mDivideColor = color;
        invalidate();
    }

    /**
     * 设置天分隔线大小
     *
     * @param divideSize
     */
    public void setDivideSize(float divideSize) {
        this.mDivideSize = divideSize;
        invalidate();
    }


    /**
     * 设置日历展示模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        if (this.mCalendarMode == mode) return;
        this.mCalendarMode = mode;
        int viewHeight = getHeight();
        if (0 != viewHeight) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            int days = CalendarUtils.getMonthDay(mCalendarDay.year, mCalendarDay.month);
            mCalendar.set(mCalendarDay.year, mCalendarDay.month - 1, 0);
            int day = mCalendar.get(Calendar.DAY_OF_WEEK);
            //绘制日期天位置
            int height = (int) (getHeight() - mTitleHeight);
            int column = (day + days) / WEEK_COUNT + (0 != ((day + days) % WEEK_COUNT) ? 1 : 0);
            if (WEEK_MODE == mode) {
                int itemHeight = height / column;
                layoutParams.height = (int) (mTitleHeight + itemHeight);
            } else {
                layoutParams.height = (int) (mTitleHeight + column * (viewHeight - mTitleHeight));
            }
            requestLayout();
        }
        invalidate();
    }

    /**
     * 获得当前展示模式
     *
     * @return
     */
    public int getMode() {
        return mCalendarMode;
    }

    /**
     * 设置日历排序模式
     *
     * @param mode
     */
    public void setDaySortMode(int mode) {
        this.mSortMode = mode;
        invalidate();

    }

    /**
     * 设置天显示模式
     *
     * @param mode
     */
    public void setDayMode(int mode) {
        this.mDayMode = mode;
        invalidate();
    }

    /**
     * 设置点击模式
     *
     * @param clickMode
     */
    public void setClickMode(int clickMode) {
        this.mClickMode = clickMode;
        clearMaskDays();
        clearSelectDays();
    }


    /**
     * 设置展示日期
     *
     * @param timeMillis
     */
    public void setTimeMillis(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH));
        setDay(calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        mLastDay.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }


    /**
     * 设置周偏移量
     *
     * @param offset
     */
    public void setWeekOffset(int offset) {
        //记录上次使用时间
        mLastDay.set(mCalendarDay.year, mCalendarDay.month, mCalendarDay.day);
        mCalendar.set(mCalendarDay.year, mCalendarDay.month, mCalendarDay.day);
        mCalendar.add(Calendar.DAY_OF_MONTH, offset * WEEK_COUNT);
        setTimeMillis(mCalendar.getTimeInMillis());
        invalidate();
    }

    /**
     * 设置月份偏移量
     *
     * @param offset
     */
    public void setMonthOffset(int offset) {
        //添加偏移量大过一年时,以年来计算,否则,当偏移过年初,年尾时.
        mCalendar.set(mCalendarDay.year, mCalendarDay.month, mCalendarDay.day);
        mCalendar.add(Calendar.MONTH, offset);
        setTimeMillis(mCalendar.getTimeInMillis());
    }


    /**
     * 设置显示年
     *
     * @param year
     */
    public void setYear(int year) {
        this.mCalendarDay.year = year;
        invalidate();
    }

    /**
     * 设置显示月份
     *
     * @param month
     */
    public void setMonth(int month) {
        this.mCalendarDay.month = month;
        invalidate();
    }


    /**
     * 设置显示天
     *
     * @param day
     */
    public void setDay(int day) {
        this.mCalendarDay.day = day;
        invalidate();
    }

    public void clearSelectDays() {
        this.mSelectDays.clear();
        invalidate();
    }

    public void clearMaskDays() {
        this.maskDays.clear();
        invalidate();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //绘制标题
        if (showHeader) {
            drawTitle(canvas);
        }
        //绘日历天
        if (MONTH_MODE == mCalendarMode) {
            drawMonthDay(canvas);
        } else {
            drawWeekDay(canvas);
        }
        drawClickDays(canvas);
        //绘制标记天数
        drawMaskDays(canvas);
        //绘制选中天数
        drawSelectDays(canvas);
    }

    /**
     * 绘制点击模式
     *
     * @param canvas
     */
    private void drawClickDays(Canvas canvas) {
        if (null == mTouchRect) return;
        resetPaint(0, 0, mSelectColor);
        float padding = mSelectDayPadding;
        RectF rectF = mTouchRect;
        switch (mDayMode) {
            case CIRCLE_MODE:
                float radius = Math.min(rectF.width(), rectF.height()) / 2;
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), (radius - padding) * mPressFraction, mPaint);
                break;
            case RECTANGLE_MODE:
                float width = (rectF.width() - padding) / 2;
                float height = (rectF.height() - padding) / 2;
                canvas.drawRect(rectF.left + width * mPressFraction, rectF.top + +height * mPressFraction, rectF.right - width * mPressFraction, rectF.bottom - height * mPressFraction, mPaint);
                break;
        }
    }

    /**
     * 绘制周标题
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        resetPaint(mTitleTextSize, 0, mTitleTextColor);
        String[] weekTitles;
        if (EAST_MODE == mSortMode) {
            weekTitles = mEastWeekTitle;
        } else {
            weekTitles = mWestWeekTitle;
        }
        if (null != weekTitles) {
            int width = getWidth();
            int length = weekTitles.length;
            int itemWidth = width / WEEK_COUNT;
            float titleHeight = mTitleHeight;
            float textHeight = mTitleTextSize;
            for (int i = 0; i < length; i++) {
                String text = weekTitles[i];
                mPaint.setTextAlign(Paint.Align.LEFT);
                float textWidth = mPaint.measureText(text);
                Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
                canvas.drawText(text, i * itemWidth + (itemWidth - textWidth) / 2, (titleHeight + textHeight - fontMetrics.descent) / 2, mPaint);
                if (isDebug) {
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(new RectF(i * itemWidth + (itemWidth - textWidth) / 2,
                            (titleHeight - textHeight) / 2,
                            i * itemWidth + (itemWidth - textWidth) / 2 + textWidth,
                            (titleHeight - textHeight) / 2 + textHeight), mPaint);
                    canvas.drawRect(new RectF(i * itemWidth, 0, i * itemWidth + itemWidth, titleHeight), mPaint);

                    //横线
                    canvas.drawLine(0, titleHeight / 2, i * itemWidth + itemWidth, titleHeight / 2, mPaint);
                    //
                    canvas.drawLine((i * itemWidth + itemWidth / 2), 0, (i * itemWidth + itemWidth / 2), titleHeight, mPaint);
                }
            }
            darwLine(canvas, 0, titleHeight, width, titleHeight);
        }
    }

    /**
     * 绘制周
     *
     * @param canvas
     */
    private void drawWeekDay(Canvas canvas) {
        mDayRects.clear();
        resetPaint(mDayTextSize, 0, mDayTextColor);
        CalendarDay day1 = mCalendarDay.clone();
        CalendarDay day2 = mLastDay.clone();
        boolean start = day1.hashCode() < day2.hashCode();
        CalendarDay startDay = start ? day1 : day2;
        CalendarDay stopDay = !start ? day1 : day2;

        Loger.i("startDay:" + startDay + " stopDay:" + stopDay);

        int width = getWidth();
        int itemWidth = width / WEEK_COUNT;
        int titleHeight = (int) mTitleHeight;
        int itemHeight = getLayoutParams().height - titleHeight;
        Rect outRect = new Rect();

        //西方展示模式,时间往后推一天
        if (WAST_MODE == mSortMode) {
            startDay.day -= 1;
            if (0 == startDay.day) {
                //退回到上一个月日期
                startDay.month -= 1;
                startDay.day = CalendarUtils.getMonthDay(startDay.year, startDay.month);
            }
        }

        for (int i = startDay.month, j = 0; i <= stopDay.month; i++) {
            int monthDay = CalendarUtils.getMonthDay(startDay.year, i);
            for (int day = (i == startDay.month ? startDay.day : 1); day < (i == stopDay.month ? stopDay.day : monthDay + 1); j++, day++) {
                String text = String.valueOf(day);
                mPaint.getTextBounds(text, 0, text.length(), outRect);
                canvas.drawText(text, j * itemWidth + (itemWidth - outRect.width()) / 2, titleHeight + (itemHeight + outRect.height()) / 2, mPaint);
                mDayRects.put(new RectF(j * itemWidth, titleHeight, j * itemWidth + itemWidth, titleHeight + itemHeight), new CalendarDay(startDay.year, i, day));
            }
        }
    }


    private void darwLine(Canvas canvas, int x, float y, float stopX, float stopY) {
        mPaint.setColor(mDivideColor);
        mPaint.setStrokeWidth(mDivideSize);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawLine(x, y, stopX, stopY, mPaint);
    }


    private void drawMonthDay(Canvas canvas) {
        mDayRects.clear();
        resetPaint(mDayTextSize, 0, mDayTextColor);
        int currentYear = mCalendarDay.year;
        int currentMonth = mCalendarDay.month;
        //上一个月份天数
        int lastMonthDay = CalendarUtils.getMonthDay(currentYear, currentMonth, -1);
        //获得当前月份天
        int days = CalendarUtils.getMonthDay(currentYear, currentMonth);
        mCalendar.set(currentYear, currentMonth - 1, 0);
        int day = mCalendar.get(Calendar.DAY_OF_WEEK) + 1;
        int width = getWidth();
        int itemWidth = width / WEEK_COUNT;
        Rect outRect = new Rect();
        float titleHeight = mTitleHeight;
        //绘制日期天位置
        int height = (int) (getHeight() - mTitleHeight);
        int column = (day + days) / WEEK_COUNT + (0 != ((day + days) % WEEK_COUNT) ? 1 : 0);
        int itemHeight = height / column;
        int month, today;
        for (int i = 0, h = 0; i < column * WEEK_COUNT; i++) {
            //绘分隔线
            if (0 != i && 0 == i % 7) {
                h++;
                //绘横线
                darwLine(canvas, 0, titleHeight + h * itemHeight, width, titleHeight + h * itemHeight);
            } else if (0 != i && 0 == h) {
                //绘竖线
                darwLine(canvas, i * itemWidth, titleHeight, i * itemWidth, getHeight());
            }
            //三个阶段绘制
            if (EAST_MODE == mSortMode) {
                //东方展示模式 周一至周日
                if (i < day) {
                    month = currentMonth - 1;
                    today = lastMonthDay - day + i + 1;
                } else if (i >= day && i < (days + day)) {
                    month = currentMonth;
                    today = i - day + 1;
                } else {
                    month = currentMonth + 1;
                    today = i - days - day + 1;
                }
            } else {
                //西方展示模式 周日开头,周一至周六
                if (i <= day) {
                    month = currentMonth - 1;
                    today = lastMonthDay - day + i;
                } else if (i > day && i <= (days + day)) {
                    month = currentMonth;
                    today = i - day;
                } else {
                    month = currentMonth + 1;
                    today = i - days - day;
                }
            }
            String text = String.valueOf(today);
            mPaint.getTextBounds(text, 0, text.length(), outRect);
            canvas.drawText(text, (i % 7) * itemWidth + (itemWidth - outRect.width()) / 2, titleHeight + h * itemHeight + (itemHeight + outRect.height()) / 2, mPaint);
            mDayRects.put(new RectF((i % 7) * itemWidth, titleHeight + h * itemHeight, (i % 7) * itemWidth + itemWidth, titleHeight + h * itemHeight + itemHeight), new CalendarDay(currentYear, month, today));
        }
    }


    /**
     * 绘制标记
     *
     * @param canvas
     */
    private void drawMaskDays(Canvas canvas) {
        if (null != maskDays) {
            resetPaint(0, maskSize, maskColor);
            int size = maskDays.size();
            for (int i = 0; i < size; i++) {
                RectF rectF = getDrawRectByDay(maskDays.get(i));
                canvas.drawRect(rectF.left, rectF.bottom - maskSize, rectF.right, rectF.bottom, mPaint);
            }
        }
    }


    /**
     * 绘制选中天数
     *
     * @param canvas
     */
    private void drawSelectDays(Canvas canvas) {
        resetPaint(0, 0, mSelectColor);
        int size = mSelectDays.size();
        float padding = mSelectDayPadding;
        for (int i = 0; i < size; i++) {
            RectF rectF = getDrawRectByDay(mSelectDays.get(i));
            switch (mDayMode) {
                case CIRCLE_MODE:
                    float radius = Math.min(rectF.width(), rectF.height()) / 2;
                    canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius - padding, mPaint);
                    break;
                case RECTANGLE_MODE:
                    canvas.drawRect(rectF.left + padding, rectF.top + padding, rectF.right - padding, rectF.bottom - padding, mPaint);
                    break;
            }
        }

    }

    /**
     * 根据日历天,获得绘制矩阵
     *
     * @param day
     * @return
     */
    private RectF getDrawRectByDay(CalendarDay day) {
        RectF rect = null;
        for (Map.Entry<RectF, CalendarDay> entry : mDayRects.entrySet()) {
            if (entry.getValue().equals(day)) {
                rect = entry.getKey();
                break;
            }
        }
        return rect;
    }

    /**
     * 重置paint属性
     *
     * @param textSize
     * @param color
     */
    private void resetPaint(float textSize, float strokeWidth, int color) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                startPressAnim(true);
                mTouchRect = getSelectDayByRect(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
//                startPressAnim(false);
                RectF dayRect = getSelectDayByRect(x, y);
                if (null != dayRect && null != mTouchRect && dayRect.equals(mTouchRect)) {
                    //处在这个方法内,算一个点击,区分五种点击模式
                    CalendarDay calendarDay = mDayRects.get(mTouchRect);
                    switch (mClickMode) {
                        case CLICK_MODE:
                            //单击模式
                            if (null != mClickListener) {
                                mClickListener.onClick(calendarDay);
                                if (isDebug) {
                                    Toast.makeText(getContext(), "点击:" + calendarDay, Toast.LENGTH_SHORT).show();
                                }
                            }
                            break;
                        case MAST_MODE:
                            //标记模式
                            if (maskDays.contains(calendarDay)) {
                                maskDays.remove(calendarDay);
                            } else {
                                maskDays.add(calendarDay);
                            }
                            if (null != mChoiceListener) {
                                mChoiceListener.onChoice(mSelectDays, maskDays);
                            }
                            invalidate();
                            break;
                        case SINGLE_CLICK_MODE:
                            //单选模式
                            mSelectDays.clear();
                            mSelectDays.add(calendarDay);
                            if (null != mChoiceListener) {
                                mChoiceListener.onChoice(mSelectDays, maskDays);
                            }
                            invalidate();
                            break;
                        case MULTIPLE_CHOICE:
                            //多选模式
                            if (mSelectDays.contains(calendarDay)) {
                                mSelectDays.remove(calendarDay);
                            } else {
                                mSelectDays.add(calendarDay);
                            }
                            if (null != mChoiceListener) {
                                mChoiceListener.onChoice(mSelectDays, maskDays);
                            }
                            invalidate();
                            break;
                        case RECTANGLE_CHOICE:
                            //选取模式,选定一个起点,一个结束点
                            if (2 <= mSelectDays.size()) {
                                mSelectDays.clear();
                            } else if (1 == mSelectDays.size()) {
                                //起点
                                CalendarDay startDay = mSelectDays.get(0);
                                mSelectDays.clear();
                                mSelectDays.addAll(CalendarUtils.getCalendarDays(startDay, calendarDay));
                            } else {
                                mSelectDays.add(calendarDay);
                            }
                            invalidate();
                            break;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
//                startPressAnim(false);
                break;
        }
        return true;
    }

    /**
     * 根据按下x,y坐标获得指定范围矩阵
     *
     * @param x
     * @param y
     * @return
     */
    private RectF getSelectDayByRect(float x, float y) {
        RectF selectRect = null;
        for (Map.Entry<RectF, CalendarDay> entry : mDayRects.entrySet()) {
            RectF rect = entry.getKey();
            if (rect.contains(x, y)) {
                selectRect = rect;
                break;
            }
        }
        return selectRect;
    }

    /**
     * 开启绘制动画
     */
    private void startPressAnim(final boolean isReverse) {
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                mPressFraction = isReverse ? 1f - fraction : fraction;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public String getCalendarString() {
        return mCalendarDay.toString();
    }

    /**
     * 置空click事件
     *
     * @param l
     */
    @Override
    public final void setOnClickListener(OnClickListener l) {
    }

    public void setOnCalendarDayClickListener(OnCalendarDayClickListener listener) {
        this.mClickListener = listener;
    }

    public void setOnCalendarDayChoiceListener(OnCalendarDayChoiceListener listener) {
        this.mChoiceListener = listener;
    }

    /**
     * 日历天数点击监听器
     */
    public interface OnCalendarDayClickListener {
        void onClick(CalendarDay day);
    }

    /**
     * 当日历天选中时监听
     */
    public interface OnCalendarDayChoiceListener {
        void onChoice(ArrayList<CalendarDay> days, ArrayList<CalendarDay> maskDays);
    }
}
