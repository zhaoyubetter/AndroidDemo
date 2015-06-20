package com.ldfs.demo.ui.anim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.Loger;
import com.ldfs.demo.util.ViewInject;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 属性动画演示
 */
public class FragmentPropertiesAnimator extends Fragment {
    @ID(id = R.id.view)
    private View mView;
    private ValueAnimator valueAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties_animator, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.start, Toast.LENGTH_LONG).show();
            }
        });
    }

    @MethodClick(ids = {R.id.btn_alpha, R.id.btn_translation, R.id.btn_rotate, R.id.btn_scale})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alpha:
                float alpha = ViewHelper.getAlpha(mView);
                ViewPropertyAnimator.animate(mView).alpha(0 == alpha ? 1f : 0f);
                break;
            case R.id.btn_scale:
                float scaleX = ViewHelper.getScaleX(mView);
                ViewPropertyAnimator.animate(mView).scaleX(1f == scaleX ? 1.5f : 1f).scaleY(1f == scaleX ? 1.5f : 1f);
                break;
            case R.id.btn_translation:
                float translationX = ViewHelper.getTranslationX(mView);
                ViewPropertyAnimator.animate(mView).translationX(0f == translationX ? 100 : 0f);
                break;
            case R.id.btn_rotate:
                float rotation = ViewHelper.getRotation(mView);
                ViewPropertyAnimator.animate(mView).rotation(0f == rotation ? 360 : 0f);
                break;
        }
    }

    @MethodClick(ids = {R.id.btn_alpha1, R.id.btn_translation1, R.id.btn_rotate1, R.id.btn_scale1})
    public void ObjectAnimatorClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alpha1:
                startAlphaAnim();
                break;
            case R.id.btn_scale1:
                startRepeatAnim();
                break;
            case R.id.btn_translation1:
                if (null != valueAnimator) {
                    valueAnimator.removeAllUpdateListeners();
                    valueAnimator.end();
                    valueAnimator.cancel();
                    valueAnimator = null;
                }
                break;
            case R.id.btn_rotate1:
                break;
        }
    }

    private void startRepeatAnim() {
        valueAnimator = ObjectAnimator.ofInt(360);
        valueAnimator.setDuration(1 * 1000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Loger.i(this, "value:" + valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Loger.i(this, "onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                Loger.i(this, "onAnimationCancel");
            }
        });
        valueAnimator.start();
    }

    private void startAlphaAnim() {
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0.5f);
        PropertyValuesHolder translationXHolder = PropertyValuesHolder.ofFloat("translationX", 100f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mView, alphaHolder, translationXHolder);
        objectAnimator.setRepeatCount(3);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Loger.i(this, "value:" + valueAnimator.getAnimatedValue());
            }
        });
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setDuration(1 * 1000);
        objectAnimator.start();
    }
}
