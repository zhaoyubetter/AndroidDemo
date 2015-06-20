package com.ldfs.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ldfs.demo.R;
import com.ldfs.demo.widget.ActivityChooserView.ActivityChooserViewAdapter;

/**
 * 分享弹出框
 * 
 * @author momo
 * @Date 2014/9/18
 */
public class ShareDialog extends Dialog {
	private GridView gridView;
	private View moreView;

	public ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public ShareDialog(Context context, int theme) {
		super(context, theme);
	}

	public ShareDialog(Context context) {
		super(context);
		setTitle(R.string.share_title);
		setContentView(getContentView(context));
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private View getContentView(final Context context) {
		View contentView = View.inflate(context, R.layout.dialog_share, null);
		gridView = (GridView) contentView.findViewById(R.id.gv_share_list);
		moreView = contentView.findViewById(R.id.tv_more);
		return contentView;
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	public void setShareAdapter(ActivityChooserViewAdapter mAdapter) {
		if (null != gridView && null != mAdapter) {
			gridView.setAdapter(mAdapter);
		}
	}

	public void setOnItemClick(OnItemClickListener listener) {
		if (null != gridView) {
			gridView.setOnItemClickListener(listener);
		}
	}

	public void setMoreShareListener(android.view.View.OnClickListener listener) {
		if (null != moreView) {
			moreView.setOnClickListener(listener);
		}
	}

	public void setMoreVisible(boolean showLoadMore) {
		if (null != moreView) {
			moreView.setVisibility(showLoadMore ? View.GONE : View.VISIBLE);
		}
	}
}
