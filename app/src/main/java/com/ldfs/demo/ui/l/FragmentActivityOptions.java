package com.ldfs.demo.ui.l;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ldfs.demo.MoreActivity;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.util.ViewInject;

public class FragmentActivityOptions extends Fragment implements View.OnClickListener {
    @ID(id = R.id.ll_container, childClick = true)
    private LinearLayout mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_options, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_imamge1:
                break;
            case R.id.iv_imamge2:
                break;
            case R.id.iv_imamge3:
                break;
        }
        //让新的Activity从一个小的范围扩大到全屏
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view, //The View that the new activity is animating from
                        (int) view.getWidth() / 2, (int) view.getHeight() / 2, //拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        startNewAcitivity(options);
    }

    private void startNewAcitivity(ActivityOptionsCompat options) {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        intent.putExtra(MoreActivity.CLASS_TAG, FragmentOptionImage.class.getName());
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
