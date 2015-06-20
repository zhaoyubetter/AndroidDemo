package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.ldfs.demo.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * 带点击波纹扩散效果的imageView;
 *
 * @author momo
 * @Date 2015/3/2 想要的效果: 1:点击下去.按住情况下,会有持续的扩散波纹 2:点击弹开,大范围的扩散,之后触发点击事件
 */
public class ImageViewFlat extends ImageView {
    // 散开波纹类型
    private static final int RECTANGLE_BG = 0;
    private static final int CIRCLE_BG = 1;
    private static final int RECTANGLE = 2;
    private static final int CIRCLE = 3;
    //点击启动模式
    private static final int CLICK = 0;
    private static final int DELAY_CLICK = 1;

    private OnClickListener mListener;
    private int mPressCircleWidth;
    private int mFlatPadding;
    private int mFlatColor;
    private int mFlatBackgroud;
    private long mFlatDuration;
    private int mFlatType;
    private int mClickMode;//点击模式,正常点击,延持等动画完毕后启动
    private int x, y;
    private Paint mPaint, mCirclePaint;
    private boolean isComplate;// 动画执行完毕
    private boolean isDismiss;// 消失状态
    private PorterDuffXfermode mXfermode;

    public ImageViewFlat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
        mCirclePaint = new Paint();
        mPaint.setAntiAlias(true);
        mCirclePaint.setAntiAlias(true);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        // 空实现长按事件,如果用户自定义了长按事件,会直接替换
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewFlat);
        setFlatColor(a.getColor(R.styleable.ImageViewFlat_flat_color, Color.GREEN));
        setFlatBackgroud(a.getColor(R.styleable.ImageViewFlat_flat_backgroud, Color.WHITE));
        setFlatPadding((int) a.getDimension(R.styleable.ImageViewFlat_flat_padding, 0));
        setFlagDuration(a.getInteger(R.styleable.ImageViewFlat_flat_duration, 600));
        setFlatType(a.getInt(R.styleable.ImageViewFlat_flat_type, CIRCLE_BG));
        setClickMode(a.getInt(R.styleable.ImageViewFlat_flat_click, CLICK));
        a.recycle();
    }

    public ImageViewFlat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewFlat(Context context) {
        this(context, null, 0);
    }

    public void setFlatColor(int color) {
        this.mFlatColor = getAlphaColor(0x88, color);
        invalidate();
    }

    private int getAlphaColor(int alpha, int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public void setFlatPadding(int padding) {
        this.mFlatPadding = padding;
        invalidate();
    }

    public void setFlatBackgroud(int color) {
        this.mFlatBackgroud = color;
        setBackgroundDrawable(getStateDrawable(0xFF, getBackgroudResByType()));
    }

    public void setFlagDuration(int duration) {
        this.mFlatDuration = duration;
    }

    @SuppressWarnings("deprecation")
    public void setFlatType(int type) {
        this.mFlatType = type;
        // 重新设置背景状态
        setFlatBackgroud(mFlatBackgroud);
    }

    private int getBackgroudResByType() {
        int resid;
        switch (mFlatType) {
            case CIRCLE_BG:
                resid = R.drawable.background_button_float;
                break;
            case RECTANGLE_BG:
                resid = R.drawable.background_button_rectangle;
                break;
            case CIRCLE:
                resid = R.drawable.button_float_selector;
                break;
            case RECTANGLE:
            default:
                resid = R.drawable.button_rectangle_selector;
                break;
        }
        return resid;
    }

    private Drawable getStateDrawable(int alpha, int resid) {
        Drawable drawable = getResources().getDrawable(resid);
        if (drawable instanceof StateListDrawable) {
            StateListDrawable stateListDrawable = (StateListDrawable) drawable;
            stateListDrawable.selectDrawable(0);
            Drawable current = stateListDrawable.getCurrent();
            if (current instanceof LayerDrawable) {
                LayerDrawable layer = (LayerDrawable) current;
                GradientDrawable shape = (GradientDrawable) layer.findDrawableByLayerId(R.id.btn_flat_press);
                shape.setColor(getAlphaColor(0xDD, mFlatBackgroud));
            }
            stateListDrawable.selectDrawable(1);
            current = stateListDrawable.getCurrent();
            if (current instanceof LayerDrawable) {
                LayerDrawable layer = (LayerDrawable) current;
                GradientDrawable shape = (GradientDrawable) layer.findDrawableByLayerId(R.id.btn_flat_shape);
                shape.setColor(getAlphaColor(alpha, mFlatBackgroud));
            }
        }
        return drawable;
    }

    private int getDarkColor(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb(r + 30, g + 30, b + 30);
    }

    /**
     * 设置点击模式
     *
     * @param mode
     */
    private void setClickMode(int mode) {
        this.mClickMode = mode;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mListener = l;
    }

    @Override
    public void setOnLongClickListener(final OnLongClickListener l) {
        super.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (null != l) {
                    l.onLongClick(v);
                }
                startFlatAnim(false);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                // 执行扩散动画,并执行点击事件
                Rect outRect = new Rect();
                getDrawingRect(outRect);
                if (outRect.contains(x, y)) {
                    startFlatAnim(true);
                }
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                break;
            default:
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    /**
     * 执行点击事件
     *
     * @param animStart 动画开始
     */
    private void setClick(boolean animStart) {
        if (null == mListener) return;
        switch (mClickMode) {
            case CLICK:
                if (animStart) {
                    mListener.onClick(this);
                }
                break;
            case DELAY_CLICK:
                if (!animStart) {
                    mListener.onClick(this);
                }
                break;
        }
    }

    /**
     * 执行扩散动画
     *
     * @param isClick
     */
    private void startFlatAnim(final boolean isClick) {
        isComplate = false;
        // 执行动画距离,应该是长与宽的交叉线
        int size = (int) Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(size);
        // 如果是长按,时间速度加快
        valueAnimator.setDuration(mFlatDuration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mFlatColor = getAlphaColor(isClick ? 0xBB : 0x88, mFlatColor);
                mPressCircleWidth = Integer.valueOf(animator.getAnimatedValue().toString());
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isClick) {
                    setClick(true);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isComplate = true;
                isDismiss = isClick;
                if (isClick) {
                    setClick(false);
                }
            }
        });
        // 消失动画
        final ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(1f);
        alphaAnimator.setDuration(600);
        alphaAnimator.setInterpolator(new AccelerateInterpolator());
        alphaAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mPaint.setColor(getAlphaColor((int) (0x66 * (1f - animator.getAnimatedFraction())), mFlatColor));
                invalidate();
            }
        });
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isDismiss = false;
            }
        });
        // 点击则执行动画组动画,否则执行扩散动画
        if (isClick) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(valueAnimator).before(alphaAnimator);
            animatorSet.start();
        } else {
            valueAnimator.start();
        }
    }

    /**
     * 创建限制绘制区域位图
     */
    public Bitmap createCropCircle() {
        int width = getWidth() - mFlatPadding * 2;
        int height = getHeight() - mFlatPadding * 2;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        final Rect rect = new Rect(0, 0, width, height);
        mPaint.reset();
        mPaint.setColor(mFlatColor);
        drawFlat(canvas);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(makeCircle(), rect, rect, mPaint);
        mPaint.setXfermode(null);
        return bitmap;
    }

    public Bitmap makeCircle() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mCirclePaint.setColor(mFlatColor);
        canvas.drawCircle(x, y, mPressCircleWidth, mCirclePaint);
        return bitmap;
    }

    /**
     * 波纹展开后消失动画
     *
     * @param canvas
     */
    private void drawFlat(Canvas canvas) {
        int width = getWidth() - mFlatPadding * 2;
        int height = getHeight() - mFlatPadding * 2;
        switch (this.mFlatType) {
            case RECTANGLE:
            case RECTANGLE_BG:
                canvas.drawRect(mFlatPadding, mFlatPadding, width, height, mPaint);
                break;
            case CIRCLE:
            case CIRCLE_BG:
                int radius = Math.min(width, height);
                canvas.drawCircle(width / 2, height / 2, radius / 2, mPaint);
            default:
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isComplate) {
            canvas.drawBitmap(createCropCircle(), mFlatPadding, mFlatPadding, null);
        } else if (isDismiss) {
            drawFlat(canvas);
        }
    }

}
