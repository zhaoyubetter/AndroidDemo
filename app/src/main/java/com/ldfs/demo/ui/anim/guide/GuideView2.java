package com.ldfs.demo.ui.anim.guide;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 用户引导�?
 * 
 * @author momo
 * @Date 2014/10/29
 */
public class GuideView2 extends Guide implements OnClickListener {
	@ID(id = R.id.guide2_image1)
	private ImageView cheese1;
	@ID(id = R.id.guide2_image2)
	private ImageView cheese2;
	@ID(id = R.id.guide2_image3)
	private ImageView cheese3;
	@ID(id = R.id.guide2_image4)
	private ImageView cheese4;
	@ID(id = R.id.guide2_image5, click = true)
	private ImageView cheese5;

	public GuideView2(Context context, int mPosition) {
		super(context, mPosition);
		ViewInject.init(this, mView);

	}

	@Override
	int getLayout() {
		return R.layout.guide2_item;
	}

	@Override
	public void pageSelect(int position) {
		// 隐藏三个控件
		setLayoutViewSize(1f, true, cheese1);
		setLayoutViewSize(0.8f, true, cheese3);
		setLayoutViewSize(0.9f, true, cheese4);
		setLayoutViewSize(0.5f, false, cheese2);
		setRotations(0.1f, cheese5);
		setAlphas(0f, cheese1, cheese2, cheese3, cheese4, cheese5);
		com.nineoldandroids.view.ViewHelper.setTranslationX(cheese1, -cheese1.getWidth());
		com.nineoldandroids.view.ViewHelper.setTranslationY(cheese2, cheese2.getHeight() + cheese2.getBottom());
		ViewPropertyAnimator.animate(cheese1).alpha(1f).setDuration(ANIMATION_TIME).setInterpolator(new AccelerateDecelerateInterpolator()).translationX(0);
		ViewPropertyAnimator.animate(cheese2).alpha(1f).setDuration(ANIMATION_TIME).setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setStartDelay(ANIMATION_TIME / 2);
		// 旋转显示出内层圈
		ViewPropertyAnimator.animate(cheese3).setDuration(ANIMATION_TIME * 3).setInterpolator(new LinearInterpolator()).alpha(1.0f).rotation(360f).setStartDelay(ANIMATION_TIME);
		// 显示外层�?
		ViewPropertyAnimator.animate(cheese4).setDuration(ANIMATION_TIME * 2).setInterpolator(new LinearInterpolator()).alpha(1.0f).rotation(-360f).setStartDelay(ANIMATION_TIME * 2);
		ViewPropertyAnimator.animate(cheese5).setDuration(ANIMATION_TIME / 2).setInterpolator(new AccelerateDecelerateInterpolator()).alpha(1.0f).scaleX(1f).scaleY(1f)
				.setStartDelay(ANIMATION_TIME * 2);
	}

	@Override
	public void pageScrolled(int position, float positionOffset, int offsetValue, boolean isSelectItem) {
		setAlphas(!isSelectItem ? positionOffset : 1.0f - positionOffset, cheese1, cheese2, cheese3, cheese4);
		float offset = !isSelectItem ? positionOffset : 1f - positionOffset;
		com.nineoldandroids.view.ViewHelper.setScaleX(cheese3, 0.5f > offset ? 0.5f : offset);
		com.nineoldandroids.view.ViewHelper.setScaleY(cheese3, 0.5f > offset ? 0.5f : offset);
		com.nineoldandroids.view.ViewHelper.setScaleX(cheese4, 0.5f > offset ? 0.5f : offset);
		com.nineoldandroids.view.ViewHelper.setScaleY(cheese4, 0.5f > offset ? 0.5f : offset);
		com.nineoldandroids.view.ViewHelper.setRotation(cheese3, !isSelectItem ? positionOffset * 180f : (1.0f - positionOffset) * 180f);
		com.nineoldandroids.view.ViewHelper.setRotation(cheese4, !isSelectItem ? positionOffset * -180f : (1.0f - positionOffset) * -180f);
	}

	@Override
	public void onClick(View v) {
		if (R.id.guide2_image5 == v.getId() && null != completeAction) {
			completeAction.run();
		}
	}

}
