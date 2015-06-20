package com.ldfs.demo.widget;

/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ActionProvider;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.support.v7.internal.widget.ActivityChooserModel.ActivityChooserModelClient;
import android.support.v7.internal.widget.ActivityChooserModel.ActivityResolveInfo;
import android.support.v7.internal.widget.ActivityChooserModel.ActivitySorter;
import android.support.v7.internal.widget.ActivityChooserModel.HistoricalRecord;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ldfs.demo.R;
import com.ldfs.demo.dialog.ShareDialog;

public class ActivityChooserView extends ViewGroup implements ActivityChooserModelClient {

	private final ActivityChooserViewAdapter mAdapter;

	private final Callbacks mCallbacks;

	private Context mContext;

	/**
	 * The content of this view.
	 */
	private final LinearLayout mActivityChooserContent;

	/**
	 * The expand activities action button;
	 */
	private final FrameLayout mExpandActivityOverflowButton;

	/**
	 * The image for the expand activities action button;
	 */
	private final TextView mExpandActivityOverflowButtonText;

	/**
	 * The default activities action button;
	 */
	private final FrameLayout mDefaultActivityButton;

	/**
	 * The image for the default activities action button;
	 */
	private final ImageView mDefaultActivityButtonImage;

	private final View divier;// 分隔线

	private boolean showLoadMore;

	/**
	 * The ActionProvider hosting this view, if applicable.
	 */
	ActionProvider mProvider;

	private ShareDialog shareDialog;

	/**
	 * Observer for the model data.
	 */
	private final DataSetObserver mModelDataSetOberver = new DataSetObserver() {

		@Override
		public void onChanged() {
			super.onChanged();
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
			mAdapter.notifyDataSetInvalidated();
		}
	};

