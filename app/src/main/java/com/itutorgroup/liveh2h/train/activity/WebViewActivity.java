package com.itutorgroup.liveh2h.train.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.mosai.ui.HorizontalProgressBarWithNumber;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class WebViewActivity extends ABaseToolbarActivity {
    @BindView(R.id.hprogressbar)
    HorizontalProgressBarWithNumber hprogressbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String url;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.webview)
    WebView webview;
    private Resources resources;
    @Override
    protected void initDatas() {
        webview.getSettings().setJavaScriptEnabled(true);
//        webview.setWebChromeClient(new WebChromeClient());
//        webview.getSettings().setSupportZoom(true);
//        webview.getSettings().setBuiltInZoomControls(true);

        webview.setWebChromeClient(new WebChromeClient() {
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
        resources = (Resources) getIntent().getSerializableExtra("resource");
        url = (String) getIntent().getSerializableExtra("url");
        String fileUrl = String.format("https://docs.google.com/gview?embedded=true&url=%s", url);

        webview.loadUrl(fileUrl);
        tvTitle.setText(getIntent().getStringExtra("filename"));
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void onEventMainThread(Event.SubmitPercent submitPercent){

    }
    @OnClick(R.id.ib_back)
    public void onClick() {
        back();
    }

    @Override
    public void back() {
       submitPercent();
    }

    @Override
    public void onBackPressed() {
        back();
    }
    private void submitPercent(){
        if(resources.percent!=100){
            AppAction.submitResourcePercent(context, resources.getClassId(), resources.getResourceId(), 100, new HttpResponseHandler(context,HttpResponse.class) {
                @Override
                public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                    Event.SubmitPercent submitPercent = new Event.SubmitPercent();
                    submitPercent.resources = resources;
                    EventBus.getDefault().post(submitPercent);
                    WebViewActivity.super.back();
                }
                @Override
                public void onResponeseStart() {
                    showProgressDialog();
                }

                @Override
                public void onResponesefinish() {

                    dismissProgressDialog();
                }

                @Override
                public void onResponeseFail(int statusCode, HttpResponse response) {
                    showHintDialog(response.message);
                }
            });

        }else{
            WebViewActivity.super.back();
        }
    }
}
