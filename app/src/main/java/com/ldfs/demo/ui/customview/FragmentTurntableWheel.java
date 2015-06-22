package com.ldfs.demo.ui.customview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.TurntableWheel;

/**
 * 抽奖转盘界面
 * 
 * @author momo
 * @Date 2015/3/4
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.turntable_wheel_bate_info)
public class FragmentTurntableWheel extends Fragment {
	@ID(id = R.id.tw_turntable_wheel)
	private TurntableWheel mWheel;
	@ID(id = R.id.tv_count)
	private TextView mCount;
	@ID(id = R.id.sb_count)
	private SeekBar mSeekCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_turntable_wheel, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWheel.setFranshapedColors(Color.GREEN, Color.BLUE);
		mWheel.setDrawableRes(new int[] { R.drawable.qq, R.drawable.review_bhx });
		mSeekCount.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int count = 2 + (int) (10 * progress * 1f / seekBar.getMax());
				mWheel.setRaw(count);
				mCount.setText(App.getStr(R.string.degress_value, count));
				TextFontUtils.setWordColorAndTypedFace(mCount, Color.GREEN, Typeface.BOLD, count);
			}
		});
	}

	/**
	 * 开始转盘动画
	 * 
	 * @param v
	 */
	@MethodClick(ids = R.id.tv_start_anim)
	public void startWheelAnim(View v) {
		mWheel.startThrntableAnim();
	}
}
