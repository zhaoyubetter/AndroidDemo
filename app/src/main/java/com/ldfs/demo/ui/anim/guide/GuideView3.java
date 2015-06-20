package com.ldfs.demo.ui.anim.guide;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.listener.SimpleAnimatorListener;
import com.ldfs.demo.util.ViewInject;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 用户引导�?
 * 
 * @author momo
 * @Date 2014/10/29
 */
public class GuideView3 extends Guide {
	@ID(id = R.id.guide3_image1)
	private View image1;
	@ID(id = R.id.guide3_image2)
	private View image2;
	@ID(id = R.id.guide3_image3)
	private View image3;
	@ID(id = R.id.guide3_image4)
	private View image4;
	@ID(id = R.id.guide3_image5)
	private View image5;
	@ID(id = R.id.guide3_image6)
	private View image6;
	@ID(id = R.id.guide3_image7)
	private View image7;
	@ID(id = R.id.guide3_image8)
	private View image8;
	@ID(id = R.id.tv_surplus_value)
	private TextView surplus;

	public GuideView3(Context context, int mPosition) {
		super(context, mPosition);
		ViewInject.init(this, mView);
	}

	@Override
	int getLayout() {
		return R.layout.guide3_item;
	}

	@Override
	public void pageSelect(int position) {
		// 隐藏其他控件
		setLayoutViewSize(0.6f, false, image1);
		setLayoutViewSize(0.6f, true, image7);
		setLayoutViewSize(0.15f, false, image8);
		setViewLocalion(image3, 0.08f, 0.25f);
		setViewLocalion(image4, 0.55f, 0.35f);

		setViewLocalion(image5, 0.10f, 0.58f);
		setViewLocalion(image6, 0.52f, 0.58f);
		setAlphas(0f, image1, image2, image3, image4, image5, image6, image7, image8);
		setScales(0.1f, image3, image4, image5, image6, image8);
		com.nineoldandroids.view.ViewHelper.setTranslationY(image1, -(image1.getTop() + image1.getHeight()));
		com.nineoldandroids.view.ViewHelper.setTranslationX(image2, App.sWidth - image2.getRight() + image2.getWidth());
		com.nineoldandroids.view.ViewHelper.setTranslationX(image7, -(image7.getLeft() + image7.getWidth()));

		ViewPropertyAnimator.animate(image1).setDuration(ANIMATION_TIME / 2).alpha(1f).translationY(0).setInterpolator(new LinearInterpolator());
		ViewPropertyAnimator.animate(image2).setDuration(ANIMATION_TIME).alpha(1f).translationX(0).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(ANIMATION_TIME / 2)
				.setListener(new SimpleAnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animator) {
						ViewPropertyAnimator.animate(image3).setDuration(ANIMATION_TIME / 2).alpha(1f).scaleX(1f).scaleY(1f);
						ViewPropertyAnimator.animate(image4).setDuration(ANIMATION_TIME / 2).alpha(1f).scaleX(1f).scaleY(1f).setStartDelay(ANIMATION_TIME / 2);
						ViewPropertyAnimator.animate(image5).setDuration(ANIMATION_TIME / 2).alpha(1f).scaleX(1f).scaleY(1f).setStartDelay(ANIMATION_TIME);
						ViewPropertyAnimator.animate(image6).setDuration(ANIMATION_TIME / 2).alpha(1f).scaleX(1f).scaleY(1f).setStartDelay(ANIMATION_TIME + ANIMATION_TIME / 2)
								.setListener(new SimpleAnimatorListener() {
									@Override
									public void onAnimationEnd(Animator animator) {
										ViewPropertyAnimator.animate(image7).setDuration(ANIMATION_TIME).alpha(1f).translationX(0).setInterpolator(new AccelerateDecelerateInterpolator());
										ViewPropertyAnimator.animate(image8).setDuration(ANIMATION_TIME).alpha(1f).scaleX(1f).scaleY(1f).setInterpolator(new LinearInterpolator())
												.setStartDelay(ANIMATION_TIME / 2);
									}
								});
					}
				});
	}

	@Override
	public void pageScrolled(int position, float positionOffset, int offsetValue, boolean isSelectItem) {
		float offset = !isSelectItem ? positionOffset : 1f - positionOffset;
		setAlphas(offset, image1, image2, image3, image4, image5, image6, image7, image8);
		setScales(offset, image3, image4, image5, image6, image8);
	}

}
