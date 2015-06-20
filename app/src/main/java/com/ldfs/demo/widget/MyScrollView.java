package com.ldfs.demo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;

import com.ldfs.demo.util.ViewInject;


/**
 * Created by momo on 2015/4/23.
 * 自定义scrollView,解决webview添加布局,以及事件问题
 */
public class MyScrollView extends LinearLayout {
    private static final int INVALID_POINTER = -1;
    private Context mContext;
    private MyWebView mWebView;
    private LinearLayout mLayout;
    private ListView mListView;
    private float mLastMotionY;
    private int mActivePointerId;

    private boolean mIsBeingDragged;
    private VelocityTracker mVelocityTracker;
    private OverScroller mScroller;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mTouchSlop;
    private int mScrollY;
    private int mScrollX;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScrollView(context);
    }



    private void initScrollView(Context context) {
        mContext=context;
        mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mTouchSlop = configuration.getScaledTouchSlop();

        mScroller = new OverScroller(getContext());
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewInject.init(this);
        mWebView = (MyWebView) getChildAt(0);
        mLayout = (LinearLayout) getChildAt(1);
        mListView= (ListView) getChildAt(2);
        mWebView.setOnFlingListener(new MyWebView.OnFlingListener() {
            @Override
            public void onFling() {
                fling(1000, 3000);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isFling;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (AbsListView.OnScrollListener.SCROLL_STATE_FLING == scrollState) {
                    isFling = true;
                } else if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState && isFling) {
                    isFling = false;
                    //监测滑动到顶部,然后往上滑动一段距离
                    if (0 == mListView.getFirstVisiblePosition() && 0 <= mListView.getChildAt(0).getTop()) {
                        fling(getScrollRange() - 200, -1000);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        int childCount = mLayout.getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = mLayout.getChildAt(i);
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                }
            });
        }

    }




public boolean onInterceptTouchEvent(MotionEvent ev) {    final int action = ev.getAction();
    if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
        return true;
    }

    switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_MOVE: {
            final int activePointerId = mActivePointerId;
            if (activePointerId == INVALID_POINTER) {
                break;
            }

            final int pointerIndex = ev.findPointerIndex(activePointerId);
            final float y = ev.getY(pointerIndex);
            final int yDiff = (int) Math.abs(y - mLastMotionY);
            if (yDiff > mTouchSlop) {
                mIsBeingDragged = true;
                mLastMotionY = y;
            }
            break;
        }

        case MotionEvent.ACTION_DOWN: {
            final float y = ev.getY();
            mLastMotionY = y;
            if (!inChild((int) ev.getX(), (int) y)) {
                mIsBeingDragged = false;
                break;
            }
            mActivePointerId = ev.getPointerId(0);
            mIsBeingDragged = !mScroller.isFinished();
            break;
        }

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            mIsBeingDragged = false;
            mActivePointerId = INVALID_POINTER;
            if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
                invalidate();
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            onSecondaryPointerUp(ev);
            break;
    }


    float x=ev.getX();
    float y=ev.getY();

    int scrollRange = getScrollRange();
    boolean isScroll;
    //1:在头或尾的时候,把事件放给子ivew
    //2:在头向下拉,在尾向上拉.放给父类.
    //3:响应中间区别的滑动点击
    mScrollY=getScrollY();
    float v = mWebView.getContentHeight() * mWebView.getScale();
    //WebView的现高度
    float currentHeight = (mWebView.getHeight() + mWebView.getScrollY());
    if(0==mScrollY){
        //在顶部,向上滑,并且view已经滑到底部
        if(v == currentHeight&&y>mLastMotionY){
            isScroll=true;
        } else {
            isScroll=false;
        }
    } else if(scrollRange==mScrollY){
        //在底部,向下滑,并且view已经滑到顶部
        if(y>mLastMotionY&&0==mListView.getFirstVisiblePosition()&&0<=mListView.getChildAt(0).getTop()){
            isScroll=true;
        } else {
            isScroll=false;
        }
    } else {
        isScroll=true;
    }

    //设定滑动点击并存机制
    Rect outRect=new Rect();
    mLayout.getHitRect(outRect);
    outRect.top-=mScrollY;
    outRect.bottom-=mScrollY;
    if(outRect.contains((int)x,(int)y)){
        //先放开事件
        isScroll=false;
        if(Math.abs(y-mLastMotionY)>mTouchSlop){
            isScroll=true;
        }
    }

    return isScroll;
}


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            childView.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private int getScrollRange() {
        int scrollRange = 0;
        int childCount = getChildCount();
        if (childCount > 0) {
            int height=0;
            for(int i=0;i<childCount;i++){
                View child = getChildAt(i);
                height+=child.getHeight();
            }
            scrollRange = Math.max(0,
                    height - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollY = mScrollY;
            final View child = getChildAt(0);
            return !(y < child.getTop() - scrollY
                    || y >= child.getBottom() - scrollY
                    || x < child.getLeft()
                    || x >= child.getRight());
        }
        return false;
    }


    @SuppressLint({ "ClickableViewAccessibility"})
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(ev);

        final int action = ev.getAction();

        mScrollY=getScrollY();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mIsBeingDragged = getChildCount() != 0;
                if (!mIsBeingDragged) {
                    return false;
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final float y = ev.getY();
                int deltaY = (int) (mLastMotionY - y);

                if (!mIsBeingDragged) {
                    // 判断是否为一个可用move的依据
                    if (Math.abs(deltaY) > mTouchSlop) {
                        mIsBeingDragged = true;
                    }

                }

                if (mIsBeingDragged) {
                    mLastMotionY = y;

                    float oldScrollY = getScrollY();
                    float scrollY = oldScrollY + deltaY;

                    overScrollBy( 0,deltaY,  getScrollX(),(int)scrollY,  0,getScrollRange(),  0,0, true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker
                            .getYVelocity(mActivePointerId);

                    if (getChildCount() > 0) {
                        // 速度超过某个阀值时才视为fling
                        if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                            fling(getScrollRange(),-initialVelocity);
                        }
                    }

                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;

                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    endDrag();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                  boolean clampedY) {
        if (!mScroller.isFinished()) {
            mScrollX = scrollX;
            mScrollY = scrollY;
            if (clampedY) {
                mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange());
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
    }

    private void endDrag() {
        mIsBeingDragged = false;

        recycleVelocityTracker();
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * Fling the scroll view
     *
     * @param velocityX
     *            The initial velocitX in the X direction. Positive numbers mean
     *            that the finger/cursor is moving down the screen, which means
     *            we want to scroll towards the top.
     */
    public void fling(int scrollRange,int velocityX) {
        if (getChildCount() > 0) {
            mScroller.fling(getScrollX(), getScrollY(),
                    0, velocityX,
                    0, 0,
                    0, scrollRange,
                    0, 0);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }

            invalidate();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }



    public void setAdapter(final BaseAdapter adapter){
        mListView.setAdapter(adapter);
//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int height = getHeight();
//                if (0 != height) {
//                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    if (!adapter.isEmpty()) {
//                        ViewGroup.LayoutParams params = mListView.getLayoutParams();
//                        params.height = height;
//                        mListView.requestLayout();
//                    }
//                }
//            }
//        });

    }


}
