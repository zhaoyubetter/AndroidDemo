package com.ldfs.demo.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.util.FileUtils;
import com.ldfs.demo.util.HandleTask;
import com.ldfs.demo.util.Loger;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.MyScrollView;

/**
 * 自定义ScrollView 解决WebView 与其他控件并存.WebView Event事件丢失
 */
public class FragmentWebView extends Fragment {
    @ID(id=R.id.scroll_view)
    private MyScrollView mScrollView;
    @ID(id = R.id.web_view)
    private WebView mWebView;
    @ID(id=R.id.list)
    private ListView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        ViewInject.init(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JavaSpriptInterface(), "listener");
        String content = FileUtils.getContentFromAssets(getResources(), "2048580829");
        final StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(content);
        mWebView.loadDataWithBaseURL("file:///android_asset/", localStringBuilder.toString(), "text/html", "utf-8", null);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addListener(view);
            }
        });
//        mScrollView.setAdapter(new ArrayAdapter<String>(getActivity(), new String[]{"A", "B", "D", "A", "B", "D", "A", "B", "D", "A", "B", "D", "A", "B", "D", "A", "B", "D", "A", "B", "D"}));
    }

    // 注入js函数监听
    private static void addListener(final WebView webView) {
        HandleTask.run(new HandleTask.TaskAction<String>() {
                           @Override
                           public String run() {
                               return FileUtils.getContentFromAssets(App.getAppResource(), "js/weixin_android.js");
                           }

                           @Override
                           public void postRun(String content) {
                               if (!TextUtils.isEmpty(content)) {
                                   webView.loadUrl("javascript:(function(){" + content + "})()");
                               }
                               webView.loadUrl("javascript:(function(){window.listener.loadJSFinished();})()");
                           }
                       }
        );
    }

    @MethodClick(ids = {R.id.btn1, R.id.btn2})
    public void OnClick(View v) {
        App.toast("click~");
    }

    // js通信接口
    public class JavaSpriptInterface {
        @JavascriptInterface
        public void onImgClick(String[] urls, int pos) {
        }

        @JavascriptInterface
        public void log(String log) {
            Loger.i(this, log);
        }

        @JavascriptInterface
        public void followAccount() {// 关注公众号
        }

        @JavascriptInterface
        public void clickOpenSourceUrl(final String paramString) {
        }

        @JavascriptInterface
        public void clickPub() {
        }

        @JavascriptInterface
        public void loadJSFinished() {
//            Loger.i(this,"load_finish");
        }
    }
}
