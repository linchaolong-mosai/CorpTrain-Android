package com.mosai.corporatetraining.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.mosai.corporatetraining.R;
import com.mosai.ui.HorizontalProgressBarWithNumber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends ABaseToolbarActivity {
    @BindView(R.id.hprogressbar)
    HorizontalProgressBarWithNumber hprogressbar;
    private String url;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void initDatas() {
        webview.getSettings().setJavaScriptEnabled(true);
//        webview.setWebChromeClient(new WebChromeClient());
//        webview.getSettings().setSupportZoom(true);
//        webview.getSettings().setBuiltInZoomControls(true);

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                hprogressbar.setVisibility(View.VISIBLE);
                hprogressbar.setProgress(newProgress);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            public void onPageFinished(WebView view, String url) {
                hprogressbar.setVisibility(View.GONE);
            }
        });
        url = (String) getIntent().getSerializableExtra("url");
        String fileUrl = String.format("https://docs.google.com/gview?embedded=true&url=%s", url);

        webview.loadUrl(fileUrl);

    }

    @Override
    protected int setContent() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }


    @OnClick(R.id.ib_back)
    public void onClick() {
        back();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
