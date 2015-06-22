package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.TimerView;

/**
 * 自定义定时控件
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.timer_bete_info)
public class FragmentTimerView extends Fragment {
    @ID(id = R.id.tv_view)
    private TimerView mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer_view, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @MethodClick(ids = R.id.tv_view)
    public void onClick(View v) {
        mView.startAnim();
    }
}
