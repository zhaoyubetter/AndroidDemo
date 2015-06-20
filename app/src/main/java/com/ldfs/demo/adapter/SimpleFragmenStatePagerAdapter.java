package com.ldfs.demo.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ldfs.demo.listener.OperatListener;
import com.ldfs.demo.ui.view.FragmentDelayLoadList;

/**
 * 可编辑fragment条目的viewpager的adapter
 * 
 * @author Administrator
 * 
 */
public class SimpleFragmenStatePagerAdapter extends FragmentStatePagerAdapter {
	private final List<Fragment> fragments;
	private final List<String> titles;
	private int[] positions;

	public SimpleFragmenStatePagerAdapter(FragmentManager fm, Fragment[] fragments) {
		this(fm, fragments, null, null);
	}

	public SimpleFragmenStatePagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
		this(fm, fragments, titles, null);
	}

	public SimpleFragmenStatePagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles, int[] positions) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		this.fragments.addAll(Arrays.asList(fragments));
		this.titles = new ArrayList<String>();
		if (null != titles) {
			this.titles.addAll(Arrays.asList(titles));
		}
		if (null != positions) {
			this.positions = positions;
		} else {
			this.positions = new int[fragments.length];
			for (int i = 0; i < fragments.length; this.positions[i] = i, i++)
				;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return !titles.isEmpty() ? titles.get(positions[position]) : super.getPageTitle(position);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(positions[position]);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	/**
	 * 通知笑话列表初始化数据
	 * 
	 * @param optionId
	 * @param args
	 */
	public void notifyData(int optionId, Bundle args) {
		int size = fragments.size();
		for (int i = 0; i < size; i++) {
			if (fragments.get(i) instanceof OperatListener) {
				((OperatListener) fragments.get(i)).onOperate(optionId, args);
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
		if (position < fragments.size()) {
			Fragment fragment = fragments.get(positions[position]);
			if (fragment instanceof OperatListener) {
				((OperatListener) fragment).onOperate(optionId, args);
			}
		}
	}

	/**
	 * 动态添加一个fragment
	 * 
	 * @param fragment
	 */
	public void addItem() {
		int count = getCount();
		this.titles.add("title" + count);
		this.fragments.add(FragmentDelayLoadList.instance(count));
		int[] newPositons = new int[count + 1];
		System.arraycopy(positions, 0, newPositons, 0, positions.length);
		newPositons[count] = count;
		positions = newPositons;
		notifyDataSetChanged();
	}

	/**
	 * 测试方法,移除最后一个fragment
	 */
	public void removeLastFragment() {
		int count = getCount();
		this.titles.remove(count - 1);
		this.fragments.remove(count - 1);
		notifyDataSetChanged();
	}

}
