package com.ldfs.demo.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;

/**
 * 自定义标题
 * 
 * @author momo
 * @Date 2014/8/8
 * 
 */
public class TitleBar extends RelativeLayout {
	private static final int UN_RES = -1;
	private static final int TEXT_DEFAULT_SIZE = 18;
	private static final int TEXT_PADDING = 10;// 文字内边距
	private static final int MENU_WIDTH = 50;// 条目宽
	private static final int DEFAULT_TEXT_COLOR = R.color.white;
	private DivideLinearLayout menuContainer;// 功能容器
	private DivideLinearLayout titleContainer;// 标题容器
	private TextView titleView;// 标题控件
	private ImageView backIcon;// 返回控件;
	private TextView pageTitleView;// 中间标题
	private boolean disPlayHome;// 显示返回箭头
	private ProgressBar indeterminate;

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttribute(context, attrs);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleBar(Context context) {
		this(context, null, 0);
	}

	private void initAttribute(Context context, AttributeSet attrs) {
		setId(R.id.titlebar_container);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
		addTitleBarView(titleContainer = new DivideLinearLayout(context), 0, RelativeLayout.LEFT_OF, R.id.titlebar_home);
		addTitleBarView(pageTitleView = getTextMenuView(R.id.titlebar_page, UN_RES), -1, CENTER_HORIZONTAL, -1);
		menuContainer = new DivideLinearLayout(context);
		menuContainer.setDivideColor(App.getResourcesColor(R.color.menu_line));
		menuContainer.setId(R.id.titlebar_home);
		addTitleBarView(menuContainer, -1, ALIGN_PARENT_RIGHT, -1);
		titleView = getTextMenuView(R.id.titlebar_title, UN_RES);
		// 跑马灯效果
		// titleView.setMarqueeRepeatLimit(1);
		// titleView.setHorizontallyScrolling(true);
		// titleView.setFocusableInTouchMode(true);
		// titleView.setPadding(0, 0, 0, 0);
		titleView.setEllipsize(TruncateAt.END);
		backIcon = getImageMenuView(R.id.titlebar_back, R.drawable.abc_ic_ab_back_holo_dark, -1);
		pageTitleView.setBackgroundColor(Color.TRANSPARENT);
		// 添加title控件
		titleContainer.showItemDivide(false);
		// 增加中间分隔线
		titleContainer.setDividePadding(UnitUtils.dip2px(context, 5));
		titleContainer.setDivideColor(App.getResourcesColor(R.color.menu_line));
		titleContainer.setStrokeWidth(UnitUtils.dip2px(context, 0.8f));
		containerAddView(titleContainer, backIcon, Gravity.CENTER);
		containerAddView(titleContainer, titleView, Gravity.CENTER);
		initIndeterminate();
		// 设置背景
		setBackgroundColor(typedArray.getColor(R.styleable.TitleBar_titlebar_bg, getResources().getColor(R.color.title_color)));
		setDisplayHome(typedArray.getBoolean(R.styleable.TitleBar_titlebar_display_home, true));// 显示返回箭头
		setTitle(typedArray.getResourceId(R.styleable.TitleBar_titlebar_title, UN_RES));// 设置标题
		setIcon(typedArray.getResourceId(R.styleable.TitleBar_titlebar_icon, UN_RES));
		setTextTitleColor(typedArray.getColor(R.styleable.TitleBar_titlebar_text_color, getResources().getColor(DEFAULT_TEXT_COLOR)));// 设置文字颜色
		setTextTitleColor(typedArray.getColorStateList(R.styleable.TitleBar_titlebar_text_color));// 设置文字状态选择器
		setPageTitle(typedArray.getResourceId(R.styleable.TitleBar_titlebar_page_title, UN_RES));
		setPageTitleColor(typedArray.getColor(R.styleable.TitleBar_titlebar_page_text_color, getResources().getColor(DEFAULT_TEXT_COLOR)));// 设置标题颜色选择器
		setPageTitleColor(typedArray.getColorStateList(R.styleable.TitleBar_titlebar_page_text_color));// 设置标题颜色
		setPageTitleVisible(typedArray.getBoolean(R.styleable.TitleBar_titlebar_page_title_visible, false));
		showItemDivier(typedArray.getBoolean(R.styleable.TitleBar_titlebar_show_divier, false));// 显示分隔线
		typedArray.recycle();
	}

	/**
	 * 是否显示返回箭头
	 * 
	 * @param boolean1
	 */
	public void setDisplayHome(boolean displayHome) {
		disPlayHome = displayHome;
		titleContainer.showItemDivide(displayHome);
		backIcon.setVisibility(disPlayHome ? View.VISIBLE : View.GONE);
	}

