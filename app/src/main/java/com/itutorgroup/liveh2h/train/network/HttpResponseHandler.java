package com.itutorgroup.liveh2h.train.network;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.text.TextUtils;

import com.itutorgroup.liveh2h.train.MyApplication;
import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.broadcast.TokenExpireReceiver;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.progress.IProgressIndicator;
import com.itutorgroup.liveh2h.train.util.FastJsonUtils;
import com.itutorgroup.liveh2h.train.util.LogUtils;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosai.utils.Tools;
import com.orhanobut.logger.Logger;

import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.Header;

public abstract class HttpResponseHandler extends TextHttpResponseHandler {
    private String url;
    private boolean showTips;
    private Class<? extends HttpResponse> mClass;
    private IProgressIndicator progressIndicator;
    private Context context;

    public HttpResponseHandler(Context context, Class<? extends HttpResponse> mClass) {
        super();
        this.mClass = mClass == null ? HttpResponse.class : mClass;
        this.context = context.getApplicationContext();
    }

    public HttpResponseHandler(Context context, Class<? extends HttpResponse> mClass, boolean showTips) {
        super();
        this.mClass = mClass == null ? HttpResponse.class : mClass;
        this.context = context.getApplicationContext();
        this.showTips = showTips;
    }

    public HttpResponseHandler(Context context, Class<? extends HttpResponse> mClass, String url) {
        super();
        this.mClass = mClass == null ? HttpResponse.class : mClass;
        this.context = context.getApplicationContext();
        this.url = url;
    }

    public HttpResponseHandler(Context context, Class<? extends HttpResponse> mClass, IProgressIndicator progressIndicator) {
        super();
        this.mClass = mClass == null ? HttpResponse.class : mClass;
        this.progressIndicator = progressIndicator;
        this.context = context.getApplicationContext();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers,
                          String responseString) {
        Logger.t("http").e("url:" + url + "\n状态码：" + statusCode + "\n" + "返回结果:" + responseString);
        if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
            try {
                if (FastJsonUtils.isJson(responseString)) {
                    HttpResponse response = FastJsonUtils.parseObject(responseString, mClass);
                    if (response != null && response.isSuccess()) {
                        onResponeseSucess(statusCode, response, responseString);// 成功
                    } else {
                        onResponeseFail(statusCode, response);
                    }
                } else {
                    onResponeseFail(statusCode, new HttpResponse(getString(R.string.parse_data_error)));
                }
            } catch (Exception e) {
                LogUtils.e("解析json数据异常", e);
                onResponeseFail(statusCode, new HttpResponse(getString(R.string.parse_data_error)));
            }
        } else {

            onResponeseFail(statusCode, new HttpResponse(getString(R.string.server_response_value_error, statusCode)));
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          String responseString, Throwable throwable) {
        String message;
        if (Tools.isConnect(context)) {
            message = (TextUtils.isEmpty(responseString) ? getString(R.string.server_error) : responseString);
            if (!TextUtils.isEmpty(message)&&FastJsonUtils.isJson(responseString)) {
                HttpResponse response = FastJsonUtils.parseObject(responseString, mClass);
                //token_expire
                if (response.returnCode == HttpResponse.CODE_TOKEN_EXPIRE) {
                    context.sendBroadcast(new Intent(TokenExpireReceiver.action));
                    LogUtils.e("returnCode = " + HttpResponse.CODE_TOKEN_EXPIRE);
                } else if ((response.returnCode == HttpResponse.CODE_NO_TOKEN) && !TextUtils.isEmpty(UserPF.getInstance().getString(UserPF.API_TOKEN, ""))) {
                    context.sendBroadcast(new Intent(TokenExpireReceiver.action));
                    LogUtils.e("returnCode = " + HttpResponse.CODE_NO_TOKEN);
                }
            }

        } else {
            message = getString(R.string.network_request_timeout);
        }
        onResponeseFail(statusCode, new HttpResponse(message));
        Logger.t("http").e("url:" + url + "\n网络请求失败：code=" + statusCode + " " + message);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        onResponesefinish();
    }

    @Override
    public void onStart() {
        super.onStart();
        onResponeseStart();
    }

    protected final String getString(int resId, Object... formatArgs) {
        return MyApplication.INSTANCE.getString(resId, formatArgs);
    }

    /**
     * 当请求成功statusCode为200时候调用
     *
     * @param statusCode 服务器响应值
     * @param response   返回的数据
     */
    public abstract void onResponeseSucess(int statusCode, HttpResponse response, String responseString) throws PackageManager.NameNotFoundException;

    /**
     * 请求发起前调用
     */
    public void onResponeseStart() {
        showProgress();
    }

    /**
     * 请求完成时候调用 无论失败成功都会调用
     */
    public void onResponesefinish() {
        dismissProgress();
    }


    /**
     * 错误调用
     *
     * @param statusCode 服务器响应值
     * @param response   返回的数据
     */
    public void onResponeseFail(int statusCode, HttpResponse response) {
        showErrorInfo(response.message);
    }

    public void showErrorInfo(String message) {
        if (progressIndicator != null) {
            progressIndicator.showErrorInfo(message);
        }
    }

    public void showProgress() {
        if (progressIndicator != null) {
            progressIndicator.showProgress();
        }
    }

    public void dismissProgress() {
        if (progressIndicator != null) {
            progressIndicator.dismissProgress();
        }
    }

}
