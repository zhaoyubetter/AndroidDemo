package com.ldfs.demo.ui.anim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;

/**
 * 属性动画控件view拌动效果
 * 
 * @author momo
 * @Date 2015/2/8
 * 
 */
@RateInfo(rate= Rate.COMPLETE)
public class AnimShakeFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_anim_shake, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@MethodClick(ids = { R.id.tv_shake_textview, R.id.tv_shake_imageview, R.id.tv_shake_view, R.id.tv_shake_layout })
	public void shakeView(View v) {
		// TODO 其他操作逻缉
	}
}
