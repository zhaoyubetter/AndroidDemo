package com.ldfs.demo.widget.guide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

import com.ldfs.demo.R;
import com.ldfs.demo.util.UnitUtils;

/**
 * 遮罩层
 * 
 * @author momo
 * @Date 2015/1/22
 * 
 */
public class GuideView extends RelativeLayout {
	private static final int DEFAULT_BG = 0x99000000;
	private int mPadding;
	private Paint mPaint;
	private RectF[] rectFs;
	private Bitmap[] mDrawBitmaps;// 中间选择区域
	private Bitmap mMaskBitmap;// 遮罩背景
	private PorterDuffXfermode srcOutXfermode;

	public GuideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setId(R.id.guide_container);
		setWillNotDraw(false);
		mPaint = new Paint();
		mPaint.setFilterBitmap(false);
		mPadding = UnitUtils.dip2px(context, 20);
		srcOutXfermode = new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_OUT);
	}

	public GuideView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GuideView(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initViews();
	}

	public void initViews() {
		View childView = null;
		ViewGroup childGrounp = null;
		List<View> mastViews = new ArrayList<View>();
		LinkedList<View> views = new LinkedList<View>();
		views.add(this);
		while (!views.isEmpty()) {
			childView = views.removeFirst();
			if (childView instanceof MastView) {
				mastViews.add(childView);
			} else if (childView instanceof ViewGroup) {
				childGrounp = ((ViewGroup) childView);
				int childCount = childGrounp.getChildCount();
				for (int i = 0; i < childCount; i++) {
					views.add(childGrounp.getChildAt(i));
				}
			}
		}
		initGuideViews(mastViews);
	}

	private void initGuideViews(final List<View> views) {
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				if (null != views) {
					View view = null;
					Rect rect = new Rect();
					int length = views.size();
					rectFs = new RectF[length];
					mDrawBitmaps = new Bitmap[length];
					Rect outRect = new Rect();
					getWindowVisibleDisplayFrame(outRect);
					int[] location = new int[2];
					for (int i = 0; i < length; i++) {
						view = views.get(i);
						view.getRight();
						view.getDrawingRect(rect);
						view.getLocationInWindow(location);
						rectFs[i] = new RectF(rect.left + location[0] - mPadding / 2, rect.top + location[1] - outRect.top - mPadding / 2, rect.right + location[0] + mPadding / 2, rect.bottom
								+ location[1] - outRect.top + mPadding / 2);
						mDrawBitmaps[i] = BitmapFactory.decodeResource(getResources(), R.drawable.mask_bg);
					}
					invalidate();
				}
			}
		});
	}

	/**
	 * 遮罩背景
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	private Bitmap makeMask() {
		int w = getWidth();
		int h = getHeight();
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(DEFAULT_BG);
		c.drawRect(0, 0, w, h, p);
		return bm;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null == mDrawBitmaps) {
			return;
		}
		if (null == mMaskBitmap) {
			mMaskBitmap = makeMask();
		}
		int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		for (int i = 0; i < mDrawBitmaps.length; i++) {
			canvas.drawBitmap(mDrawBitmaps[i], null, rectFs[i], mPaint);
		}
		mPaint.setXfermode(srcOutXfermode);
		canvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
		mPaint.setXfermode(null);
		canvas.restoreToCount(sc);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 只允许遮罩层点击事件
		float rawX = event.getRawX();
		float rawY = event.getRawY();
		int length = rectFs.length;
		boolean result = false;
		for (int i = 0; i < length; i++) {
			if (rectFs[i].contains(rawX, rawY - mPadding / 2)) {
				result = true;
				break;
			}
		}
		return result ? super.onTouchEvent(event) : true;
	}
}
