package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.PopupMenu;
import com.ldfs.demo.widget.PopupMenu.Gravity;

/**
 * 向各个方向可展开的菜单
 * 
 * @author momo
 * @Date 2015/2/28
 */
public class FragmentPopupMenu extends Fragment implements OnClickListener {
	@ID(id = R.id.tv_left_view, click = true)
	private TextView mLeftView;
	@ID(id = R.id.tv_top_view, click = true)
	private TextView mTopView;
	@ID(id = R.id.tv_right_view, click = true)
	private TextView mRightView;
	@ID(id = R.id.tv_bottom_view, click = true)
	private TextView mBottomView;
	private PopupMenu mLeftMenu;
	private PopupMenu mTopMenu;
	private PopupMenu mRightMenu;
	private PopupMenu mBottomMenu;

	@ID(id = R.id.tv_left_image, click = true)
	private ImageView mLeftImage;
	@ID(id = R.id.tv_top_image, click = true)
	private ImageView mTopImage;
	@ID(id = R.id.tv_right_image, click = true)
	private ImageView mRightImage;
	@ID(id = R.id.tv_bottom_image, click = true)
	private ImageView mBottomImage;
	private PopupMenu mLeftImageMenu;
	private PopupMenu mTopImageMenu;
	private PopupMenu mRightImageMenu;
	private PopupMenu mBottomImageMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_popup_menu, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLeftMenu = new PopupMenu(getActivity(), mLeftView);
		mLeftMenu.setGravity(Gravity.LEFT);
		mTopMenu = new PopupMenu(getActivity(), mTopView);
		mTopMenu.setGravity(Gravity.TOP);
		mRightMenu = new PopupMenu(getActivity(), mRightView);
		mRightMenu.setGravity(Gravity.RIGHT);
		mBottomMenu = new PopupMenu(getActivity(), mBottomView);
		mBottomMenu.setGravity(Gravity.BOTTOM);

		String[] testValues = App.getStringArray(R.array.test_array);
		for (String value : testValues) {
			mLeftMenu.addTextMenu(value, R.drawable.btn_selector);
			mTopMenu.addTextMenu(value, R.drawable.btn_selector);
			mRightMenu.addTextMenu(value, R.drawable.btn_selector);
			mBottomMenu.addTextMenu(value, R.drawable.btn_selector);
		}
		// 设置点击后是否消失
		mLeftMenu.setClickDismiss(true);

		mLeftImageMenu = new PopupMenu(this, mLeftImage);
		mLeftImageMenu.setGravity(Gravity.LEFT);
		mTopImageMenu = new PopupMenu(this, mTopImage);
		mTopImageMenu.setGravity(Gravity.TOP);
		mRightImageMenu = new PopupMenu(this, mRightImage);
		mRightImageMenu.setGravity(Gravity.RIGHT);
		mBottomImageMenu = new PopupMenu(this, mBottomImage);
		mBottomImageMenu.setGravity(Gravity.BOTTOM);

		for (int i = 0; i < 4; i++) {
			mLeftImageMenu.addImageMenu(R.drawable.ic_launcher);
			mTopImageMenu.addImageMenu(R.drawable.ic_launcher);
			mRightImageMenu.addImageMenu(R.drawable.ic_launcher);
			mBottomImageMenu.addImageMenu(R.drawable.ic_launcher);
		}

		// 设置点击后是否消失
		mLeftImageMenu.setClickDismiss(true);
		mTopImageMenu.setClickDismiss(true);
		mRightImageMenu.setClickDismiss(true);
		mBottomImageMenu.setClickDismiss(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_left_view:
			mLeftMenu.toggle();
			break;
		case R.id.tv_top_view:
			mTopMenu.toggle();
			break;
		case R.id.tv_right_view:
			mRightMenu.toggle();
			break;
		case R.id.tv_bottom_view:
			mBottomMenu.toggle();
			break;

		case R.id.tv_left_image:
			mLeftImageMenu.toggle();
			break;
		case R.id.tv_top_image:
			mTopImageMenu.toggle();
			break;
		case R.id.tv_right_image:
			mRightImageMenu.toggle();
			break;
		case R.id.tv_bottom_image:
			mBottomImageMenu.toggle();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}
