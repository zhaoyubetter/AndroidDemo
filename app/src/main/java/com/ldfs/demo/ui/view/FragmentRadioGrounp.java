package com.ldfs.demo.ui.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.anim.AnimationUtils;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.OptionGroup;
import com.ldfs.demo.widget.OptionGroup.OnCheckListener;

/**
 * RadioGrounp控件组
 * 
 * @author momo
 * @Date 2015/2/8
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.radio_group_bate_info)
public class FragmentRadioGrounp extends Fragment implements OnSeekBarChangeListener {
	@ID(id = R.id.og_grounp)
	private OptionGroup mOptionGroup;
	@ID(id = R.id.sb_color)
	private SeekBar mColorSeekBar;
	@ID(id = R.id.sb_radio)
	private SeekBar mDegreesSeekBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_radiogrounp, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mColorSeekBar.setOnSeekBarChangeListener(this);
		mDegreesSeekBar.setOnSeekBarChangeListener(this);
		mOptionGroup.setOnCheckListener(new OnCheckListener() {
			@Override
			public void check(int position, String value) {
			}
		});
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		float fraction = progress * 1f / seekBar.getMax();
		int color = AnimationUtils.evaluate(fraction, App.getResourcesColor(R.color.green), Color.RED);
		if (R.id.sb_color == seekBar.getId()) {
			mOptionGroup.setDividerColor(color);
		} else if (R.id.sb_radio == seekBar.getId()) {
			mOptionGroup.setRoundPadding(progress / 2);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
