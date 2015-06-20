package com.ldfs.demo.ui.view;

import com.ldfs.demo.R;
import com.ldfs.demo.util.ViewInject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 视图遮罩效果
 * 
 * @author momo
 * @Date 2015/2/8
 * 
 */
public class FragmentViewMask extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_viewmask, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
