package com.ldfs.demo.ui.l;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.RecyclerAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * l RecyclerView与cardView
 *
 * @author momo
 * @Date 2015/3/5
 */
public class FragmentRecyclerView extends Fragment {
    @ID(id = R.id.rv_recycler_view)
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view,
                container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            datas.add("Data:" + i);
        }
        mRecyclerView.setAdapter(new RecyclerAdapter(getActivity(), datas));
    }

    @MethodClick(ids = {R.id.tv_horizontal, R.id.tv_vertical})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_horizontal:
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                break;
            case R.id.tv_vertical:
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                break;
            default:
                break;
        }
    }
}
