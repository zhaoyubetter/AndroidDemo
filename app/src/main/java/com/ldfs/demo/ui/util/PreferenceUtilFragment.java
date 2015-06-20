package com.ldfs.demo.ui.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.ViewClick;
import com.ldfs.demo.preference.ConfigName;
import com.ldfs.demo.preference.PrefernceUtils;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.ViewInject;

/**
 * prefernce工具类测试
 * 
 * @author momo
 * @Date 2014/11/28
 */
@ViewClick(ids = R.id.btn_save)
public class PreferenceUtilFragment extends Fragment implements OnClickListener {
	@ID(id = R.id.rg_params_type)
	private RadioGroup radioGroup;
	@ID(id = R.id.et_value)
	private EditText editor;
	@ID(id = R.id.tv_prefernce_value)
	private TextView info;
	@ID(id = R.id.tv_now_value)
	private TextView myValue;
	private int mCheckedId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_util_preference, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		info.setText(PrefernceUtils.getValue());
		mCheckedId = radioGroup.getCheckedRadioButtonId();
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mCheckedId = checkedId;
			}
		});
	}

	@Override
	public void onClick(View v) {
		String value = editor.getText().toString();
		switch (mCheckedId) {
		case R.id.rb_type_int:
			int number = -1;
			try {
				number = Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
			PrefernceUtils.setInt(ConfigName.IS_INIT, number);
			break;
		case R.id.rb_type_str:
			PrefernceUtils.setString(ConfigName.ITEM_VERSION, value);
			break;
		case R.id.rb_type_bool:
			boolean result = Boolean.parseBoolean(value);
			PrefernceUtils.setBoolean(ConfigName.NEW_VERSION, result);
			break;
		default:
			break;
		}
		info.setText(App.getStr(R.string.file_value, PrefernceUtils.getValue()));
		TextFontUtils.setWordColor(info, Color.GREEN, PrefernceUtils.getValue());// 设置红色
		TextFontUtils.setWordTypedFace(info, Typeface.BOLD, PrefernceUtils.getValue());// 设置粗体

		myValue.setText(App.getStr(R.string.cache_value, PrefernceUtils.getMyValue()));
		TextFontUtils.setWordColor(myValue, Color.GREEN, PrefernceUtils.getMyValue());// 设置红色
		TextFontUtils.setWordTypedFace(myValue, Typeface.BOLD, PrefernceUtils.getMyValue());// 设置粗体
	}
}
