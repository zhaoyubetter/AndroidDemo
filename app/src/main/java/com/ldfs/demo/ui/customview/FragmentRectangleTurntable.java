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
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.TurntableView;
import com.ldfs.demo.widget.TurntableView.PlayCallback;

/**
 * 抽奖转盘界面
 * 
 * @author momo
 * @Date 2015/3/4
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.thrntable_bete_info)
public class FragmentRectangleTurntable extends Fragment {
	@ID(id = R.id.tw_rectangle_turntable)
	private TurntableView mTurntable;
	@ID(id = R.id.tv_count)
	private TextView mCount;
	@ID(id = R.id.sb_count)
	private SeekBar mSeekCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rectangle_turntable, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setItemCount(8);
		mTurntable.startPlay(new PlayCallback() {
			@Override
			public void playComplate(int position, int res) {
				App.toast(String.valueOf(position));
			}
		});
		mSeekCount.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int count = 8 + 4 * progress;
				setItemCount(count);
				mCount.setText(App.getStr(R.string.degress_value, count));
				TextFontUtils.setWordColorAndTypedFace(mCount, Color.GREEN, Typeface.BOLD, count);
			}
		});
	}

	private void setItemCount(int count) {
		int[] newRes = new int[count];
		for (int i = 0; i < count; i++) {
			newRes[i] = R.drawable.qq_item;
		}
		mTurntable.setTurntableDrawables(newRes);
	}

}
