package com.ldfs.demo.ui.anim;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.anim.AnimValue;
import com.ldfs.demo.anim.AnimationUtils;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.Loger;
import com.ldfs.demo.util.ViewInject;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * 通过属性动画让数值迭增方式增加
 * 
 * @author momo
 * @Date 2015/2/8
 * 
 */
public class AnimValueFragment extends Fragment {
	private static final int MAX_VALUE = 1000;
	private static final int ANIM_DURATION = 5 * 1000;
	private int[] mColors;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_anim_value, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mColors = new int[] { Color.RED, Color.GREEN, Color.BLUE };
	}

	@MethodClick(ids = { R.id.tv_add_value, R.id.tv_add_value_rvs, R.id.tv_value_to_value })
	public void addValue(final View v) {
		switch (v.getId()) {
		case R.id.tv_add_value:
			startValueAnim(v, 0, false);
			break;
		case R.id.tv_add_value_rvs:
			startValueAnim(v, 0, true);
			break;
		case R.id.tv_value_to_value:
			startValueAnim(v, 100, false);
			break;
		default:
			break;
		}
	}

	private void startValueAnim(final View v, int startValue, boolean isRvs) {
		AnimValue<Integer> animValue = new AnimValue<Integer>(startValue);
		ValueAnimator valueAnimator = ObjectAnimator.ofInt(animValue, "v", MAX_VALUE);
		valueAnimator.setDuration(ANIM_DURATION);
		valueAnimator.setRepeatCount(isRvs ? 1 : 0);
		valueAnimator.setRepeatMode(isRvs ? ValueAnimator.REVERSE : ValueAnimator.INFINITE);
		valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		final float value = 1f / mColors.length;// 阶段性值
		// 起始颜色值
		final int startColor = App.getResourcesColor(R.color.text_color);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 执行数值变动动画
				if (v instanceof TextView) {
					TextView textView = ((TextView) v);
					textView.setText(animator.getAnimatedValue().toString());
					int multiple = (int) (animator.getAnimatedFraction() / value);
					float fraction = animator.getAnimatedFraction() - multiple * value;
					multiple = multiple > mColors.length - 1 ? mColors.length - 1 : multiple;// 防止角标越界
					int color = mColors[multiple];// 当前颜色
					Loger.i("index:" + multiple);
					textView.setTextColor(AnimationUtils.evaluate(fraction, startColor, color));
				}
			}
		});
		valueAnimator.start();
	}
}
