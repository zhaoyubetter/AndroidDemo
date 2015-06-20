package com.ldfs.demo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ldfs.demo.listener.OperatListener;

/**
 * 首页viewpager的adapter
 * 
 * @author Administrator
 * 
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
	private Fragment[] fragments = null;
	private String[] titles;

	public SimpleFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
		super(fm);
		this.fragments = fragments;
	}

	public SimpleFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null != titles ? titles[position] : super.getPageTitle(position);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	/**
	 * 通知笑话列表初始化数据
	 * 
	 * @param optionId
	 * @param args
	 */
	public void notifyData(int optionId, Bundle args) {
		int length = fragments.length;
		for (int i = 0; i < length; i++) {
			if (fragments[i] instanceof OperatListener) {
				((OperatListener) fragments[i]).onOperate(optionId, args);
			}
		}
	}

	/**
	 * 通知笑话列表初始化数据
	 * 
	 * @param optionId
	 * @param args
	 */
	public void notifyDataByPosition(int position, int optionId, Bundle args) {
		if (0 > position)
			return;
		if (position < fragments.length) {
			Fragment fragment = fragments[position];
			if (fragment instanceof OperatListener) {
				((OperatListener) fragment).onOperate(optionId, args);
			}
		}
	}
}
