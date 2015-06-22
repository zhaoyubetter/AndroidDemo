package com.ldfs.demo.ui.customview;

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
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.WaveView;

/**
 * 音乐播放波纹效果
 * 
 * @author momo
 * @Date 2015/3/1
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.wave_bete_info)
public class FragmentMusicWave extends Fragment implements OnSeekBarChangeListener {
	@ID(id = R.id.wave_view)
	private WaveView mWaveView;
	@ID(id = R.id.sb_wave_count)
	private SeekBar mWaveCount;
	@ID(id = R.id.sb_wave_height)
	private SeekBar mWaveHeight;
	@ID(id = R.id.sb_wave_speed)
	private SeekBar mWaveSpeed;
	@ID(id = R.id.rg_wave_type)
	private RadioGroup mWaveType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music_wave, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWaveCount.setOnSeekBarChangeListener(this);
		mWaveHeight.setOnSeekBarChangeListener(this);
		mWaveSpeed.setOnSeekBarChangeListener(this);
		mWaveType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mWaveView.setWaveType(group.indexOfChild(group.findViewById(checkedId)));
			}
		});
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.sb_wave_count:
			mWaveView.setWaveCount(progress);
			break;
		case R.id.sb_wave_height:
			mWaveView.setWaveHeight(UnitUtils.dip2px(getActivity(), progress));
			break;
		case R.id.sb_wave_speed:
			mWaveView.setWaveSpeed(0.1f * (progress * 1f / seekBar.getMax()));
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
