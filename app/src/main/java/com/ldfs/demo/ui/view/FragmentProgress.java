package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ldfs.demo.App;
import com.ldfs.demo.R;

/**
 * 各种状态桢的加载页
 * 
 * @author momo
 * @Date 2015/2/9
 * 
 */
public class FragmentProgress extends ProgressFragment {
	@Override
	public int getLayout() {
		return R.layout.fragment_progress_container;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setTitleShown(true);
		// 功能选择菜单
		RadioGroup radioGroup = new RadioGroup(getActivity());
		radioGroup.setOrientation(RadioGroup.HORIZONTAL);
		String[] options = App.getStringArray(R.array.progress_option);
		RadioButton radioButton = null;
		for (int i = 0; i < options.length; i++) {
			radioButton = new RadioButton(getActivity());
			radioButton.setId(i);
			radioButton.setText(options[i]);
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			radioGroup.addView(radioButton, params);
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case 0:
					setProgressShown(true);
					break;
				case 1:
					setEmptyShown(true);
					break;
				case 2:
					setRepeatRunnable(new Runnable() {
						@Override
						public void run() {
							App.toast(R.string.pull_to_refresh_try);
						}
					});
					break;
				case 3:
					setRepeatSetting();
					break;
				case 4:
					setContainerShown(true);
				default:
					break;
				}
			}
		});
		mTitleBar.addContentView(radioGroup, LayoutParams.WRAP_CONTENT, -1);
	}

}
