package com.ldfs.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by cz on 15/6/2.
 */
public class MWebView extends WebView {
    public MWebView(Context context) {
        super(context);
    }

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    public boolean isBottom(){
        float contentHeight = getContentHeight() * getScale();
        float currentHeight = getHeight() + getScrollY();
        return contentHeight == currentHeight;
    }
}
