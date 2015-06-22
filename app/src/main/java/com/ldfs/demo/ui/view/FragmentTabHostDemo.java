package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.adapter.SimpleViewPagerAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义tabhost示例
 *
 * @author momo
 * @date 2015/3/8
 */
@RateInfo(rate = Rate.CODING)
public class FragmentTabHostDemo extends Fragment {
    @ID(id = R.id.vp_pager)
    private ViewPager mPager;
    @ID(id = R.id.th_host)
    private TabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_host, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTabHost.addImageSpec(R.drawable.ic_sb_my_music);
        mTabHost.addImageSpec(R.drawable.ic_sb_personal);
        mTabHost.addImageSpec(R.drawable.ic_sb_search);
        mTabHost.addImageSpec(R.drawable.ic_sb_settings);


        int count = mTabHost.getCount();
        List<View> views = new ArrayList<View>(count);
        for (int i = 0; i < count; i++) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setText("Item:" + i);
            views.add(textView);
        }
        mPager.setAdapter(new SimpleViewPagerAdapter<View>(views));
        mTabHost.setViewPager(mPager);
        mTabHost.setOnTabItemClickListener(new TabHost.OnTabItemClickListener() {
            @Override
            public void onTabItemClick(View v, int position) {
                App.toast("Item_Click:" + position);
            }
        });
    }
}
