package com.ldfs.demo.ui.l;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.ViewInject;

/**
 * CircularReveal动画演示
 * 
 * @author momo
 * @Date 2015/3/5
 * 
 */
public class FragmentCircularReveal extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_circular_reveal,
				container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@MethodClick(ids = { R.id.view1, R.id.view2, R.id.view3 })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view1:
			
			break;
		case R.id.view2:
			
			break;
		case R.id.view3:
			
			break;
		default:
			break;
		}
	}
}
