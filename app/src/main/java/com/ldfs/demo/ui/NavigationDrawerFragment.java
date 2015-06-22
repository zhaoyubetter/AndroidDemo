package com.ldfs.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.bean.MyItem;
import com.ldfs.demo.db.DbHelper;
import com.ldfs.demo.db.DbTable;
import com.ldfs.demo.preference.ConfigName;
import com.ldfs.demo.preference.PrefernceUtils;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.SwitchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {
	@ID(id = R.id.lv_menu_list)
	private ListView mDrawerListView;
	private List<MyItem> mItems;
	@ID(id=R.id.sv_switch)
	private SwitchView mSwitchView;
	private NavigationDrawerCallbacks mCallbacks;

	public NavigationDrawerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		ViewInject.init(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MyItem myItem = new MyItem();
		mItems = DbHelper.getDatas(DbTable._URI, myItem, DbTable._SELECTION, "parent=?", new String[] { String.valueOf(-1) }, "id ASC");
		if (null != mItems) {
			List<String> titles = new ArrayList<String>(mItems.size());
			for (MyItem item : mItems) {
				titles.add(item.name);
			}
			mDrawerListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, titles));
			mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					selectItem(position);
				}
			});
		}
		mSwitchView.setChecked(!PrefernceUtils.getBoolean(ConfigName.BATE_INFO));
		mSwitchView.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(SwitchView switchView, boolean isChecked) {
				PrefernceUtils.setBoolean(ConfigName.BATE_INFO,isChecked);
			}
		});

	}

	private void selectItem(int position) {
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		mCallbacks.onItemClick(position, mItems.get(position).name);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.view_content, FragmentFunctionList.newInstance(mItems.get(position).id)).commit();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	public interface NavigationDrawerCallbacks {
		void onItemClick(int position, String title);
	}
}
