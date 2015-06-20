package com.ldfs.demo.ui.customview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.MusicProgressBar;

/**
 * 进度条,以及进度截取
 */
public class FragmentMusicProgressBar extends Fragment {
    @ID(id = R.id.tv_rate)
    private TextView mRateInfo;
    @ID(id = R.id.mp_progress)
    private MusicProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_progressbar, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressBar.setOnSelectListener(new MusicProgressBar.OnSelectListener() {
            @Override
            public void onSelect(MusicProgressBar progressBar, int start, int end, int value) {
                mRateInfo.setText(App.getStr(R.string.select_value, start, end, value));
                TextFontUtils.setWordColorAndTypedFace(mRateInfo, Color.RED, Typeface.BOLD, start, end, value);
            }
        });
//        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(100);
//        valueAnimator.setDuration(10 * 1000);
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
//        valueAnimator.setRepeatCount(-1);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                Float progress = Float.valueOf(valueAnimator.getAnimatedValue().toString());
//                mProgressBar.setProgress(progress);
//            }
//        });
//        valueAnimator.start();
    }

    @MethodClick(ids = R.id.btn_swrp)
    public void OnClick(View view) {
        mProgressBar.wrapMode();
    }
}
