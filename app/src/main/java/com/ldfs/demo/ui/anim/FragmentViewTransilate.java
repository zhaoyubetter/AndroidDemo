package com.ldfs.demo.ui.anim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.translation.TranslationHelper;
import com.ldfs.demo.translation.ViewTranslation;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.RadioGridLayout;

import java.util.ArrayList;

/**
 * 控件组移动动画
 */
public class FragmentViewTransilate extends Fragment {
    @ID(id = R.id.gl_layout1)
    private GridLayout mLayout;
    @ID(id = R.id.rl_radio)
    private RadioGridLayout mRadioLayout;
    @ID(id = R.id.rl_mode)
    private RadioGridLayout mRadioMode;
    @ID(id = R.id.rl_anims)
    private RadioGridLayout mRadioAnim;
    private int mMode;
    private int mAnim;
    private int mGravity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_transilate, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGravity = ViewTranslation.LEFT;
        mRadioLayout.addDefaultTextViewByResource(R.array.anim_duration);
        mRadioLayout.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mGravity = newPosition;
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
        mRadioMode.addDefaultTextViewByResource(R.array.anim_translation);
        mRadioMode.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mMode = newPosition;
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {

            }
        });
        final int[] animValue = new int[]{0x1, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80};
        mRadioAnim.addDefaultTextViewByResource(R.array.anim_arrays);
        mRadioAnim.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
                mAnim = 0;
                for (Integer position : mChoicePositions) {
                    mAnim |= animValue[position];
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        TranslationHelper.startTranslation(this, R.layout.fragment_view_transilate);
    }

    @MethodClick(ids = R.id.btn_start)
    public void startAnim(View v) {
//        ViewTranslation.startTransilate(mLayout, mMode, mDuration, mDuraiton, isReverse);
//        isReverse = !isReverse;
//        ViewTranslation.startAnim(mLayout, mMode, mAnim, mGravity);
        TranslationHelper.endTranslation(this, R.layout.fragment_view_transilate);
    }
}
