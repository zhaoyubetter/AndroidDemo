package com.ldfs.demo.ui.l;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ldfs.demo.R;
import com.ldfs.demo.adapter.ImageRecyclerAdapter;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.listener.OnRecyclerItemClickListener;
import com.ldfs.demo.util.ViewInject;

/**
 * 从图片中提取突出的颜色
 * 
 * @author momo
 * @Date 2015/3/5
 * 
 */
public class FragmentPalette extends Fragment {
	@ID(id = R.id.view1)
	private View mView1;
	@ID(id = R.id.view2)
	private View mView2;
	@ID(id = R.id.view3)
	private View mView3;
	@ID(id = R.id.view4)
	private View mView4;
	@ID(id = R.id.view5)
	private View mView5;

	@ID(id = R.id.rv_recycler_view)
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_palette, container,
				false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		final int[] res = new int[] { R.drawable.abc_ab_share_pack_holo_dark,
				R.drawable.abc_btn_radio_to_on_mtrl_000,
				R.drawable.abc_btn_radio_to_on_mtrl_015,
				R.drawable.abc_btn_switch_to_on_mtrl_00012, R.drawable.qq,
				R.drawable.qq_item, R.drawable.checked1, R.drawable.checked2,
				R.drawable.car, R.drawable.three01, R.drawable.three02,
				R.drawable.three03, R.drawable.three04 };
		ImageRecyclerAdapter adapter = new ImageRecyclerAdapter(getActivity(),
				res);
		mRecyclerView.setAdapter(adapter);
		adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
			@Override
			public void onItemClick(View convertView, int position) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						res[position]);
				Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // Vibrant （有活力）
                        // Vibrant dark（有活力 暗色）
                        // Vibrant light（有活力 亮色）
                        // Muted （柔和）
                        // Muted dark（柔和 暗色）
                        // Muted light（柔和 亮色）
                        int vibrantColor = palette.getVibrantColor(Color.RED);
                        int darkVibrantColor = palette
                                .getDarkVibrantColor(Color.RED);
                        int mutedColor = palette.getMutedColor(Color.RED);
                        int darkMutedColor = palette
                                .getDarkMutedColor(Color.RED);
                        int lightMutedColor = palette
                                .getLightMutedColor(Color.RED);
                        mView1.setBackgroundColor(vibrantColor);
                        mView2.setBackgroundColor(darkVibrantColor);
                        mView3.setBackgroundColor(mutedColor);
                        mView4.setBackgroundColor(darkMutedColor);
                        mView5.setBackgroundColor(lightMutedColor);

                    }
                });
			}
		});
	}
}
