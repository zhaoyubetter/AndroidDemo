package com.ldfs.demo.ui.customview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.CenterTextView;
import com.ldfs.demo.widget.RadioGridLayout;

import java.util.ArrayList;

/**
 * textview drawable与文字居中
 */
public class FragmentCenterTextView extends Fragment {
    @ID(id = R.id.cv_text_view)
    private CenterTextView mTextView;
    @ID(id = R.id.rl_gravity)
    private RadioGridLayout mRadioLayout;
    @ID(id = R.id.tv_padding)
    private TextView mPadding;
    @ID(id = R.id.sb_padding)
    private SeekBar mSeekPadding;
    private String[] mGravitys;
    private int mGravity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center_textview, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGravitys = App.getStringArray(R.array.center_text_gravity);
        mRadioLayout.addDefaultTextViewByResource(R.array.center_text_gravity);
        mRadioLayout.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                switch (mGravity) {
                    case 1:
                        mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher, 0, 0, 0);
                        break;
                    case 2:
                        mTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_launcher, 0, 0);
                        break;
                    case 3:
                        mTextView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_launcher, 0);
                        break;
                    case 4:
                        mTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_launcher);
                        break;
                }
                mTextView.setCenterGravity(newPosition);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });

        mSeekPadding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int padding = UnitUtils.dip2px(getActivity(), progress);
                mPadding.setText(App.getStr(R.string.padding_value, progress));
                TextFontUtils.setWordColorAndTypedFace(mPadding, Color.RED, Typeface.BOLD, padding);
                switch (mGravity) {
                    case 1:
                        mTextView.setPadding(padding,0,0,0);
                        break;
                    case 2:
                        mTextView.setPadding(0,padding,0,0);
                        break;
                    case 3:
                        mTextView.setPadding(0,0,padding,0);
                        break;
                    case 4:
                        mTextView.setPadding(0,0,0,padding);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }
}
