package com.ldfs.demo.ui.customview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.RadioGridLayout;
import com.ldfs.demo.widget.calendar.CalendarView;

import java.util.ArrayList;

public class FragmentCalendarView extends Fragment {
    @ID(id = R.id.cv_calendar_view)
    private CalendarView mCalendarView;
    @ID(id = R.id.tv_month_info)
    private TextView monthInfo;

    @ID(id = R.id.rl_calendar_mode)
    private RadioGridLayout mCalendarMode;
    @ID(id = R.id.fl_change_data)
    private RadioGridLayout mChangeData;
    @ID(id = R.id.fl_sort_mode)
    private RadioGridLayout mSortMode;
    @ID(id = R.id.fl_click_mode)
    private RadioGridLayout mClickMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        monthInfo.setText(mCalendarView.getCalendarString());
        mCalendarMode.addDefaultTextViewByResource(R.array.calendar_mode);
        mChangeData.addDefaultTextViewByResource(R.array.calendar_change_data);
        mSortMode.addDefaultTextViewByResource(R.array.calendar_sort_mode);
        mClickMode.addDefaultTextViewByResource(R.array.click_mode);


        mCalendarMode.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mCalendarView.setMode(newPosition);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
        mChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mChangeData.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                int mode = mCalendarView.getMode();
                if (CalendarView.WEEK_MODE == mode) {
                    mCalendarView.setWeekOffset(0 == newPosition ? -1 : 1);
                } else if (CalendarView.MONTH_MODE == mode) {
                    mCalendarView.setMonthOffset(0 == newPosition ? -1 : 1);
                }
                monthInfo.setText(mCalendarView.getCalendarString());
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
        mSortMode.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mCalendarView.setDaySortMode(newPosition);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
        mClickMode.setCheckedListener(new RadioGridLayout.OnCheckListener() {
            @Override
            public void checked(View v, int newPosition, int oldPosition) {
                mCalendarView.setClickMode(newPosition);
            }

            @Override
            public void multipleChoice(View v, ArrayList<Integer> mChoicePositions) {
            }
        });
    }

}
