package com.mosai.corporatetraining.network;

import android.text.TextUtils;

import com.loopj.android.http.TextHttpResponseHandler;
import com.mosai.corporatetraining.MyApplication;
import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.progress.IProgressIndicator;
import com.mosai.corporatetraining.util.FastJsonUtils;
import com.mosai.corporatetraining.util.LogUtils;

import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.Header;

public abstract class HttpResponseHandler extends TextHttpResponseHandler{
    private Class<? extends HttpResponse> mClass;
    private IProgressIndicator progressIndicator;

    public HttpResponseHandler(Class<? extends HttpResponse> mClass) {
        this(mClass, null);
    }

    public HttpResponseHandler(Class<? extends HttpResponse> mClass, IProgressIndicator progressIndicator) {
        super();
        this.mClass = mClass == null ? HttpResponse.class : mClass;
        this.progressIndicator = progressIndicator;
    }

	@Override
	public void onSuccess(int statusCode, Header[] headers,
			String responseString) {
		/*if (headers != null) {
			StringBuilder builder = new StringBuilder();
            for (Header h : headers) {
                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
                builder.append(_h);
                builder.append("\n");
            }
            LogUtils.i("请求headers：" + builder.toString());
		}*/
		LogUtils.i("状态码：" + statusCode + " 返回值：" + responseString);
		if (statusCode == HttpURLConnection.HTTP_OK) {
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
        String message = (TextUtils.isEmpty(responseString) ? getString(R.string.network_request_timeout) : responseString);
        onResponeseFail(statusCode, new HttpResponse(message));
        LogUtils.e("网络请求失败：code=" + statusCode + " " + message);
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
	 * @param statusCode 服务器响应值
	 * @param response 返回的数据
	 */
	public abstract void onResponeseSucess(int statusCode, HttpResponse response, String responseString);
	
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
	 * @param statusCode 服务器响应值
	 * @param response 返回的数据
	 */
	public void onResponeseFail(int statusCode, HttpResponse response){
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
