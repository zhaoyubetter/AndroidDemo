package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.Loger;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.DivideLinearLayout;
import com.ldfs.demo.widget.RadioGridLayout;

import java.util.ArrayList;

/**
 * 绘制各个方向,以及子条目分隔线演示
 *
 * @author momo
 * @Date 2015/3/14
 */
@RateInfo(rate= Rate.COMPLETE_BATE,beteInfo = R.string.divider_bete_info)
public class FragmentDivierLayout extends Fragment {
    @ID(id = R.id.dl_divide_layout)
    private DivideLinearLayout mDivideLayout;
    @ID(id = R.id.rl_gravity)
    private RadioGridLayout mGravity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_divier_layout, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGravity.addDefaultTextViewByResource(R.array.divide_layout_gravity);
        final int[] DivideGravity = new int[]{DivideLinearLayout.LEFT, DivideLinearLayout.TOP, DivideLinearLayout.RIGHT, DivideLinearLayout.BOTTOM};
        mGravity.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
                int gravity = 0;
                Loger.i("position:"+mChoicePositions);
                for (Integer position : mChoicePositions) {
                    gravity |= (DivideGravity[position]);
                }
                mDivideLayout.setDivideGravity(gravity);
            }
        });
    }
}
