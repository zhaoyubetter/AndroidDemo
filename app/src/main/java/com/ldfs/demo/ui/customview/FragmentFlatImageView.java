package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.ImageViewFlat;
import com.ldfs.demo.widget.RadioGridLayout;

import java.util.ArrayList;

@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.flat_bete_info)
public class FragmentFlatImageView extends Fragment {
    @ID(id = R.id.flat_view)
    private ImageViewFlat mFlatView;
    @ID(id = R.id.rl_flat)
    private RadioGridLayout mRadioLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imageview_flat, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFlatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                App.toast(R.string.test);
            }
        });
        mRadioLayout.addDefaultTextViewByResource(R.array.flat_type);
        mRadioLayout.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mFlatView.setFlatType(newPosition);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
    }
}
