package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.item.Rate;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.FrameView;
import com.ldfs.demo.widget.RadioGridLayout;

import java.util.ArrayList;

/**
 * View 显示状态桢
 */
@RateInfo(rate= Rate.COMPLETE,beteInfo = R.string.frame_bete_info)
public class FragmentFrameView extends Fragment {
    @ID(id = R.id.rl_radio)
    private RadioGridLayout mRadio;
    @ID(id = R.id.fv_view1)
    private FrameView mFrame1;
    @ID(id = R.id.fv_view2)
    private FrameView mFrame2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_view, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRadio.addDefaultTextViewByResource(R.array.view_frames);
        Runnable action=new Runnable() {
            @Override
            public void run() {
                App.toast(R.string.repeat_load);
            }
        };
        mFrame1.setRepeatRunnable(action);
        mFrame2.setRepeatRunnable(action);
        mRadio.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                switch (newPosition) {
                    case 0:
                        mFrame1.setContainerShown(true);
                        mFrame2.setContainerShown(true);
                        break;
                    case 1:
                        mFrame1.setProgressShown(true);
                        mFrame2.setProgressShown(true);
                        break;
                    case 2:
                        mFrame1.setEmptyShown(true);
                        mFrame2.setEmptyShown(true);
                        break;
                    case 3:
                        mFrame1.setErrorShown(true);
                        mFrame2.setErrorShown(true);
                        break;
                    case 4:
                        mFrame1.setRepeatShown(true);
                        mFrame2.setRepeatShown(true);
                        break;
                }
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
    }
}
