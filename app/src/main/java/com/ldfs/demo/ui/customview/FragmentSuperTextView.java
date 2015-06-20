package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.RadioGridLayout;
import com.ldfs.demo.widget.SuperTextView;

import java.util.ArrayList;

/**
 * 自定义的textView
 */
public class FragmentSuperTextView extends Fragment {
    @ID(id = R.id.sv_view)
    private SuperTextView mTextView;
    @ID(id = R.id.rl_gravity)
    private RadioGridLayout mRadioLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_super_textview, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int[] gravitys = new int[]{0x00, 0x01, 0x02, 0x04, 0x08};
        mRadioLayout.addDefaultTextViewByResource(R.array.text_gravity);
        mRadioLayout.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mTextView.setGravity(gravitys[newPosition]);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
    }
}
