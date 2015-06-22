package com.ldfs.demo.ui.view;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.RadioGridLayout;
import com.ldfs.demo.widget.RadioGridLayout.OnCheckListener;
import com.ldfs.demo.widget.XfermodesSampleView;

import java.util.ArrayList;

/**
 * 测试xfermode各种绘制状态
 * 
 * @author momo
 * @Date 2015/3/3
 * 
 */
@RateInfo(rate= Rate.COMPLETE,beteInfo = R.string.default_bete_info)
public class FragmentXfermodes extends Fragment {
	@ID(id = R.id.xv_view)
	XfermodesSampleView mSampleView;
	@ID(id = R.id.rl_radio)
	private RadioGridLayout mRadioLayout;
	@ID(id = R.id.sb_radius)
	private SeekBar mSeekRadio;
	private static final PorterDuffXfermode[] sModes = { new PorterDuffXfermode(PorterDuff.Mode.CLEAR), new PorterDuffXfermode(PorterDuff.Mode.SRC), new PorterDuffXfermode(PorterDuff.Mode.DST),
			new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER), new PorterDuffXfermode(PorterDuff.Mode.DST_OVER), new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
			new PorterDuffXfermode(PorterDuff.Mode.DST_IN), new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT), new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
			new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP), new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP), new PorterDuffXfermode(PorterDuff.Mode.XOR),
			new PorterDuffXfermode(PorterDuff.Mode.DARKEN), new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN), new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
			new PorterDuffXfermode(PorterDuff.Mode.SCREEN) };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_xfermode, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mRadioLayout.addDefaultTextViewByResource(R.array.xfermodes);
		mRadioLayout.setCheckedListener(new OnCheckListener() {

			@Override
			public void multipleChoice(View view,ArrayList<Integer> mChoicePositions) {

			}

			@Override
			public void checked(View view,int newPosition, int oldPosition) {
				mSampleView.setXfermode(sModes[newPosition]);
			}
		});
		mSeekRadio.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int width = mSampleView.getWidth();
				int height = mSampleView.getHeight();
				int radius = Math.min(width, height) / 2 + 100;
				mSampleView.setRadius((int) (radius * (progress * 1f / seekBar.getMax())));
			}
		});
	}
}
