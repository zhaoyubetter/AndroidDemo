package com.ldfs.demo.ui.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.SubscribeView;

/**
 * 微看点订阅展示功能
 */
@RateInfo(rate = Rate.COMPLETE_BATE,beteInfo = R.string.subscribe_bate_info)
public class FragmentSubscribe extends Fragment {
    @ID(id = R.id.sv_subscribe)
    private SubscribeView mSubscribeView;
    @ID(id = R.id.sb_degress)
    private SeekBar mSeekBar;
    @ID(id = R.id.tv_degress)
    private TextView mDegrees;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubscribeView.addSubscribe("科技", 40, 0xFF5492D6, 16, 1, 15, 10);
        mSubscribeView.addSubscribe("宠物", 30, 0xFFBDC7CD, 12, 2, 50, -15);
        mSubscribeView.addSubscribe("美文", 60, 0xFFD85452, 18, 2, 90, -10);
        mSubscribeView.addSubscribe("健康", 45, 0xFF74D5C1, 16, 2, 180, 0);
        mSubscribeView.addSubscribe("美食", 35, 0xFFB2C4DD, 13, 2, 225, -10);
        mSubscribeView.addSubscribe("娱乐", 50, 0xFF938BD1, 15, 2, 270, -5);
        mSubscribeView.addSubscribe("笑话", 35, 0xFFF38684, 13, 1, 315, 0);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSubscribeView.setDegrees(0, 40, i, 10,1);
                mDegrees.setText(App.getStr(R.string.degress_value, i));
                TextFontUtils.setWordColorAndTypedFace(mDegrees, Color.GREEN, Typeface.BOLD, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @MethodClick(ids = {R.id.btn_start, R.id.btn_restory})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mSubscribeView.startAnim();
                break;
            case R.id.btn_restory:
                mSubscribeView.recovery();
                break;
        }
    }
}
