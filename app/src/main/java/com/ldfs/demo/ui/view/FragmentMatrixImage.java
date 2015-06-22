package com.ldfs.demo.ui.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.ldfs.demo.widget.CarView;

/**
 * 以矩阵旋转绘制图片
 * 
 * @author momo
 * @Date 2015/3/3
 * 
 */
@RateInfo(rate= Rate.COMPLETE,beteInfo = R.string.default_bete_info)
public class FragmentMatrixImage extends Fragment {
	@ID(id = R.id.sb_radius)
	private SeekBar mSeekBar;
	@ID(id = R.id.sb_count)
	private SeekBar mSeekCount;
	@ID(id = R.id.tv_degress)
	private TextView mDegress;
	@ID(id = R.id.tv_count)
	private TextView mCount;
	@ID(id = R.id.iv_car_view)
	private CarView mCarView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_matrix_image, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				switch (seekBar.getId()) {
				case R.id.sb_radius:
					mCarView.setDegress(progress);
					mDegress.setText(App.getStr(R.string.degress_value, progress));
					TextFontUtils.setWordColorAndTypedFace(mDegress, Color.GREEN, Typeface.BOLD, progress);
					break;
				case R.id.sb_count:
					mCarView.setCount(progress);
					mCount.setText(App.getStr(R.string.count_value, progress));
					TextFontUtils.setWordColorAndTypedFace(mCount, Color.GREEN, Typeface.BOLD, progress);
					break;
				default:
					break;
				}
			}
		};
		mSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
		mSeekCount.setOnSeekBarChangeListener(seekBarChangeListener);
		mCarView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCarView.startAnim();
			}
		});
	}
}
