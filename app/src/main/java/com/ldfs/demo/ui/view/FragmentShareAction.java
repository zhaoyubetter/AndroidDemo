package com.ldfs.demo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.ActivityChooserView;
import com.ldfs.demo.widget.ActivityChooserView.ShareListener;
import com.ldfs.demo.widget.TitleBar;

/**
 * 一个可订制的分享选择器,取自v7包ActivityChooserView,更改其排序规则,加入自定义条目.以应对一些特殊需要的app分享
 * 
 * @author momo
 * @Date 2015/2/9
 * 
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.share_bete_info)
public class FragmentShareAction extends Fragment {
	@ID(id = R.id.titlebar_container)
	private TitleBar mTitleBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_share_action, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActivityChooserView activityShooserView = new ActivityChooserView(getActivity());
		mTitleBar.addContentView(activityShooserView, -1, UnitUtils.dip2px(getActivity(), 30));
		mTitleBar.setBackListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		activityShooserView.setShareListener(new ShareListener() {
			@Override
			public void toShare(Intent shareIntent) {

			}

			@Override
			public void localtionOption(int position) {

			}
		});
	}
}
