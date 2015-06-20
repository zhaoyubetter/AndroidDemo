package com.ldfs.demo.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.ArrayAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.DismissListView;
import com.ldfs.demo.widget.DismissListView.OnDismissListener;

/**
 * 点击条目消失的ListView
 * @author momo
 * @Date 2015/2/10
 *
 */
public class FragmentDismissListView extends Fragment {
	@ID(id = R.id.list)
	private ListView list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		List<String> datas = new ArrayList<String>(100);
		for (int i = 0; i < 100; i++) {
			datas.add("data:" + i);
		}
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), datas);
		list.setAdapter(arrayAdapter);
		DismissListView dismissList = new DismissListView(list);
		dismissList.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(int position) {
				arrayAdapter.remove(position);
			}
		});
	}

}