	/**
	 * 设置返回事件
	 * 
	 * @param listener
	 */
	public void setBackListener(OnClickListener listener) {
		if (disPlayHome && null != listener) {
			findViewById(R.id.titlebar_back).setOnClickListener(listener);
		}
	}

	/**
	 * 设置标题文字
	 * 
	 * @param resid
	 *            标题res
	 */
	public void setTitle(int resid) {
		if (UN_RES != resid) {
			titleView.setText(resid);
		}
	}

	/**
	 * 设置标题文字
	 * 
	 */
	public void setTitle(String title) {
		if (!TextUtils.isEmpty(title)) {
			titleView.setText(title);
		}
	}

	/**
	 * 设置头标
	 * 
	 * @param resid
	 */
	public void setIcon(int resid) {
		if (UN_RES != resid) {
			titleView.setPadding(0, 0, TextUtils.isEmpty(titleView.getText()) ? 0 : UnitUtils.dip2px(getContext(), TEXT_PADDING), 0);
			titleView.setCompoundDrawablesWithIntrinsicBounds(resid, 0, 0, 0);
		}
	}

	/**
	 * 设置title文字颜色
	 * 
	 * @param color
	 */
	public void setTextTitleColor(int color) {
		View homeView = findViewById(R.id.titlebar_title);
		if (null != homeView && homeView == titleView) {
			titleView.setTextColor(color);
		}
	}

	/**
	 * 设置title文字颜色选择器
	 * 
	 * @param color
	 */
	public void setTextTitleColor(ColorStateList stateList) {
		View homeView = findViewById(R.id.titlebar_title);
		if (null != homeView && homeView == titleView && null != stateList) {
			titleView.setTextColor(stateList);
		}
	}

	/**
	 * 设置页标题
	 * 
	 * @param resid
	 */
	public void setPageTitle(int resid) {
		if (null != pageTitleView && UN_RES != resid) {
			pageTitleView.setText(resid);
		}
	}

	/**
	 * 设置页标题
	 * 
	 * @param resid
	 */
	public void setPageTitle(String pageTitle) {
		if (null != pageTitleView) {
			pageTitleView.setText(pageTitle);
		}
	}

	/**
	 * 设置页标题颜色
	 * 
	 * @param stateList
	 */
	public void setPageTitleColor(int color) {
		if (null != pageTitleView) {
			pageTitleView.setTextColor(color);
		}
	}

	/**
	 * 设置页标题颜色选择器
	 * 
	 * @param stateList
	 */
	public void setPageTitleColor(ColorStateList colors) {
		if (null != pageTitleView && null != colors) {
			pageTitleView.setTextColor(colors);
		}
	}

	/**
	 * 设置页显示隐藏状态
	 */
	public void setPageTitleVisible(boolean visibility) {
		if (null != pageTitleView) {
			pageTitleView.setVisibility(visibility ? View.VISIBLE : View.GONE);
		}
	}

	/**
	 * 显示条目之间分隔线
	 * 
	 * @param showDivier
	 */
	public void showItemDivier(boolean showDivier) {
		menuContainer.showItemDivide(showDivier);
	}

	/**
	 * 是否显示加载旋转框
	 * 
	 * @param showIndeterminate
	 */
	public void showIndeterminate(boolean showIndeterminate) {
		if (null != indeterminate) {
			indeterminate.setVisibility(showIndeterminate ? View.VISIBLE : View.GONE);
		}
	}

	private void initIndeterminate() {
		Context context = getContext();
		indeterminate = new ProgressBar(context, null, android.R.attr.progressBarStyle);
		int padding = UnitUtils.dip2px(context, 10);
		indeterminate.setPadding(padding, padding, padding, padding);
		indeterminate.setVisibility(View.GONE);
		addContentView(indeterminate);
	}

	/**
	 * 添加左控件
	 * 
	 * @param view
	 */
	private void addTitleBarView(View view, int width, int verb, int anchor) {
		RelativeLayout.LayoutParams params = new LayoutParams(-1 == width ? LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		if (-1 == anchor) {
			params.addRule(verb);
		} else {
			params.addRule(verb, anchor);
		}
		addView(view, params);
	}

	/**
	 * 添加左控件
	 * 
	 * @param view
	 */
	private void containerAddView(DivideLinearLayout layout, View childView, int gravity) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		params.gravity = gravity;
		layout.addView(childView, params);
	}

	/**
	 * 添加文字菜单项
	 * 
	 * @param id
	 * @param resId
	 */
	public void addTextMenu(int id, int resId) {
		addTextMenu(id, resId, null);
	}

