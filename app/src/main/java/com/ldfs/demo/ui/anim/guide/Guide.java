package com.ldfs.demo.ui.anim.guide;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.ldfs.demo.App;
import com.nineoldandroids.view.ViewHelper;

/**
 * 引导页控制基�?
 * 
 * @author momo
 * @Date 2014/10/30
 */
public abstract class Guide {
	protected static final int ANIMATION_TIME = 500;
	protected final int mPosition;
	protected final View mView;
	protected boolean isInit;
	protected Runnable completeAction;

	public Guide(Context context, int mPosition) {
		super();
		this.mPosition = mPosition;
		this.mView = View.inflate(context, getLayout(), null);
		this.mView.setVisibility(View.INVISIBLE);
	}

	/**
	 * 获得当前控件�?
	 * 
	 * @return
	 */
	public View getView() {
		return mView;
	}

	/** 获得当前布局对象 */
	abstract int getLayout();

	/** 选中当前引导页操�?*/
	public void onPageSelected(final int position) {
		if (!isInit) {
			final ViewTreeObserver viewTreeObserver = mView.getViewTreeObserver();
			viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (!isInit) {
						mView.setVisibility(View.VISIBLE);
						pageSelect(position);
						isInit = true;
					}
				}
			});
		}
	}

	/** 滑动引导页操�?*/
	public void onPageScrolled(int position, float positionOffset, int offsetValue, boolean isSelectItem) {
		if (position == mPosition && isInit) {
			pageScrolled(position, positionOffset, offsetValue, isSelectItem);
		}
	};

	/**
	 * 设置控件显示隐藏状�?
	 */
	protected void setViewStatus(boolean show, View... views) {
		if (null != views && 0 < views.length) {
			for (int i = 0; i < views.length; i++) {
				views[i].setVisibility(show ? View.VISIBLE : View.INVISIBLE);
			}
		}
	}

	/**
	 * 批量设置控件alpha�?
	 * 
	 * @param alpha
	 * @param views
	 */
	protected void setAlphas(float alpha, View... views) {
		if (null != views && 0 < views.length) {
			for (int i = 0; i < views.length; i++) {
				ViewHelper.setAlpha(views[i], alpha);
			}
		}
	}

	/**
	 * 批量设置控件alpha�?
	 * 
	 * @param alpha
	 * @param views
	 */
	protected void setRotations(float rotation, View... views) {
		if (null != views && 0 < views.length) {
			for (int i = 0; i < views.length; i++) {
				ViewHelper.setRotation(views[i], rotation);
			}
		}
	}

	/**
	 * 批量设置控件scale�?
	 * 
	 * @param alpha
	 * @param views
	 */
	protected void setScales(float scale, View... views) {
		if (null != views && 0 < views.length) {
			for (int i = 0; i < views.length; i++) {
				ViewHelper.setScaleX(views[i], scale);
				ViewHelper.setScaleY(views[i], scale);
			}
		}
	}

	protected void setLayoutViewSize(float weight, boolean isWidth, View... views) {
		if (null != views) {
			for (int i = 0; i < views.length; i++) {
				int width = views[i].getWidth();
				int height = views[i].getHeight();
				ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) views[i].getLayoutParams();
				float scale = (isWidth ? App.sWidth * weight : App.sHeight * weight) / (isWidth ? width : height);
				params.width = (int) (width * scale);
				params.height = (int) (height * scale);
				views[i].setLayoutParams(params);
			}
		}
	}

	/**
	 * 设置控件在屏幕显示比例位�?
	 * 
	 * @param view
	 * @param widthWeight
	 * @param heightWeight
	 */
	protected void setViewLocalion(View view, float widthWeight, float heightWeight) {
		RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
		params.leftMargin = (int) (widthWeight * App.sWidth);
		params.topMargin = (int) (heightWeight * App.sHeight);
		view.setLayoutParams(params);
	}

	/**
	 * 设置完成事件
	 * 
	 * @param runnable
	 */
	public void setCompleteAction(Runnable runnable) {
		this.completeAction = runnable;
	}

	/** 页�?中状�?*/
	public abstract void pageSelect(int position);

	/** 页滑动状�?*/
	public abstract void pageScrolled(int position, float positionOffset, int offsetValue, boolean isSelectItem);

}
