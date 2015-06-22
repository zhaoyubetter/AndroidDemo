package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.SimpleFragmenStatePagerAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.PagerStrip;

import static com.ldfs.demo.listener.OperatListener.INIT_DATA;

/**
 * FragmentPagerAdapter条目动态编辑
 * 
 * @author momo
 * @Date 2015/2/11
 * 
 */
@RateInfo(rate= Rate.COMPLETE,beteInfo = R.string.pager_edit_info)
public class FragmentPagerEdit extends Fragment implements OnPageChangeListener {
	@ID(id = R.id.vp_pager)
	private ViewPager pager = null;
	@ID(id = R.id.ps_strip)
	private PagerStrip tabStrip;
	private SimpleFragmenStatePagerAdapter mAdapter;
	private int mLastPosition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pager_item_edit, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int length = 10;
		Fragment[] fragments = new Fragment[length];
		String[] titles = new String[length];
		for (int i = 0; i < length; i++) {
			titles[i] = "title:" + i;
			fragments[i] = FragmentDelayLoadList.instance(i);
		}
		pager.setAdapter(mAdapter = new SimpleFragmenStatePagerAdapter(getChildFragmentManager(), fragments, titles));
		tabStrip.setViewPager(pager);
		tabStrip.setOnPageChangeListener(this);
		onPageSelected(0);
	}

	@MethodClick(ids = { R.id.btn_add, R.id.btn_remove })
	public void editPager(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			mAdapter.addItem();
			tabStrip.notifyDataChange();
			onPageSelected(mLastPosition);
			break;
		case R.id.btn_remove:
			mAdapter.removeLastFragment();
			tabStrip.notifyDataChange();
			pager.setCurrentItem(mAdapter.getCount() - 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		Bundle args = new Bundle();
		args.putInt("position", position);
		mLastPosition = position;
		mAdapter.notifyDataByPosition(position, INIT_DATA, args);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}
}
