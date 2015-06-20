package com.ldfs.demo.ui.anim;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ldfs.demo.R;
import com.ldfs.demo.anim.AnimationUtils;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.ImageUtils;
import com.ldfs.demo.util.ViewInject;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * 动画控件颜色变幻
 * 
 * @author momo
 * @Date 2015/2/8
 */
public class AnimColorFragment extends Fragment {
	@ID(id = R.id.view)
	private View mView;
	@ID(id = R.id.iv_image)
	private ImageView mImageView;
	@ID(id = R.id.iv_filter_image)
	private ImageView mFilterImage;
	@ID(id = R.id.seekBar)
	private SeekBar mSeekBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_anim_color, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 背景颜色变幻
		AnimationUtils.setBackGroundAnim(mView, 3 * 1000, Color.RED, Color.GREEN, Color.BLUE);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float fraction = progress * 1.0f / seekBar.getMax();
				ImageUtils.setDrawableScale(mFilterImage, AnimationUtils.evaluate(fraction, Color.WHITE, Color.RED));
			}
		});
	}

	/**
	 * 更变颜色矩阵
	 * 
	 * @param v
	 */
	@MethodClick(ids = R.id.iv_image)
	public void startDrawableAnim(View v) {
		ValueAnimator valueAnimator = ObjectAnimator.ofFloat(1f);
		valueAnimator.setDuration(1 * 1000);
		valueAnimator.setRepeatCount(1);
		valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 白色到红色
				ImageUtils.setDrawableScale(mImageView, AnimationUtils.evaluate(animator.getAnimatedFraction(), Color.WHITE, Color.RED));
			}
		});
		valueAnimator.start();
	}

}
