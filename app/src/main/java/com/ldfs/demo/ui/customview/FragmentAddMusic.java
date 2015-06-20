package com.ldfs.demo.ui.customview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.AddMusicView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 添加音乐效果
 */
public class FragmentAddMusic extends Fragment {
    @ID(id = R.id.av_add_music)
    private AddMusicView mAddMusicView;
    @ID(id = R.id.tv_degress)
    private TextView mFractioin;
    @ID(id = R.id.sb_rate)
    private SeekBar mSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_music, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewTreeObserver viewTreeObserver = mAddMusicView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mAddMusicView.getWidth();
                if (0 != width) {
                    mAddMusicView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ViewHelper.setScaleX(mAddMusicView, 0.4f);
                    ViewHelper.setScaleY(mAddMusicView, 0.4f);
                    ViewPropertyAnimator.animate(mAddMusicView).scaleX(1f).scaleY(1f).setDuration(1000);
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float fraction = i * 1f / seekBar.getMax();
//                mAddMusicView.setFraction(fraction);
                mFractioin.setText(App.getStr(R.string.padding_value, i));
                TextFontUtils.setWordColorAndTypedFace(mFractioin, Color.GREEN, Typeface.BOLD, i);
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
        mAddMusicView.add();
    }
}
