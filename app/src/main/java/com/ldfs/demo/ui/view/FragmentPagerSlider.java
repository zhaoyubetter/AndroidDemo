package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.adapter.SimpleFragmentPagerAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.ui.ListTestFragment;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.PagerStrip;

@RateInfo(rate = Rate.COMPLETE_BATE,beteInfo = R.string.slider_bate_info)
public class FragmentPagerSlider extends Fragment {
	@ID(id = R.id.pager_slider)
	private PagerStrip mPagerStrip;
	@ID(id = R.id.vp_pager)
	private ViewPager mPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pager_slider, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] titles = App.getStringArray(R.array.test_array);
		Fragment[] fragments = new Fragment[titles.length];
		for (int i = 0; i < titles.length; i++) {
			fragments[i] = ListTestFragment.instance("data_" + i);
		}
		mPager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager(), fragments, titles));
		mPagerStrip.setViewPager(mPager);
	}
}
