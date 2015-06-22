package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.ArrayAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.listener.OperatListener;

/**
 * 延持加载的fragment list
 * 
 * @author momo
 * @Date 2015/2/11
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.delayload_bate_info)
public class FragmentDelayLoadList extends ProgressFragment implements OperatListener {
	private static final String _PARAMS1 = "position";
	@ID(id = R.id.list)
	private ListView mListView;
	private boolean isInit;// 是否初始化

	public static Fragment instance(int position) {
		Fragment fragment = new FragmentDelayLoadList();
		Bundle args = new Bundle();
		args.putInt(_PARAMS1, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getLayout() {
		return R.layout.fragment_list;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setProgressShown(true);
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (null != getActivity() && isInit) {
			int length = 100;
			String[] datas = new String[length];
			for (int i = 0; i < length; i++) {
				datas[i] = "data:" + i;
			}
			mListView.setAdapter(new ArrayAdapter<String>(getActivity(), datas));
			setContainerShown(true);
		}
	}

	@Override
	public void onDetach() {
		isInit = false;
		super.onDetach();
	}

	@Override
	public void onOperate(int optionId, Bundle bundle) {
		if (INIT_DATA == optionId && !isInit) {
			int position = getArguments().getInt(_PARAMS1);
			int selectPosition = bundle.getInt(_PARAMS1);
			if (position == selectPosition) {
				isInit = true;
				initData();
			}
		}
	}

}
