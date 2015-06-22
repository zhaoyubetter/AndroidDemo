package com.ldfs.demo.ui.customview;

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
import com.ldfs.demo.widget.ClockWheelView;

/**
 * 时钟进度控件
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.click_wheel_bete_info)
public class FragmentColckWheel extends Fragment {
    @ID(id = R.id.cv_colck_view)
    private ClockWheelView mClockWheelView;
    @ID(id = R.id.tv_scale)
    private TextView mScaleInfo;
    @ID(id = R.id.sb_fraction)
    private SeekBar mSeekFractioin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock_view, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSeekFractioin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float fraction = i * 1.0f / seekBar.getMax();
                mClockWheelView.setScaleFractioin(fraction);
                mScaleInfo.setText(App.getStr(R.string.fraction_value, fraction));
                TextFontUtils.setWordColorAndTypedFace(mScaleInfo, Color.GREEN, Typeface.BOLD, fraction);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @MethodClick(ids = R.id.btn_start)
    public void startAnim(View v) {
        mClockWheelView.startAnim();
    }
}
