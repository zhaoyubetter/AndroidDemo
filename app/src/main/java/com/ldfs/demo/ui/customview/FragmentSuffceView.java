package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.LoveView;

@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.default_bete_info)
public class FragmentSuffceView extends Fragment implements SeekBar.OnSeekBarChangeListener {
    @ID(id = R.id.ll_container)
    private LinearLayout mContainer;
    @ID(id = R.id.sb_width)
    private SeekBar mSeekWidth;
    @ID(id = R.id.sb_height)
    private SeekBar mSeekHeight;
    @ID(id = R.id.lv_love)
    private LoveView mLoveView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surface_view, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSeekWidth.setOnSeekBarChangeListener(this);
        mSeekHeight.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float fraction = i * 1f / seekBar.getMax();
        switch (seekBar.getId()) {
            case R.id.sb_width:
                mLoveView.setWidth((int) (90 * fraction));
                break;
            case R.id.sb_height:
                mLoveView.setHeight((int) (90 * fraction));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