	public TextView addTextMenu(int id, int resId, OnClickListener listener) {
		TextView textMenuView = getTextMenuView(id, resId);
		if (null != listener) {
			textMenuView.setOnClickListener(listener);
		}
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		menuContainer.addView(textMenuView, params);
		return textMenuView;
	}

	/**
	 * 添加图片菜单项
	 * 
	 * @param id
	 * @param resId
	 */
	public void addImageMenu(int id, int resId, int textid) {
		addImageMenu(id, resId, textid, null);
	}

	public ImageView addImageMenu(int id, int resId, int textid, OnClickListener listener) {
		ImageView imageMenuView = getImageMenuView(id, resId, textid);
		if (null != listener) {
			imageMenuView.setOnClickListener(listener);
		}
		LayoutParams params = new LayoutParams(UnitUtils.dip2px(getContext(), MENU_WIDTH), LayoutParams.MATCH_PARENT);
		menuContainer.addView(imageMenuView, params);
		return imageMenuView;
	}

	/**
	 * 添加其他控件体
	 * 
	 * @param view
	 */
	public void addContentView(View view) {
		this.addContentView(view, -1, -1);
	}

	/**
	 * 添加其他控件体
	 * 
	 * @param view
	 */
	public void addContentView(View view, int width, int height) {
		if (null != view) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UnitUtils.dip2px(getContext(), -1 == width ? MENU_WIDTH : width), UnitUtils.dip2px(getContext(), -1 == height ? MENU_WIDTH
					: height));
			params.gravity = Gravity.CENTER_VERTICAL;
			params.rightMargin = UnitUtils.dip2px(getContext(), 5);
			menuContainer.addView(view, params);
		}
	}

	/**
	 * 添加一个图片菜单
	 * 
	 * @param id
	 * @param resId
	 * @return
	 */
	private ImageView getImageMenuView(int id, int resId, final int textid) {
		ImageView imageMenu = new ImageView(getContext());
		imageMenu.setId(id);
		imageMenu.setImageResource(resId);
		imageMenu.setBackgroundResource(R.drawable.titlebar_textselector);
		imageMenu.setScaleType(ScaleType.FIT_CENTER);
		int padding = UnitUtils.dip2px(getContext(), 12);
		imageMenu.setPadding(padding, padding, padding, padding);
		if (-1 != textid) {
			imageMenu.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// ToastUtils.showAtLocatl(menuContainer, textid);
					return true;
				}
			});
		}
		return imageMenu;
	}

	/**
	 * 添加文字菜单
	 * 
	 * @param id
	 * @param resid
	 * @return
	 */
	private TextView getTextMenuView(int id, final int resid) {
		int padding = UnitUtils.dip2px(getContext(), TEXT_PADDING);
		return getTextMenuView(id, resid, padding, padding);
	}

	/**
	 * 
	 * 添加文字菜单
	 * 
	 * @param id
	 * @param resid
	 * @param paddingLeft
	 *            控件左边距
	 * @param paddingRight
	 *            控件右边距
	 * @return
	 */
	private TextView getTextMenuView(int id, final int resid, int paddingLeft, int paddingRight) {
		TextView textMenu = new TextView(getContext());
		textMenu.setId(id);
		textMenu.setGravity(Gravity.CENTER);
		textMenu.setPadding(paddingLeft, 0, paddingRight, 0);
		textMenu.setGravity(Gravity.CENTER_VERTICAL);
		textMenu.setSingleLine();
		textMenu.setBackgroundResource(R.drawable.titlebar_textselector);
		textMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_DEFAULT_SIZE);
		textMenu.setTextColor(getResources().getColor(DEFAULT_TEXT_COLOR));
		if (R.id.titlebar_page != id) {
			textMenu.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// ToastUtils.showAtLocatl(menuContainer, resid);
					return true;
				}
			});
		}
		if (UN_RES != resid) {
			textMenu.setText(resid);
		}
		return textMenu;
	}

	/**
	 * 获得标题内容
	 * 
	 * @return
	 */
	public String getTitle() {
		return titleView.getText().toString();
	}

	/**
	 * 设置标题显示隐藏状态
	 * 
	 * @param i
	 */
	public void setTitleVisible(int visibility) {
		if (null != titleContainer) {
			titleContainer.setVisibility(visibility);
		}
	}

	public void removeLastMenu() {
		if (null != menuContainer) {
			int count = menuContainer.getChildCount();
			View childView = null;
			for (int i = count - 1; i >= 0; i--) {
				childView = menuContainer.getChildAt(i);
				if (childView != indeterminate) {
					menuContainer.removeView(childView);
					break;
				}
			}
		}
	}

	/**
	 * 清空菜单
	 */
	public void clearMenu() {
		if (null != menuContainer) {
			menuContainer.removeAllViews();
			menuContainer.addView(indeterminate);
		}
	}
}