	private final OnGlobalLayoutListener mOnGlobalLayoutListener = new OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			if (isShowingPopup()) {
				if (!isShown()) {
					getShareDialog().dismiss();
				} else {
					getShareDialog().show();
					if (mProvider != null) {
						mProvider.subUiVisibilityChanged(true);
					}
				}
			}
		}
	};

	public ShareDialog getShareDialog() {
		if (null == shareDialog) {
			shareDialog = new ShareDialog(mContext);
			shareDialog.setShareAdapter(mAdapter);
			shareDialog.setOnItemClick(mCallbacks);
			shareDialog.setMoreShareListener(mCallbacks);
		}
		shareDialog.setMoreVisible(showLoadMore);
		return shareDialog;
	}

	/**
	 * Flag whether a default activity currently being selected.
	 */
	private boolean mIsSelectingDefaultActivity;

	/**
	 * The count of activities in the popup.
	 */
	private int mInitialActivityCount = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_DEFAULT;

	/**
	 * Flag whether this view is attached to a window.
	 */
	private boolean mIsAttachedToWindow;

	/**
	 * String resource for formatting content description of the default target.
	 */
	private int mDefaultActionButtonContentDescription;

	private Intent shareIntent;

	private ShareListener listener;

	private boolean showDefault;// 是否显示默认分享图标

	private int backGroundRes;// 显示背景

	/**
	 * Create a new instance.
	 * 
	 * @param context
	 *            The application environment.
	 */
	public ActivityChooserView(Context context) {
		this(context, null);
	}

	/**
	 * Create a new instance.
	 * 
	 * @param context
	 *            The application environment.
	 * @param attrs
	 *            A collection of attributes.
	 */
	public ActivityChooserView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Create a new instance.
	 * 
	 * @param context
	 *            The application environment.
	 * @param attrs
	 *            A collection of attributes.
	 * @param defStyle
	 *            The default style to apply to this view.
	 */
	public ActivityChooserView(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.activity_chooser_view, this, true);

		mCallbacks = new Callbacks();

		mActivityChooserContent = (LinearLayout) findViewById(R.id.activity_chooser_view_content);
		mDefaultActivityButton = (FrameLayout) findViewById(R.id.default_activity_button);
		mDefaultActivityButton.setOnClickListener(mCallbacks);
		mDefaultActivityButton.setOnLongClickListener(mCallbacks);
		mDefaultActivityButtonImage = (ImageView) mDefaultActivityButton.findViewById(R.id.image);
		divier = mActivityChooserContent.findViewById(R.id.view_divier);
		divier.setVisibility(View.GONE);
		mDefaultActivityButtonImage.setVisibility(View.GONE);

		mExpandActivityOverflowButton = (FrameLayout) findViewById(R.id.expand_activities_button);
		mExpandActivityOverflowButton.setOnClickListener(mCallbacks);
		mExpandActivityOverflowButtonText = (TextView) mExpandActivityOverflowButton.findViewById(R.id.tv_to_share);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActivityChooserView, defStyle, 0);

		mInitialActivityCount = a.getInt(R.styleable.ActivityChooserView_initialActivityCount, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_DEFAULT);
		backGroundRes = a.getResourceId(R.styleable.ActivityChooserView_chooser_background, R.drawable.setting_bg);
		showDefault = a.getBoolean(R.styleable.ActivityChooserView_chooser_show_default, true);
		int resourceId = a.getResourceId(R.styleable.ActivityChooserView_chooser_src, -1);
		if (-1 != resourceId) {
			mExpandActivityOverflowButtonText.setText(null);
			mExpandActivityOverflowButtonText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resourceId, 0);
		}
		a.recycle();

		mAdapter = new ActivityChooserViewAdapter();
		mAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				updateAppearance();
			}
		});

		ShareActionProvider provider = new ShareActionProvider(context);
		provider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		provider.setShareIntent(createShareIntent());
		ActivityChooserModel activityChooserModel = ActivityChooserModel.get(context, ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		activityChooserModel.setActivitySorter(new ActivitySorter() {
			private final Map<String, ActivityResolveInfo> mPackageNameToActivityMap = new HashMap<String, ActivityResolveInfo>();

			@Override
			public void sort(Intent intent, List<ActivityResolveInfo> activities, List<HistoricalRecord> historicalRecords) {
				Map<String, ActivityResolveInfo> activityNameToActivityMap = mPackageNameToActivityMap;
				activityNameToActivityMap.clear();

				final int activityCount = activities.size();
				for (int i = 0; i < activityCount; i++) {
					ActivityResolveInfo activity = activities.get(i);
					activity.weight = 0.0f;
					String packageName = activity.resolveInfo.activityInfo.name;
					activityNameToActivityMap.put(packageName, activity);
				}
				final int lastShareIndex = historicalRecords.size() - 1;
				ActivityResolveInfo activity = null;
				for (int i = lastShareIndex; i >= 0; i--) {
					HistoricalRecord historicalRecord = historicalRecords.get(i);
					String activityName = historicalRecord.activity.getClassName();
					activity = activityNameToActivityMap.get(activityName);
					// 使第一个位置activityInfo 权重最高
					if (null != activity) {
						activity.weight += 1;
						break;
					}
				}
				Collections.sort(activities);
			}
		});
		setActivityChooserModel(activityChooserModel);
	}

	/**
	 * 设置是否启用点击事件
	 * 
	 * @param enable
	 */
	public void setClickEnable(boolean enable) {
		if (null != mCallbacks) {
			mDefaultActivityButton.setOnClickListener(enable ? mCallbacks : null);
			mDefaultActivityButton.setOnLongClickListener(enable ? mCallbacks : null);
			mExpandActivityOverflowButton.setOnClickListener(enable ? mCallbacks : null);
		}
	}

	private Intent createShareIntent() {
		shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		return shareIntent;
	}

	/**
	 * 设置分享内容
	 * 
	 * @param value
	 */
	public void setShareDate(String value) {
		if (null != shareIntent && !TextUtils.isEmpty(value)) {
			shareIntent.putExtra(Intent.EXTRA_TEXT, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setActivityChooserModel(ActivityChooserModel dataModel) {
		mAdapter.setDataModel(dataModel);
		if (isShowingPopup()) {
			dismissPopup();
			showPopup();
		}
	}

	/**
	 * Sets the background for the button that expands the activity overflow
	 * list.
	 * 
	 * <strong>Note:</strong> Clients would like to set this drawable as a clue
	 * about the action the chosen activity will perform. For example, if a
	 * share activity is to be chosen the drawable should give a clue that
	 * sharing is to be performed.
	 * 
	 * @param drawable
	 *            The drawable.
	 */

	/**
	 * Sets the content description for the button that expands the activity
	 * overflow list.
	 * 
	 * description as a clue about the action performed by the button. For
	 * example, if a share activity is to be chosen the content description
	 * should be something like "Share with".
	 * 
	 * @param resourceId
	 *            The content description resource id.
	 */
	public void setExpandActivityOverflowButtonContentDescription(int resourceId) {
		CharSequence contentDescription = mContext.getString(resourceId);
		mExpandActivityOverflowButtonText.setContentDescription(contentDescription);
	}

	/**
	 * Shows the popup window with activities.
	 * 
	 * @return True if the popup was shown, false if already showing.
	 */
	public boolean showPopup() {
		if (isShowingPopup() || !mIsAttachedToWindow) {
			return false;
		}
		mIsSelectingDefaultActivity = false;
		showPopupUnchecked(mInitialActivityCount);
		return true;
	}

	/**
	 * Shows the popup no matter if it was already showing.
	 * 
	 * @param maxActivityCount
	 *            The max number of activities to display.
	 */
	private void showPopupUnchecked(int maxActivityCount) {
		if (mAdapter.getDataModel() == null) {
			throw new IllegalStateException("No data model. Did you call #setDataModel?");
		}

		getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);

		final boolean defaultActivityButtonShown = mDefaultActivityButton.getVisibility() == VISIBLE;

		final int activityCount = mAdapter.getActivityCount();
		final int maxActivityCountOffset = defaultActivityButtonShown ? 1 : 0;
		if (maxActivityCount != ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED && activityCount > maxActivityCount + maxActivityCountOffset) {
			mAdapter.setMaxActivityCount(maxActivityCount);
		} else {
			mAdapter.setMaxActivityCount(maxActivityCount);
		}

		Dialog shareDialog = getShareDialog();
		if (!shareDialog.isShowing()) {
			if (mIsSelectingDefaultActivity || !defaultActivityButtonShown) {
				mAdapter.setShowDefaultActivity(true, defaultActivityButtonShown);
			} else {
				mAdapter.setShowDefaultActivity(false, false);
			}
			shareDialog.show();
			if (mProvider != null) {
				mProvider.subUiVisibilityChanged(true);
			}
		}
	}

	/**
	 * Dismisses the popup window with activities.
	 * 
	 * @return True if dismissed, false if already dismissed.
	 */
	public boolean dismissPopup() {
		if (isShowingPopup()) {
			getShareDialog().dismiss();
			ViewTreeObserver viewTreeObserver = getViewTreeObserver();
			if (viewTreeObserver.isAlive()) {
				viewTreeObserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
			}
		}
		return true;
	}

	/**
	 * Gets whether the popup window with activities is shown.
	 * 
	 * @return True if the popup is shown.
	 */
	public boolean isShowingPopup() {
		return getShareDialog().isShowing();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ActivityChooserModel dataModel = mAdapter.getDataModel();
		if (dataModel != null) {
			dataModel.registerObserver(mModelDataSetOberver);
		}
		mIsAttachedToWindow = true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		ActivityChooserModel dataModel = mAdapter.getDataModel();
		if (dataModel != null) {
			dataModel.unregisterObserver(mModelDataSetOberver);
		}
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
		}
		mIsAttachedToWindow = false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		View child = mActivityChooserContent;
		// If the default action is not visible we want to be as tall as the
		// ActionBar so if this widget is used in the latter it will look as
		// a normal action button.
		if (mDefaultActivityButton.getVisibility() != VISIBLE) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
		}
		measureChild(child, widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		mActivityChooserContent.layout(0, 0, right - left, bottom - top);
		if (getShareDialog().isShowing()) {
			showPopupUnchecked(mAdapter.getMaxActivityCount());
		} else {
			dismissPopup();
		}
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
	}

	public ActivityChooserModel getDataModel() {
		return mAdapter.getDataModel();
	}

	/**
	 * Sets the initial count of items shown in the activities popup i.e. the
	 * items before the popup is expanded. This is an upper bound since it is
	 * not guaranteed that such number of intent handlers exist.
	 * 
	 * @param itemCount
	 *            The initial popup item count.
	 */
	public void setInitialActivityCount(int itemCount) {
		mInitialActivityCount = itemCount;
	}

	/**
	 * Sets a content description of the default action button. This resource
	 * should be a string taking one formatting argument and will be used for
	 * formatting the content description of the button dynamically as the
	 * default target changes. For example, a resource pointing to the string
	 * "share with %1$s" will result in a content description
	 * "share with Bluetooth" for the Bluetooth activity.
	 * 
	 * @param resourceId
	 *            The resource id.
	 */
	public void setDefaultActionButtonContentDescription(int resourceId) {
		mDefaultActionButtonContentDescription = resourceId;
	}

	/**
	 * Updates the buttons state.
	 */
	private void updateAppearance() {
		// Expand overflow button.
		if (mAdapter.getCount() > 0) {
			mExpandActivityOverflowButton.setEnabled(true);
		} else {
			mExpandActivityOverflowButton.setEnabled(false);
		}
		// Default activity button.
		final int activityCount = mAdapter.getActivityCount();
		final int historySize = mAdapter.getHistorySize();
		if (activityCount > 0 && historySize > 0) {
			mDefaultActivityButton.setVisibility(VISIBLE);
			ResolveInfo activity = mAdapter.getDefaultActivity();
			PackageManager packageManager = mContext.getPackageManager();
			Drawable loadIcon = activity.loadIcon(packageManager);
			if (null != loadIcon && showDefault) {
				divier.setVisibility(View.VISIBLE);
				mDefaultActivityButtonImage.setVisibility(View.VISIBLE);
				mDefaultActivityButtonImage.setImageDrawable(loadIcon);
			} else {
				divier.setVisibility(View.GONE);
				mDefaultActivityButtonImage.setVisibility(View.GONE);
			}
			if (mDefaultActionButtonContentDescription != 0) {
				CharSequence label = activity.loadLabel(packageManager);
				String contentDescription = mContext.getString(mDefaultActionButtonContentDescription, label);
				mDefaultActivityButton.setContentDescription(contentDescription);
			}
		}
		// Activity chooser content.
		if (mDefaultActivityButton.getVisibility() == VISIBLE) {
			mActivityChooserContent.setBackgroundResource(backGroundRes);
		} else {
			mActivityChooserContent.setBackgroundDrawable(null);
		}
	}

	/**
	 * Interface implementation to avoid publishing them in the APIs.
	 */
	private class Callbacks implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, PopupWindow.OnDismissListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			dismissPopup();
			if (null != listener) {
				if (0 == position || (null != mAdapter && position == mAdapter.getCount() - 1)) {
					listener.localtionOption(position);
				} else {
					Intent launchIntent = mAdapter.getDataModel().chooseActivity(position - 1);
					if (launchIntent != null) {
						listener.toShare(launchIntent);
						mContext.startActivity(launchIntent);
					}
				}
			}
		}

		// View.OnClickListener
		public void onClick(View view) {
			if (view == mDefaultActivityButton) {
				dismissPopup();
				ResolveInfo defaultActivity = mAdapter.getDefaultActivity();
				final int index = mAdapter.getDataModel().getActivityIndex(defaultActivity);
				Intent launchIntent = mAdapter.getDataModel().chooseActivity(index);
				if (launchIntent != null) {
					if (null != listener) {
						listener.toShare(launchIntent);
					}
					mContext.startActivity(launchIntent);
				}
			} else if (view == mExpandActivityOverflowButton && !showLoadMore) {
				mIsSelectingDefaultActivity = false;
				showPopupUnchecked(mInitialActivityCount);
			} else if (R.id.tv_more == view.getId() || showLoadMore) {
				showLoadMore = true;
				view.setVisibility(R.id.tv_more == view.getId() ? View.GONE : View.VISIBLE);
				showPopupUnchecked(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
			}
		}

		// OnLongClickListener#onLongClick
		@Override
		public boolean onLongClick(View view) {
			if (view == mExpandActivityOverflowButtonText) {
				if (mAdapter.getCount() > 0) {
					mIsSelectingDefaultActivity = true;
					showPopupUnchecked(mInitialActivityCount);
				}
			}
			return true;
		}

		// PopUpWindow.OnDismissListener#onDismiss
		public void onDismiss() {
			if (mProvider != null) {
				mProvider.subUiVisibilityChanged(false);
			}
		}

	}

	/**
	 * Adapter for backing the list of activities shown in the popup.
	 */
	public class ActivityChooserViewAdapter extends BaseAdapter {

		private static final int OPTION_ITEM = 2;

		public static final int MAX_ACTIVITY_COUNT_UNLIMITED = Integer.MAX_VALUE;

		public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;

		private ActivityChooserModel mDataModel;

		private int mMaxActivityCount = MAX_ACTIVITY_COUNT_DEFAULT;

		private boolean mShowDefaultActivity;

		private boolean mHighlightDefaultActivity;

		private String[] items;
		private int[] iconRes;

		public ActivityChooserViewAdapter() {
			super();
			items = mContext.getResources().getStringArray(R.array.share_items);
			iconRes = new int[] { R.drawable.ss_ic_save, R.drawable.ss_ic_copy_link };
		}

		public void setDataModel(ActivityChooserModel dataModel) {
			ActivityChooserModel oldDataModel = mAdapter.getDataModel();
			if (oldDataModel != null && isShown()) {
				oldDataModel.unregisterObserver(mModelDataSetOberver);
			}
			mDataModel = dataModel;
			if (dataModel != null && isShown()) {
				dataModel.registerObserver(mModelDataSetOberver);
			}
			notifyDataSetChanged();
		}

		public int getCount() {
			int activityCount = mDataModel.getActivityCount();
			int count = Math.min(activityCount, mMaxActivityCount);
			return count + OPTION_ITEM;
		}

		public Object getItem(int position) {
			return mDataModel.getActivity(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ShareHolder holder = null;
			if (null == convertView) {
				holder = new ShareHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.share_item, parent, false);
				holder.icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
				holder.label = (TextView) convertView.findViewById(R.id.tv_app_info);
				convertView.setTag(holder);
			}
			holder = (ShareHolder) convertView.getTag();
			PackageManager packageManager = mContext.getPackageManager();
			boolean isOptionItem = (0 == position || getCount() - 1 == position);
			if (isOptionItem) {
				int optionPosition = (0 == position) ? position : 1;
				holder.icon.setImageDrawable(mContext.getResources().getDrawable(iconRes[optionPosition]));
				holder.label.setText(items[optionPosition]);
			} else {
				ResolveInfo activity = (ResolveInfo) getItem(position - 1);
				holder.icon.setImageDrawable(activity.loadIcon(packageManager));
				holder.label.setText(activity.loadLabel(packageManager));
			}
			return convertView;
		}

		public int measureContentWidth() {
			final int oldMaxActivityCount = mMaxActivityCount;
			mMaxActivityCount = MAX_ACTIVITY_COUNT_UNLIMITED;

			int contentWidth = 0;
			View itemView = null;

			final int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			final int count = getCount();

			for (int i = 0; i < count; i++) {
				itemView = getView(i, itemView, null);
				itemView.measure(widthMeasureSpec, heightMeasureSpec);
				contentWidth = Math.max(contentWidth, itemView.getMeasuredWidth());
			}

			mMaxActivityCount = oldMaxActivityCount;

			return contentWidth;
		}

		public void setMaxActivityCount(int maxActivityCount) {
			if (mMaxActivityCount != maxActivityCount) {
				mMaxActivityCount = maxActivityCount;
				notifyDataSetChanged();
			}
		}

		public ResolveInfo getDefaultActivity() {
			return mDataModel.getDefaultActivity();
		}

		public int getActivityCount() {
			return mDataModel.getActivityCount();
		}

		public int getHistorySize() {
			return mDataModel.getHistorySize();
		}

		public int getMaxActivityCount() {
			return mMaxActivityCount;
		}

		public ActivityChooserModel getDataModel() {
			return mDataModel;
		}

		public void setShowDefaultActivity(boolean showDefaultActivity, boolean highlightDefaultActivity) {
			if (mShowDefaultActivity != showDefaultActivity || mHighlightDefaultActivity != highlightDefaultActivity) {
				mShowDefaultActivity = showDefaultActivity;
				mHighlightDefaultActivity = highlightDefaultActivity;
				notifyDataSetChanged();
			}
		}

		public boolean getShowDefaultActivity() {
			return mShowDefaultActivity;
		}

	}

	static class ShareHolder {
		ImageView icon;
		TextView label;
	}

	public void setShareListener(ShareListener listener) {
		this.listener = listener;
	}

	public interface ShareListener {
		void toShare(Intent shareIntent);

		void localtionOption(int position);
	}
}
