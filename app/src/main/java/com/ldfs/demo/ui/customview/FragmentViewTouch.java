package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.TouchView;

@RateInfo(rate= Rate.COMPLETE,beteInfo = R.string.default_bete_info)
public class FragmentViewTouch extends Fragment {
    @ID(id = R.id.tv_touch_info)
    private TextView mTouchInfo;
    @ID(id = R.id.tv_touch_view)
    private TouchView mTouchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_touch, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTouchView.setOnTouchListener(new TouchView.OnTouchListener() {
            @Override
            public void velocityTracker(float xVelocity, float yVelocity) {
                mTouchInfo.setText("xVelocity:" + xVelocity + " yVelocity:" + yVelocity);
            }
        });
    }
}
