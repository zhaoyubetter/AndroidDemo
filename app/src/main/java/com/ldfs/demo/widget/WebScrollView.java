package com.ldfs.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.ldfs.demo.R;

/**
 * Created by cz on 15/6/2.
 */
public class WebScrollView extends ScrollView {
    private MWebView mWebView;
    public WebScrollView(Context context) {
        super(context);
    }

    public WebScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mWebView= (MWebView) findViewById(R.id.web_view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mWebView.isBottom();
    }
}
