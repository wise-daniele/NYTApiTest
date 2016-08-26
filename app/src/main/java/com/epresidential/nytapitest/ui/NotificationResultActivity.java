package com.epresidential.nytapitest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.epresidential.nytapitest.R;

/**
 * Created by daniele on 26/08/16.
 */
public class NotificationResultActivity extends AppCompatActivity {

    public static final String WEB_URL_EXTRA = "web_url_extra";

    private String mWebUrl;
    private WebView articleWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_result);
        mWebUrl = getIntent().getStringExtra(WEB_URL_EXTRA);
        articleWebView = (WebView) findViewById(R.id.article_web_view);
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.loadUrl(mWebUrl);
        articleWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
        return true;
    }
}
