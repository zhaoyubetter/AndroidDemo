package com.ldfs.demo.ui.anim.guide;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 用户引导�?
 * 
 * @author momo
 * @Date 2014/10/29
 */
public class GuideView1 extends Guide {
	@ID(id = R.id.guide1_image1)
	private ImageView cheese1;
	@ID(id = R.id.guide1_image2)
	private ImageView cheese2;

	public GuideView1(Context context, int mPosition) {
		super(context, mPosition);
		ViewInject.init(this, mView);
	}

	@Override
	int getLayout() {
		return R.layout.guide1_item;
	}

	@Override
	public void pageSelect(int position) {
		setAlphas(0f, cheese1, cheese2);
		setLayoutViewSize(0.7f, true, cheese1);
		setLayoutViewSize(0.9f, true, cheese2);
		com.nineoldandroids.view.ViewHelper.setTranslationY(cheese1, -(cheese1.getHeight() + cheese1.getTop()));
		com.nineoldandroids.view.ViewHelper.setTranslationY(cheese2, (App.sHeight + cheese2.getHeight()));
		ViewPropertyAnimator.animate(cheese1).alpha(1f).translationY(0).setInterpolator(new LinearInterpolator()).setDuration(ANIMATION_TIME);
		ViewPropertyAnimator.animate(cheese2).alpha(1f).translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(ANIMATION_TIME).setStartDelay(ANIMATION_TIME / 2);
	}

	@Override
	public void pageScrolled(int position, float positionOffset, int offsetValue, boolean isSelectItem) {
		com.nineoldandroids.view.ViewHelper.setTranslationY(cheese1, -(cheese1.getHeight() + cheese1.getBottom()) * positionOffset);
		com.nineoldandroids.view.ViewHelper.setTranslationY(cheese2, (App.sHeight + cheese2.getHeight()) * positionOffset);
		setAlphas(1.0f - positionOffset, cheese1, cheese2);
	}

}
