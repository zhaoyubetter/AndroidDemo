package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;

/**
 * 视图遮罩效果
 * 
 * @author momo
 * @Date 2015/2/8
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.guide_bate_info)
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
