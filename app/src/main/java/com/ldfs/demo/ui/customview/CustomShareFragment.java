package com.ldfs.demo.ui.customview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;

/**
 * A simple {@link Fragment} subclass.
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.share_bete_info)
public class CustomShareFragment extends Fragment {


    public CustomShareFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_share, container, false);
    }


}
