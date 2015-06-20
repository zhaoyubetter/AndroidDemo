package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.TitleBar;

/**
 * 自定义的Titlebar测试,用以针对项目标题不统一情况,可设定类actionBar或苹果中间样式
 * 
 * @author momo
 * @Date 2015/2/9
 * 
 */
public class FragmentTitleBar extends Fragment implements OnCheckedChangeListener, OnClickListener {
	@ID(id = R.id.titlebar_container)
	private TitleBar mTitleBar;
	@ID(id = R.id.cb_display_home)
	private CheckBox mDisplayHome;
	@ID(id = R.id.cb_show_loading)
	private CheckBox mShowLoading;
	@ID(id = R.id.cb_show_title)
	private CheckBox mShowTitle;
	@ID(id = R.id.cb_center_title)
	private CheckBox mShowCenterTitle;
	@ID(id = R.id.cb_show_divier)
	private CheckBox mShowDivier;
	private int mMenuId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_titlebar, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mTitleBar.setBackListener(this);
		mDisplayHome.setOnCheckedChangeListener(this);
		mShowLoading.setOnCheckedChangeListener(this);
		mShowTitle.setOnCheckedChangeListener(this);
		mShowCenterTitle.setOnCheckedChangeListener(this);
		mShowDivier.setOnCheckedChangeListener(this);
	}

	@MethodClick(ids = { R.id.btn_add_menu, R.id.btn_add_imagemenu, R.id.btn_remove_menu, R.id.btn_clear_menu })
	public void MenuClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_menu:
			final int textId = mMenuId++;
			mTitleBar.addTextMenu(textId, R.string.test, this);
			break;
		case R.id.btn_add_imagemenu:
			final int imageId = mMenuId++;
			mTitleBar.addImageMenu(imageId, R.drawable.ic_launcher, -1, this);
			break;
		case R.id.btn_remove_menu:
			mTitleBar.removeLastMenu();
			break;
		case R.id.btn_clear_menu:
			mTitleBar.clearMenu();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_display_home:
			mTitleBar.setDisplayHome(isChecked);
			break;
		case R.id.cb_show_loading:
			mTitleBar.showIndeterminate(isChecked);
			break;
		case R.id.cb_show_title:
			mTitleBar.setTitleVisible(isChecked ? View.VISIBLE : View.GONE);
			break;
		case R.id.cb_center_title:
			mTitleBar.setPageTitleVisible(isChecked);
			break;
		case R.id.cb_show_divier:
			mTitleBar.showItemDivier(isChecked);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (R.id.titlebar_back == v.getId()) {
			getFragmentManager().popBackStack();
		} else {
			App.toast(v.getId() + " click!");
		}
	}
}
