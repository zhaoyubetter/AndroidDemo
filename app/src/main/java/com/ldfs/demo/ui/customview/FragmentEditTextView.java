package com.ldfs.demo.ui.customview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.TextFontUtils;
import com.ldfs.demo.util.UnitUtils;
import com.ldfs.demo.util.ViewInject;

/**
 * 编辑文字演示控件
 */
@RateInfo(rate= Rate.CODING,beteInfo = R.string.edit_text_bate_info)
public class FragmentEditTextView extends Fragment implements SeekBar.OnSeekBarChangeListener {
    @ID(id = R.id.et_editor)
    private EditText mEditor;

    @ID(id = R.id.tv_font_size)
    private TextView mFontSize;
    @ID(id = R.id.sb_font_size)
    private SeekBar mSeekSize;

    @ID(id = R.id.tv_left_padding)
    private TextView mLeftPadding;
    @ID(id = R.id.sb_left_padding)
    private SeekBar mSeekLeft;

    @ID(id = R.id.tv_top_padding)
    private TextView mTopPadding;
    @ID(id = R.id.sb_top_padding)
    private SeekBar mSeekTop;

    private float mTextSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_textview, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSeekSize.setOnSeekBarChangeListener(this);
        mSeekLeft.setOnSeekBarChangeListener(this);
        mSeekTop.setOnSeekBarChangeListener(this);

        mTextSize = mEditor.getTextSize();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        int id = seekBar.getId();
        float fraction = progress * 1.0f / seekBar.getMax();
        int padding = (int) (UnitUtils.dip2px(App.getAppContext(), 10) * fraction);
        switch (id) {
            case R.id.sb_font_size:
                float textFontSize = 20 + 100 * fraction;
                mEditor.setTextSize(textFontSize);
                mFontSize.setText(App.getStr(R.string.padding_value, textFontSize));
                TextFontUtils.setWordColorAndTypedFace(mFontSize, Color.GREEN, Typeface.BOLD, textFontSize);
                break;
            case R.id.sb_left_padding:
                mEditor.setPadding(padding, 0, padding, 0);
                mLeftPadding.setText(App.getStr(R.string.padding_value, padding));
                TextFontUtils.setWordColorAndTypedFace(mLeftPadding, Color.GREEN, Typeface.BOLD, padding);
                break;
            case R.id.sb_top_padding:
                mEditor.setPadding(0, padding, 0, padding);
                mTopPadding.setText(App.getStr(R.string.padding_value, padding));
                TextFontUtils.setWordColorAndTypedFace(mTopPadding, Color.GREEN, Typeface.BOLD, padding);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
