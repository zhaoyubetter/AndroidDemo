package com.ldfs.demo.ui.customview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPagerIndicatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@Navigation(title=R.string.pager_indicator_title)
public class ViewPagerIndicatorFragment extends Fragment {

    public static ViewPagerIndicatorFragment newInstance() {
        return new ViewPagerIndicatorFragment();
    }

    public ViewPagerIndicatorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_view_pager_indicator, container, false);
        return view;
    }


}
