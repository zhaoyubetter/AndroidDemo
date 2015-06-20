package com.ldfs.demo.ui.anim;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.SimpleViewPagerAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.ui.anim.guide.Guide;
import com.ldfs.demo.ui.anim.guide.GuideView1;
import com.ldfs.demo.ui.anim.guide.GuideView2;
import com.ldfs.demo.ui.anim.guide.GuideView3;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.CircleNavigationBar;

/**
 * 动画引导页
 * 
 * @author momo
 * @Date 2015/2/8
 * 
 */
public class AnimGuideFragment extends Fragment implements OnPageChangeListener{
	@ID(id = R.id.vp_pager)
	private ViewPager pager;
	@ID(id = R.id.cn_navigationbar)
	private CircleNavigationBar navigationBar;
	private List<Guide> guides;

	public static Fragment instance(Bundle args) {
		AnimGuideFragment fragment = new AnimGuideFragment();
		if (null != args) {
			fragment.setArguments(args);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_anim_guide, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initIntros();
	}

	private void initIntros() {
		FragmentActivity activity = getActivity();
		if (null != activity) {
			Guide lastGuide = null;
			guides = new ArrayList<Guide>();
			guides.add(new GuideView1(activity, guides.size()));
			guides.add(new GuideView3(activity, guides.size()));
			guides.add(lastGuide = new GuideView2(activity, guides.size()));
			List<View> views = new ArrayList<View>();
			for (int i = 0, length = guides.size(); i < length; i++) {
				views.add(guides.get(i).getView());
			}
			pager.setAdapter(new SimpleViewPagerAdapter<View>(views));
			pager.setOnPageChangeListener(this);
			onPageSelected(0);
			navigationBar.setContentViewPager(pager);
			lastGuide.setCompleteAction(new Runnable() {
				@Override
				public void run() {
					navigationBar.postDelayed(new Runnable() {
						@Override
						public void run() {
							toDesktop();
						}
					}, 1 * 1000);
				}
			});
		}
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int offsetValue) {
		navigationBar.scrollToPosition(position, positionOffset);
		if (position + 1 < guides.size()) {
			guides.get(position + 1).onPageScrolled(position + 1, positionOffset, offsetValue, false);
		}
		guides.get(position).onPageScrolled(position, positionOffset, offsetValue, true);
	}

	@Override
	public void onPageSelected(int position) {
		guides.get(position).onPageSelected(position);
	}

	private void toDesktop() {
		Toast.makeText(getActivity(), "打开主页~", Toast.LENGTH_SHORT).show();
	}
}
