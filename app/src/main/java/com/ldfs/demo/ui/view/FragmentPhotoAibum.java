package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.PhotoAlbumAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.bean.PhotoItem;
import com.ldfs.demo.util.AlbumUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.TitleBar;

import java.util.ArrayList;

/**
 * 自定义图库
 *
 * @author cz
 * @date 2015/6/16
 */
public class FragmentPhotoAibum extends Fragment {

    @ID(id = R.id.titlebar_container)
    private TitleBar mTitleBar;
    @ID(id = R.id.rv_recycler_view)
    private RecyclerView mRecyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_aibum, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mTitleBar.setPageTitleVisible(true);
        mTitleBar.setTitle("全部图片");
        //标题内容
        ArrayList<PhotoItem> infos = AlbumUtils.getPhotoInfos(getActivity());
        //所有图片内容
        ArrayList<PhotoItem> items = AlbumUtils.getPhotoItems(getActivity(), null);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(new PhotoAlbumAdapter(getActivity(),items));
    }


}
