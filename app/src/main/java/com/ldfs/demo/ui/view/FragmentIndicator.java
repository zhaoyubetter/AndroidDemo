package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.SimpleViewPagerAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.indicator.ViewPagerIndicator;

import java.util.ArrayList;

@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.pager_indicator_bete_info)
public class FragmentIndicator extends Fragment implements OnSeekBarChangeListener, OnCheckedChangeListener {
	@ID(id = R.id.vp_circle_pager)
	private ViewPager mPager;
	@ID(id = R.id.vp_indicator)
	private ViewPagerIndicator mIndicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_indicator, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<ImageView> views = new ArrayList<ImageView>();
		int length = 5;
		for (int i = 0; i < length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setImageResource(R.drawable.ic_launcher);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			views.add(imageView);
		}
		mPager.setAdapter(new SimpleViewPagerAdapter<ImageView>(views, true));
		mIndicator.setViewPager(mPager, length);

		setOnSeekBarListener(R.id.sb_radius_size, R.id.sb_radius_padding, R.id.sb_radius_scale);
		setOnCheckedChangeListener(R.id.rg_gravity, R.id.rg_anim_type);
	}

	private void setOnSeekBarListener(int... ids) {
		View view = getView();
		for (int i = 0; i < ids.length; i++) {
			View findView = view.findViewById(ids[i]);
			if (findView instanceof SeekBar) {
				((SeekBar) findView).setOnSeekBarChangeListener(this);
			}
		}
	}

	private void setOnCheckedChangeListener(int... ids) {
		View view = getView();
		for (int i = 0; i < ids.length; i++) {
			View findView = view.findViewById(ids[i]);
			if (findView instanceof RadioGroup) {
				((RadioGroup) findView).setOnCheckedChangeListener(this);
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.sb_radius_size:
			// 默认大小
			float radius = UnitUtils.dip2px(getActivity(), 10);
			mIndicator.setRadius(radius + (radius / 2 * progress * 0.01f));
			break;
		case R.id.sb_radius_padding:
			// 默认大小
			float padding = UnitUtils.dip2px(getActivity(), 2);
			mIndicator.setPadding(padding + (padding * 5 * progress * 0.01f));
			break;
		case R.id.sb_radius_scale:
			float scaleSize = UnitUtils.dip2px(getActivity(), 5);
			mIndicator.setScaleSize(scaleSize * progress * 0.01f);
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.rg_gravity:
			mIndicator.setGravity(group.indexOfChild(group.findViewById(checkedId)));
			break;
		case R.id.rg_anim_type:
			mIndicator.setCircleType(group.indexOfChild(group.findViewById(checkedId)));
			break;
		default:
			break;
		}
	}
}
