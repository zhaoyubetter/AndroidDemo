package com.ldfs.demo.widget;

import static com.nineoldandroids.view.ViewHelper.setAlpha;
import static com.nineoldandroids.view.ViewHelper.setTranslationX;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.SortedSet;
import java.util.TreeSet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 点击划动删除的ListView
 * 
 * @author momo
 * @Date 2015/1/30
 */
public final class DismissListView implements OnItemClickListener {

	private static final long ANIMATION_TIME = 300;

	private SortedSet<PendingDismissData> mPendingDismisses = new TreeSet<PendingDismissData>();
	private OnItemClickListener mItemClickListener;
	private boolean mSwipeDisabled;

	private OnDismissListener mDismissListener;

	public DismissListView(ListView listView) {

		if (listView == null) {
			throw new IllegalArgumentException("listview must not be null.");
		}
		mSwipeDisabled = true;
		listView.setOnItemClickListener(this);
	}

	class PendingDismissData implements Comparable<PendingDismissData> {

		public int position;
		public View view;

		public PendingDismissData(int position, View view) {
			this.position = position;
			this.view = view;
		}

		@Override
		public int compareTo(PendingDismissData other) {
			return other.position - position;
		}
	}

	private void performDismiss(final View dismissView, final int dismissPosition) {

		final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
		final int originalHeight = dismissView.getHeight();

		ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(ANIMATION_TIME);

		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {

				ViewGroup.LayoutParams lp;
				for (PendingDismissData pendingDismiss : mPendingDismisses) {
					setAlpha(pendingDismiss.view, 1f);
					setTranslationX(pendingDismiss.view, 0);
					lp = pendingDismiss.view.getLayoutParams();
					lp.height = originalHeight;
					pendingDismiss.view.setLayoutParams(lp);
				}
				mPendingDismisses.clear();
				if (null != mDismissListener) {
					mDismissListener.onDismiss(dismissPosition);
				}
			}
		});

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				lp.height = (Integer) valueAnimator.getAnimatedValue();
				dismissView.setLayoutParams(lp);
			}
		});

		mPendingDismisses.add(new PendingDismissData(dismissPosition, dismissView));
		animator.start();
	}

	public void setSwipeDisabled(boolean disabled) {
		this.mSwipeDisabled = disabled;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
		if (null != mItemClickListener) {
			mItemClickListener.onItemClick(parent, view, position, id);
		}
		if (mSwipeDisabled) {
			animate(view).translationX(-view.getWidth()).alpha(0).setDuration(ANIMATION_TIME).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					performDismiss(view, position);
				}
			});
		}
	}

	public void setOnItemClick(OnItemClickListener listener) {
		this.mItemClickListener = listener;
	}

	public void setOnDismissListener(OnDismissListener listener) {
		this.mDismissListener = listener;
	}

	public interface OnDismissListener {
		void onDismiss(int position);
	}
}
