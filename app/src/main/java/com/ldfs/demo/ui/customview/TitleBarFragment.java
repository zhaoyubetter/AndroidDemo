package com.ldfs.demo.ui.customview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitleBarFragment extends Fragment {


    public TitleBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_title_bar, container, false);
    }


}
