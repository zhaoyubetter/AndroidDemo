package com.ldfs.drawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ldfs.drawable.annotation.ID;
import com.ldfs.drawable.annotation.ViewInject;
import com.ldfs.drawable.drawable.MagnifierDrawable;
import com.ldfs.drawable.drawable.MusicDrawable;
import com.ldfs.drawable.drawable.MusicStateDrawable;
import com.ldfs.drawable.drawable.PathEffectDrawable;
import com.ldfs.drawable.drawable.PersonalDrawable;
import com.ldfs.drawable.drawable.SettingDrawable;
import com.ldfs.drawable.drawable.StatisticsDrawable;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 自定义Drawable演示
 */
public class FragmentCustomDrawable extends Fragment implements SeekBar.OnSeekBarChangeListener {
    @ID(id = R.id.iv_music)
    private ImageView music;
    @ID(id = R.id.iv_path_effect)
    private ImageView mPathEffect;
    @ID(id = R.id.iv_statistics)
    private ImageView mStatistics;
    @ID(id = R.id.iv_music_play)
    private ImageView mMusicPlay;
    @ID(id = R.id.iv_magnifier)
    private ImageView mMagnifier;
    @ID(id = R.id.iv_setting)
    private ImageView mSetting;
    @ID(id = R.id.iv_personel)
    private ImageView mPresonel;

    @ID(id = R.id.tv_width)
    private TextView mWidth;
    @ID(id = R.id.sb_size)
    private SeekBar mSeekSize;

    private SettingDrawable mSettingDrawable;
    private PersonalDrawable mPersonalDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_drawable, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setImageDrawables();

        mSeekSize.setOnSeekBarChangeListener(this);
    }

    private void setImageDrawables() {
        //音乐播放drawable
        final MusicDrawable drawable = new MusicDrawable.MusicDrawableBuild().setStrokeWidth(5).build();
        music.setImageDrawable(drawable);
        drawable.startAnim();

        //路径绘制
        final PathEffectDrawable pathEffectDrawable = new PathEffectDrawable();
        mPathEffect.setImageDrawable(pathEffectDrawable);
        pathEffectDrawable.startAnim();
        //统计图标
        StatisticsDrawable statisticsDrawable = new StatisticsDrawable();
        mStatistics.setImageDrawable(statisticsDrawable);
        statisticsDrawable.startAnim(-1, -1, 1, 3 * 1000);

        //音乐播放
        MusicStateDrawable musicStateDrawable = new MusicStateDrawable();
        mMusicPlay.setImageDrawable(musicStateDrawable);
        musicStateDrawable.startAnim(-1, ValueAnimator.REVERSE, 90, 3 * 1000);

        //放大镜
        final MagnifierDrawable magnifierDrawable = new MagnifierDrawable();
        mMagnifier.setImageDrawable(magnifierDrawable);
        magnifierDrawable.startAnim(-1, -1, 1, 3000);

        //系统设置
        mSettingDrawable = new SettingDrawable();
        mSetting.setImageDrawable(mSettingDrawable);
        mSettingDrawable.startAnim(-1, -1, 1,3000);

        //人员图标
        mPersonalDrawable = new PersonalDrawable();
        mPresonel.setImageDrawable(mPersonalDrawable);
        mPersonalDrawable.startAnim(-1, ValueAnimator.REVERSE, 1, 3000);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float fraction = i * 1f / seekBar.getMax();
        int size = (int) (100 + 100 * fraction);
        switch (seekBar.getId()) {
            case R.id.sb_size:
//                100-200;
                setViewSize(size, music, mStatistics, mPathEffect, mMusicPlay, mMagnifier, mSetting, mPresonel);
                mWidth.setText(App.getStr(R.string.size_value, size));
                break;
        }
    }

    private void setViewSize(int size, ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            imageView.requestLayout();
        }
//        setImageDrawables();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
