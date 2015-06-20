package com.ldfs.demo.ui.customview;

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
import com.ldfs.demo.widget.ListIndicator;

/**
 * listview左侧滑动
 */
public class FragmentListIndicator extends Fragment {
    @ID(id = R.id.lv_listview)
    private ListView mListView;
    @ID(id = R.id.li_indicator)
    private ListIndicator mIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listindicator, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new String[]{"A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B", "A", "B"}));
        mIndicator.setData(new String[]{"1","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"});
    }
}
