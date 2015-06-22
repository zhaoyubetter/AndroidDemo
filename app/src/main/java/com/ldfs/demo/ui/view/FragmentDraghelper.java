package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.ArrayAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 控件滑动示例
 * 
 * @author momo
 * @Date 2015/2/10
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.default_bete_info)
public class FragmentDraghelper extends Fragment {
	@ID(id = R.id.view_content)
	private ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_drag, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		List<String> datas = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			datas.add("data:" + i);
		}
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(), datas));
	}
}
