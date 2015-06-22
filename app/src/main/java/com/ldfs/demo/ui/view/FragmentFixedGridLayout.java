package com.ldfs.demo.ui.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.anim.AnimValue;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.FixedGridLayout;
import com.ldfs.demo.widget.RadioGridLayout;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * 自定义gridLayout控件组,控件计算与布局来自api Demo
 *
 * @author momo
 * @Date 2015/2/12
 */
@RateInfo(rate= Rate.COMPLETE)
public class FragmentFixedGridLayout extends Fragment {
    @ID(id = R.id.fl_grid_layout)
    private FixedGridLayout mGridLayout;
    @ID(id = R.id.rl_radio)
    private RadioGridLayout mRadioLayout;
    @ID(id = R.id.sb_seek_padding)
    private SeekBar mSeekBar;
    private int mLayoutWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fixed_gridlayout, container, false);
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
                int padding = UnitUtils.dip2px(getActivity(), 20);
                mGridLayout.setItemPadding((int) (padding * progress * 1f / seekBar.getMax()));
            }
        });
        mRadioLayout.addDefaultTextViewByResource(R.array.view_gravity);
        mRadioLayout.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mGridLayout.setItemGravity(newPosition);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {

            }
        });
    }

    @MethodClick(ids = {R.id.btn_auto_resize, R.id.btn_add, R.id.btn_remove})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto_resize:
                randomResize();
                break;
            case R.id.btn_add:
                mGridLayout.addView(getTextView());
                break;
            case R.id.btn_remove:
                // 移除最后一个
                mGridLayout.removeViewAt(mGridLayout.getChildCount() - 1);
                break;
            default:
                break;
        }
    }

    public TextView getTextView() {
        TextView textView = new TextView(getActivity());
        int padding = UnitUtils.dip2px(getActivity(), 5);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText("text:"+mGridLayout.getChildCount());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.WHITE);
        textView.setSingleLine();
        textView.setBackgroundResource(R.color.green);
        textView.setCompoundDrawablePadding(padding);
        return textView;
    }

    /**
     * 随机布局
     */
    private void randomResize() {
        mLayoutWidth = 0 == mLayoutWidth ? mGridLayout.getWidth() : mLayoutWidth;
        AnimValue<Integer> animValue = new AnimValue<Integer>(mLayoutWidth);
        ObjectAnimator animator = ObjectAnimator.ofInt(animValue, "v", new Random().nextInt(mLayoutWidth / 2) + mLayoutWidth / 2);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams params = mGridLayout.getLayoutParams();
                params.width = Integer.valueOf(valueAnimator.getAnimatedValue().toString());
                mGridLayout.requestLayout();
            }
        });
        animator.start();

    }
}
