package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ldfs.demo.R;
import com.ldfs.demo.util.DrawableBuilder;
import com.ldfs.demo.util.ImageUtils;
import com.ldfs.demo.util.SelectorBuilder;
import com.ldfs.demo.util.UnitUtils;

/**
 * 控件组,可自定义外圈选中样式
 * 
 * @author momo
 * @Date 2014/5/16 2014/11/5 重构代码, 1:将外观改为用颜色值用代码生成,非drawalbe目录下shspe生成.
 *       2:新增滑块移动动画,难点距边缘时自动圆角化
 */
public class OptionGroup extends RadioGroup implements OnCheckedChangeListener {
	private static final int DEFAULT_COLOR = R.color.green;
	private static final float DEFALUT_OPTION_SIZE = 15;
	private static final int DEFAULT_TEXT_PADDING = 5;
	private static final int DEFAULT_BUTTON_ROUND = 3;
	private static final float STROCK_WIDTH = 1;
	private float mDividerWidth;// 获取分隔线宽度,以dip为单位
	private int mDividerColor;// 获取分隔线颜色,默认黑色
	private ColorStateList mColorStateList;
	private int mSelectColor;// 按钮选中颜色
	private int mPressColor;// 按钮按下去颜色
	private int mDefaultColor;// 默认选中颜色
	private float mRoundPadidng;// 图景圆角
	private int mTextPadding;// 文字上下边距
	private int mTextWidthPadding;// 文字左右边距
	private int mSelectPosition;// 选中位置
	private OnCheckListener mListener;// 选中监听
	private String[] mTitles;
	private Paint mPaint;

	public OptionGroup(Context context) {
		this(context, null);
	}

