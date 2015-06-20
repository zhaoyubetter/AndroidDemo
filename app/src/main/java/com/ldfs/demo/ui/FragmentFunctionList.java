package com.ldfs.demo.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.database.CursorJoiner.Result;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.adapter.ItemAdapter;
import com.ldfs.demo.bean.MyItem;
import com.ldfs.demo.db.DbHelper;
import com.ldfs.demo.db.DbTable;
import com.ldfs.demo.util.FragmentUtils;
import com.ldfs.demo.util.ResUtils;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p/>
 * interface.
 */
public class FragmentFunctionList extends Fragment implements AbsListView.OnItemClickListener {

	private static final String ARG_PARAM1 = "position";
	private TitleChangeCallbacks mTitleChangeCallbacks;

	private AbsListView mListView;
	private ItemAdapter mAdapter;
	private int mPosition;

	public static FragmentFunctionList newInstance(int param1) {
		FragmentFunctionList fragment = new FragmentFunctionList();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	public FragmentFunctionList() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mPosition = getArguments().getInt(ARG_PARAM1);
		}
		MyItem myItem = new MyItem();
		ArrayList<MyItem> items = DbHelper.getDatas(DbTable._URI, myItem, DbTable._SELECTION, "parent=?", new String[] { String.valueOf(mPosition) }, null);
		if (null != items) {
			mAdapter = new ItemAdapter(getActivity(), items);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_function, container, false);
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MyItem item = mAdapter.getItem(position);
		mTitleChangeCallbacks.onTitleChange(item.name);
		if (-1 != item.id) {
			FragmentUtils.addFragment(getActivity(), FragmentFunctionList.newInstance(item.id), R.id.view_content, true);
		} else {
			try {
				Class<?> clazz = Class.forName(item.clazz);
				Object instance = clazz.newInstance();
				if (null != instance && instance instanceof Fragment) {
					Fragment fragment = (Fragment) instance;
					FragmentUtils.addFragment(getActivity(), fragment, TextUtils.isEmpty(item.container) ? R.id.fragment_container : ResUtils.id(item.container), true);
				}
			} catch (Exception e) {
				App.toast(R.string.open_option_fail);
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mTitleChangeCallbacks = (TitleChangeCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();
		if (emptyView instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
	}

	@Override
	public void onDetach() {
		mTitleChangeCallbacks.onDetach();
		super.onDetach();
	}

	public interface TitleChangeCallbacks {
		void onTitleChange(String title);

		void onDetach();
	}
}
