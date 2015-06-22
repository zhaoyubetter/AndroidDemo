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
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.RadioGridLayout;
import com.ldfs.demo.widget.RadioGridLayout.OnCheckListener;
import com.ldfs.demo.widget.WheelView;

import java.util.ArrayList;

/**
 * 自定义Wheel旋转
 * 
 * @author momo
 * @Date 2015/3/4
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.wheel_bate_info)
public class FragmentWheelView extends Fragment implements OnSeekBarChangeListener, OnCheckListener {
	@ID(id = R.id.wv_wheel)
	private WheelView mWheelView;
	@ID(id = R.id.rl_radio)
	private RadioGridLayout mRadioLayout;
	@ID(id = R.id.rl_inner_width)
	private RadioGridLayout mContourRadioLayout;
	@ID(id = R.id.rl_contour)
	private RadioGridLayout mContourLayout;
	@ID(id = R.id.rl_text)
	private RadioGridLayout mTextLayout;

	@ID(id = R.id.tv_degress)
	private TextView mDegress;
	@ID(id = R.id.sb_degress)
	private SeekBar mDegressSeek;

	@ID(id = R.id.tv_out_contour)
	private TextView mOutContour;
	@ID(id = R.id.sb_out_contour)
	private SeekBar mOuterSeek;

	@ID(id = R.id.tv_inner_contour)
	private TextView mInnerContour;
	@ID(id = R.id.sb_inner_contour)
	private SeekBar mInnerSeek;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wheel_view, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWheelView.setText("看");
		mRadioLayout.addDefaultTextViewByResource(R.array.wheel_mode);
		mRadioLayout.setCheckedListener(this);
		mContourRadioLayout.addDefaultTextViewByResource(R.array.inner_contour_mode);
		mContourRadioLayout.setCheckedListener(this);
		mContourLayout.addDefaultTextViewByResource(R.array.contour_mode);
		mContourLayout.setCheckedListener(this);
		mTextLayout.addDefaultTextViewByResource(R.array.text_mode);
		mTextLayout.setCheckedListener(this);

		mDegressSeek.setOnSeekBarChangeListener(this);
		mOuterSeek.setOnSeekBarChangeListener(this);
		mInnerSeek.setOnSeekBarChangeListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.sb_degress:
			// 设置角度
			mWheelView.setProgress(progress);
			mDegress.setText(App.getStr(R.string.degress_value, progress));
			TextFontUtils.setWordColorAndTypedFace(mDegress, Color.GREEN, Typeface.BOLD, progress);
			break;
		case R.id.sb_out_contour:
			// 外环宽
			mWheelView.setOutcontourWidth(UnitUtils.dip2px(getActivity(), 5) * (progress * 1f / seekBar.getMax()));
			mOutContour.setText(App.getStr(R.string.out_contour_value, progress));
			TextFontUtils.setWordColorAndTypedFace(mOutContour, Color.GREEN, Typeface.BOLD, progress);
			break;
		case R.id.sb_inner_contour:
			mWheelView.setInnercontourWidth(UnitUtils.dip2px(getActivity(), 20) * (progress * 1f / seekBar.getMax()));
			mInnerContour.setText(App.getStr(R.string.inner_contour_value, progress));
			TextFontUtils.setWordColorAndTypedFace(mInnerContour, Color.GREEN, Typeface.BOLD, progress);
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

	@Override
	public void checked(View v, int newPosition, int oldPosition) {
		switch (v.getId()) {
		case R.id.rl_radio:
			mWheelView.setWheelMode(newPosition);
			break;
		case R.id.rl_inner_width:
			mWheelView.setInnerContourDrawMode(newPosition);
			break;
		case R.id.rl_contour:
			mWheelView.setContourMode(newPosition);
			break;
		case R.id.rl_text:
			mWheelView.setTextMode(newPosition);
			break;
		default:
			break;
		}
	}

	@Override
	public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
	}
}