	public OptionGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		setOrientation(HORIZONTAL);
		mPaint = new Paint();
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionGroup);
		setDividerWidth(typedArray.getDimension(R.styleable.OptionGroup_groupDividerWidth, UnitUtils.dip2px(context, STROCK_WIDTH)));
		setDividerColor(getResources().getColor(typedArray.getResourceId(R.styleable.OptionGroup_groupDividerColor, DEFAULT_COLOR)));
		setTextPadding(typedArray.getDimension(R.styleable.OptionGroup_groupTextPadding, UnitUtils.dip2px(context, DEFAULT_TEXT_PADDING)));
		setTextWidthPadding(typedArray.getDimension(R.styleable.OptionGroup_groupTextWidthPadding, UnitUtils.dip2px(context, DEFAULT_TEXT_PADDING)));
		setTextSize(typedArray.getDimension(R.styleable.OptionGroup_groupTexSize, UnitUtils.px2sp(context, DEFALUT_OPTION_SIZE)));
		setTextColorState(getResources().getColorStateList(typedArray.getResourceId(R.styleable.OptionGroup_groupTextSelectRes, R.drawable.green_text_selector)));
		setSelectColor(getResources().getColor(typedArray.getResourceId(R.styleable.OptionGroup_groupSelectRes, DEFAULT_COLOR)));
		setPressColor(getResources().getColor(typedArray.getResourceId(R.styleable.OptionGroup_groupPressRes, DEFAULT_COLOR)));
		setDefaultColor(getResources().getColor(typedArray.getResourceId(R.styleable.OptionGroup_groupDefaultRes, android.R.color.transparent)));
		setRoundPadding(typedArray.getDimension(R.styleable.OptionGroup_groupPadding, UnitUtils.dip2px(context, DEFAULT_BUTTON_ROUND)));
		setData(typedArray.getResourceId(R.styleable.OptionGroup_groupData, -1));
		typedArray.recycle();
	}

	/**
	 * 设置控件组内边距
	 * 
	 * @param padding
	 */
	public void setRoundPadding(float padding) {
		this.mRoundPadidng = padding;
		setBackgroundDrawable(DrawableBuilder.createRoundRectsGradientDrawable(getContext(), mDividerWidth, mDividerColor, Color.TRANSPARENT, Duration.ALL, mRoundPadidng));
		initButtonBackgroundRound();
		invalidate();
	}

	/**
	 * 重置各个按钮角度
	 */
	private void initButtonBackgroundRound() {
		int count = getChildCount();
		View childView = null;
		for (int i = 0; i < count; i++) {
			childView = getChildAt(i);
			if (childView instanceof RadioButton) {
				setButtonBackground((RadioButton) childView, i, count);
			}
		}
	}

	/**
	 * 设置按钮默认显示颜色
	 * 
	 * @param res
	 */
	public void setDefaultColor(int color) {
		mDefaultColor = color;
	}

	/**
	 * 设置选中颜色
	 * 
	 * @param resourceId
	 */
	public void setSelectColor(int color) {
		mSelectColor = color;
	}

	/**
	 * 设置控件按下颜色
	 * 
	 * @param res
	 */
	public void setPressColor(int color) {
		mPressColor = color;
	}

	/**
	 * 设置展示数据
	 * 
	 * @param res
	 */
	public void setData(int res) {
		if (-1 != res) {
			setData(getResources().getStringArray(res));
		}
	}

	public void setData(String[] data) {
		if (null != data) {
			initData(this.mTitles = data);
		}
	}

	/**
	 * 设置显示文字大小
	 * 
	 * @param textSize
	 */
	public void setTextSize(float textSize) {
		for (int i = 0, length = getChildCount(); i < length; i++) {
			View childView = getChildAt(i);
			if (childView instanceof RadioButton) {
				((RadioButton) childView).setTextSize(textSize);
			}
		}
	}

	/**
	 * 设置文字左右边距
	 * 
	 * @param padding
	 */
	public void setTextWidthPadding(float padding) {
		this.mTextWidthPadding = (int) padding;
	}

	public void setTextColorState(ColorStateList stateList) {
		if (null != stateList) {
			this.mColorStateList = stateList;
			for (int i = 0, length = getChildCount(); i < length; i++) {
				View childView = getChildAt(i);
				if (childView instanceof RadioButton) {
					((RadioButton) childView).setTextColor(stateList);
				}
			}
		}
	}

	/**
	 * 设置文字上下边距
	 * 
	 * @param dimension
	 */
	private void setTextPadding(float padding) {
		this.mTextPadding = (int) padding;
	}

	/**
	 * 初始化数据
	 * 
	 * @param context
	 * @param dataRes
	 */
	private void initData(String[] datas) {
		RadioButton button = null;
		removeAllViews();
		for (int i = 0, length = datas.length; i < length; i++) {
			button = new RadioButton(getContext());
			button.setLayoutParams(new RadioGroup.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
			button.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
			button.setGravity(Gravity.CENTER);
			button.setText(mTitles[i]);
			button.setTextColor(mColorStateList);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFALUT_OPTION_SIZE);
			setButtonBackground(button, i, length);
			button.setPadding(mTextWidthPadding, mTextPadding, mTextWidthPadding, mTextPadding);
			button.setId(i);
			addView(button);
		}
		// 默认选中第一个
		check(mSelectPosition);
		setCheck(mSelectPosition);
		setOnCheckedChangeListener(this);
	}

	/**
	 * 根据位置设置字体颜色
	 * 
	 * @param index
	 * @param isSelect
	 */
	private void setTextColorByPosition(int index, boolean isSelect) {
		final View childView = getChildAt(index);
		if (null != childView && childView instanceof RadioButton) {
			childView.setSelected(isSelect);
			// AnimationUtils.startColorAnim(new AnimatorUpdateListener() {
			// @Override
			// public void onAnimationUpdate(ValueAnimator animator) {
			// ((RadioButton)
			// childView).setTextColor(Integer.valueOf(animator.getAnimatedValue().toString()));
			// }
			// }, isSelect ? mTextColor : mTextSelectColor, !isSelect ?
			// mTextColor : mTextSelectColor);

		}
	}

	/**
	 * 设置按钮背景
	 * 
	 * @param button
	 * @param i
	 * @param length
	 */
	private void setButtonBackground(RadioButton button, int i, int length) {
		Context context = getContext();
		Duration duration = Duration.NONE;
		if (0 == i) {
			duration = Duration.LEFT;
		} else if (i == length - 1) {
			duration = Duration.RIGHT;
		}
		GradientDrawable pressDrawable = DrawableBuilder.createFillRoundRectsGradientDrawable(context, mPressColor, duration, mRoundPadidng);
		GradientDrawable mSelectDrawable = DrawableBuilder.createFillRoundRectsGradientDrawable(context, mSelectColor, duration, mRoundPadidng);
		GradientDrawable defaultDrawable = DrawableBuilder.createFillRoundRectsGradientDrawable(context, mDefaultColor, duration, mRoundPadidng);
		button.setBackgroundDrawable(SelectorBuilder.getSelector(pressDrawable, mSelectDrawable, defaultDrawable));
	}

	/**
	 * 设置分隔线宽度
	 * 
	 * @param width
	 */
	public void setDividerWidth(float width) {
		this.mDividerWidth = width;
		invalidate();
	}

	public void setDividerColor(int color) {
		this.mDividerColor = color;
		setBackgroundDrawable(DrawableBuilder.createRoundRectsGradientDrawable(getContext(), mDividerWidth, mDividerColor, Color.TRANSPARENT, Duration.ALL, mRoundPadidng));
		invalidate();
	}

	public void setLine(int color) {
		this.mDividerColor = color;
		for (int i = 0; i < getChildCount(); i++) {
			ImageUtils.setBackGroundScale(getChildAt(i), color);
		}
		ImageUtils.setBackGroundScale(this, color);
		invalidate();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (null != mTitles) {
			int position = checkedId;
			if (position < mTitles.length && null != mListener) {
				setCheck(position);
				mListener.check(position, mTitles[position]);
			}
		}
	}

	public void setCheck(final int position) {
		setTextColorByPosition(mSelectPosition, false);
		setTextColorByPosition(position, true);
		mSelectPosition = position;
	}

	/**
	 * 设置选中监听
	 */
	public void setOnCheckListener(OnCheckListener listener) {
		this.mListener = listener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制分隔线
		drawDivier(canvas);
	}

	/**
	 * 绘制分隔线
	 * 
	 * @param canvas
	 */
	private void drawDivier(Canvas canvas) {
		int length = getChildCount();
		if (0 != length) {
			mPaint.reset();
			mPaint.setColor(mDividerColor);
			mPaint.setStrokeWidth(mDividerWidth);
			mPaint.setStyle(Paint.Style.STROKE);
			RadioButton radioButton = null;
			for (int i = 0; i < length - 1; i++) {
				radioButton = (RadioButton) getChildAt(i);
				canvas.drawLine(radioButton.getRight(), 0, radioButton.getRight(), getHeight(), mPaint);
			}
		}
	}

	/**
	 * OptionGroup 选择监听
	 */
	public interface OnCheckListener {
		void check(int position, String value);
	}

}
