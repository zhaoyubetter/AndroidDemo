package com.ldfs.demo.ui.view;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.RadioGridLayout;
import com.ldfs.demo.widget.RadioGridLayout.OnCheckListener;

/**
 * GridLayout的选择状态组
 * 
 * @author Administrator
 * @Date 2015/2/12
 */
public class FragmentRadioGridLayout extends Fragment implements OnSeekBarChangeListener {
	@ID(id = R.id.rl_grid_layout)
	private RadioGridLayout mRadioLayout;
	@ID(id = R.id.rg_select_mode)
	private RadioGroup mRadioGroup;
	@ID(id = R.id.tv_select_positions)
	private TextView mSelectInfo;
	@ID(id = R.id.tv_change_res)
	private TextView ChangeRes;
	@ID(id = R.id.sb_right_padding)
	private SeekBar mRightPadding;
	@ID(id = R.id.sb_bottom_padding)
	private SeekBar mBottomPadding;
	private int mSelectRes;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_radio_gridlayout, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int length = 20;
		mSelectRes = R.drawable.checked1;
		for (int i = 0; i < length; i++) {
			mRadioLayout.addCheckView(getTextView(i), 0 == i);
		}
		mRadioLayout.setOnCheckClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				App.toast(R.string.click_item, mRadioLayout.indexOfChild(v));
			}
		});
		mRadioLayout.setCheckedListener(new OnCheckListener() {
			@Override
			public void checked(View view,int newPosition, int oldPosition) {
				mSelectInfo.setText(App.getStr(R.string.select_position, newPosition));
				TextFontUtils.setWordColorAndTypedFace(mSelectInfo, Color.RED, Typeface.BOLD, newPosition);
			}

			@Override
			public void multipleChoice(View view,ArrayList<Integer> mChoicePositions) {
				mSelectInfo.setText(App.getStr(R.string.select_position, mRadioLayout.getSelectPositions()));
				TextFontUtils.setWordColorAndTypedFace(mSelectInfo, Color.RED, Typeface.BOLD, mRadioLayout.getSelectPositions());
			}
		});
		mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int position = group.indexOfChild(group.findViewById(checkedId));
				mRadioLayout.setMode(position);
				mSelectInfo.setText(R.string.un_select);
			}
		});
		mRightPadding.setOnSeekBarChangeListener(this);
		mBottomPadding.setOnSeekBarChangeListener(this);
	}

	public TextView getTextView(int index) {
		TextView textView = new TextView(getActivity());
		int padding = UnitUtils.dip2px(getActivity(), 5);
		textView.setPadding(padding, padding, padding, padding);
		textView.setText("text" + index);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextColor(Color.WHITE);
		textView.setBackgroundResource(R.color.green);
		textView.setCompoundDrawablePadding(padding);
		return textView;
	}

	@MethodClick(ids = { R.id.tv_change_res })
	public void onClick(View v) {
		mSelectRes = (mSelectRes == R.drawable.checked1) ? R.drawable.checked2 : R.drawable.checked1;
		mRadioLayout.setCheckRes(mSelectRes);
		ChangeRes.setCompoundDrawablesWithIntrinsicBounds(0, 0, mSelectRes, 0);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		int padding = UnitUtils.dip2px(getActivity(), 20);
		switch (seekBar.getId()) {
		case R.id.sb_right_padding:
			mRadioLayout.setCheckRightPadding(padding * progress * 1f / seekBar.getMax());
			break;
		case R.id.sb_bottom_padding:
			mRadioLayout.setCheckBottomPadding(padding * progress * 1f / seekBar.getMax());
			break;
		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
