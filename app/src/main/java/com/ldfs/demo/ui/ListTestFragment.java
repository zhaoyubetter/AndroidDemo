package com.ldfs.demo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;

/**
 * 列表测试类
 * 
 * @author momo
 * @Date 2014/12/13
 * 
 */
public class ListTestFragment extends Fragment {
	public static final String _PARAMS1 = "value";
	@ID(id = R.id.list)
	private ListView list;

	public static Fragment instance(String value) {
		Fragment fragment = new ListTestFragment();
		Bundle args = new Bundle();
		args.putString(_PARAMS1, value);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle arguments = getArguments();
		if (null != arguments) {
			String value = arguments.getString(_PARAMS1);
			int count = 100;
			String[] values = new String[count];
			for (int i = 0; i < count; i++) {
				values[i] = value + "i";
			}
			list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values));
		}
	}
}
