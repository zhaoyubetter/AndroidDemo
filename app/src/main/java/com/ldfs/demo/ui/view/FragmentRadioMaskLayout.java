package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.MaskRadioLayout;

/**
 * 点选遮罩的gridlayout
 * 
 * @author momo
 * @Date 2015/2/12
 * 
 */
public class FragmentRadioMaskLayout extends Fragment {
	@ID(id = R.id.ml_radio_layout)
	private MaskRadioLayout mMaskLayout;
	@ID(id = R.id.sb_padding)
	private SeekBar mSeekBar;
	@ID(id = R.id.rg_mask_mode)
	private RadioGroup mRadioGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_radio_masklayout, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int padding = UnitUtils.dip2px(getActivity(), 10);
				mMaskLayout.setMaskPadding(padding * progress * 1f / seekBar.getMax());
			}
		});
		mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mMaskLayout.setMaskMode(group.indexOfChild(group.findViewById(checkedId)));
			}
		});
		int length = 40;
		int[] resid = new int[length];
		for (int i = 0; i < length; i++) {
			resid[i] = R.drawable.ic_launcher;
		}
		mMaskLayout.addImageViews(resid);
	}
}
