package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.translation.ViewTranslation;
import com.ldfs.demo.util.ViewInject;

public class FragmentDistanceLayout extends Fragment implements View.OnClickListener {
    @ID(id = R.id.container, click = true)
    private ViewGroup mLayout;
    private boolean isReverse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distance_layout, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        ViewTranslation.startTransilate(mLayout, ViewTranslation.ALL, ViewTranslation.NEAR, 2000, isReverse);
        isReverse = !isReverse;
    }
}
