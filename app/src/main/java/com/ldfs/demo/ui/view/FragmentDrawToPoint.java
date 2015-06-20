package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.DrawLineView;
import com.ldfs.demo.widget.RadioGridLayout;

import java.util.ArrayList;

/**
 * 控件绘制点
 */
public class FragmentDrawToPoint extends Fragment {
    @ID(id = R.id.dv_draw)
    private DrawLineView mDrawLine;
    @ID(id = R.id.rl_radio)
    private RadioGridLayout mRadio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw_to_point, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRadio.addDefaultTextViewByResource(R.array.draw_point);
        mRadio.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mDrawLine.setDrawType(newPosition);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
    }
}
