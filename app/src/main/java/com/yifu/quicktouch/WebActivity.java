package com.yifu.quicktouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by yifu on 16-10-20.
 */

public class WebActivity extends AppCompatActivity {
    private WebView mWv;
    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initUi();
    }

    private void initUi() {
        mWv = (WebView) findViewById(R.id.wv);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mWv.loadUrl("https://github.com/GGandYY/QuickTouch");
        WebSettings settings = mWv.getSettings();
        settings.setJavaScriptEnabled(true);
        mWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgress.setVisibility(View.GONE);
            }
        });
    }
}
