package com.ldfs.demo.ui.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.MusicImageView;

/**
 * 播放状态无缝切换更变演示
 *
 * @author momo
 * @Date 2015/3/3
 */
@RateInfo(rate= Rate.COMPLETE)
public class FragmentPlayState extends Fragment {
    @ID(id = R.id.if_play_view)
    private MusicImageView mView;
    @ID(id = R.id.tv_degress)
    private TextView mDegress;
    @ID(id = R.id.sb_radius)
    private SeekBar mSeekBar;
    @ID(id = R.id.tv_padding)
    private TextView mPadding;
    @ID(id = R.id.sb_padding)
    private SeekBar mSeekPadding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_status, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mView.setDegress(progress);
                mDegress.setText(App.getStr(R.string.degress_value, progress));
                TextFontUtils.setWordColorAndTypedFace(mDegress, Color.GREEN, Typeface.BOLD, progress);
            }
        });

        mSeekPadding.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mView.setMusicPadding(UnitUtils.dip2px(getActivity(),progress));
                mPadding.setText(App.getStr(R.string.padding_value, progress));
                TextFontUtils.setWordColorAndTypedFace(mPadding, Color.GREEN, Typeface.BOLD, progress);
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.startAnim();
            }
        });
    }
}
