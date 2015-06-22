package com.ldfs.demo.ui.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.listener.SimpleAnimatorListener;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.DragGridLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 控件拖动排序
 * 
 * @author momo
 * @Date 2015/2/11
 */
@RateInfo(rate= Rate.CODING,beteInfo = R.string.draw_bete_info)
public class FragmentDragSort extends Fragment {
	@ID(id = R.id.dl_new_item)
	private DragGridLayout mNewLayout;
	@ID(id = R.id.dl_add_item)
	private DragGridLayout mAddLayout;
	@ID(id = R.id.tv_move_view)
	private View moveView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dragsort, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int length = 40;
		for (int i = 0; i < length; i++) {
			addNewItem(String.valueOf(i), false);
		}
		for (int i = 0; i < length; i++) {
			addItem(String.valueOf(i), false);
		}
		ViewTreeObserver treeObserver = moveView.getViewTreeObserver();
		treeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				moveView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				com.nineoldandroids.view.ViewHelper.setTranslationX(moveView, -moveView.getWidth());
			}
		});
	}

	private View addNewItem(String value, boolean showAnim) {
		TextView newButton = new TextView(getActivity());
		newButton.setText(value);
		newButton.setBackgroundColor(Color.WHITE);
		newButton.setGravity(Gravity.CENTER);
		newButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				moveViewtoLayout(v, mNewLayout, false);
			}
		});
		mNewLayout.addView(newButton, -1, showAnim);
		return newButton;
	}

	private TextView addItem(String value, boolean showAnim) {
		TextView newButton = new TextView(getActivity());
		newButton.setText(value);
		newButton.setBackgroundColor(Color.WHITE);
		newButton.setGravity(Gravity.CENTER);
		newButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// 移动到点击控件处
				moveViewtoLayout(v, mAddLayout, true);
			}
		});
		mAddLayout.addView(newButton, 0, showAnim);
		return newButton;
	}

	private int[] movetoView(final View v) {
		int[] location = new int[2];
		int[] moveLocation = new int[2];
		v.getLocationInWindow(location);
		moveView.getLocationInWindow(moveLocation);
		final int y = location[1];
		final int top = moveLocation[1];
		// 移动距离
		final int translationY = y - top;
		// 设置到删除按钮处
		com.nineoldandroids.view.ViewHelper.setTranslationX(moveView, v.getLeft());
		com.nineoldandroids.view.ViewHelper.setTranslationY(moveView, translationY);
		return moveLocation;
	}

	private void moveViewtoLayout(final View v, final ViewGroup layout, boolean isEdit) {
		final int[] location = movetoView(v);
		View addItem = null;
		if (isEdit) {
			addItem = addNewItem("添加view", false);
		} else {
			addItem = addItem("删除view", true);
		}
		final View finaItem = addItem;
		addItem.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				finaItem.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int[] newViewLocation = new int[2];
				finaItem.getLocationInWindow(newViewLocation);
				ViewPropertyAnimator.animate(moveView).translationX(newViewLocation[0]).translationY(newViewLocation[1] - location[1]).setDuration(500).setStartDelay(200)
						.setListener(new SimpleAnimatorListener() {
							@Override
							public void onAnimationEnd(Animator animator) {
								super.onAnimationEnd(animator);
								com.nineoldandroids.view.ViewHelper.setTranslationX(moveView, -moveView.getWidth());
								com.nineoldandroids.view.ViewHelper.setTranslationY(moveView, 0);
								layout.removeView(v);
							}
						});
			}
		});
	}

}
