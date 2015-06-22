package com.ldfs.demo.ui.anim;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.FixedGridLayout;

import java.util.Random;

/**
 * 自定义简单的LayoutTransition测试
 * 
 * @author momo
 * @Date 2015/2/13
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.view_translation_bete_info)
public class FragmentLayoutTransition extends Fragment {
	@ID(id = R.id.fl_grid_layout)
	private FixedGridLayout mGridLayout;
	private int mCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_layout_transition, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@MethodClick(ids = { R.id.btn_add, R.id.btn_random_add, R.id.btn_remove })
	public void addView(View v) {
		int childCount = mGridLayout.getChildCount();
		int randomIndex = (0 == childCount) ? -1 : new Random().nextInt(childCount);
		switch (v.getId()) {
		case R.id.btn_random_add:
			mGridLayout.addView(getTextView(), randomIndex,true);
			break;
		case R.id.btn_add:
			mGridLayout.addView(getTextView(),0,true);
			break;
		case R.id.btn_remove:
			mGridLayout.removeViewAt(randomIndex);
			break;
		default:
			break;
		}
	}

	public TextView getTextView() {
		TextView textView = new TextView(getActivity());
		int padding = UnitUtils.dip2px(getActivity(), 5);
		textView.setPadding(padding, padding, padding, padding);
		textView.setText(String.valueOf("index:" + mCount++));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextColor(Color.WHITE);
		textView.setBackgroundResource(R.color.green);
		textView.setCompoundDrawablePadding(padding);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGridLayout.removeView(v);
			}
		});
		return textView;
	}
}
