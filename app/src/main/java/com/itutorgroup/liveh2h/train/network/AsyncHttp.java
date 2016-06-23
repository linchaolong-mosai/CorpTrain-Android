package com.itutorgroup.liveh2h.train.network;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.util.LogUtils;
import com.lidroid.xutils.http.client.util.URLEncodedUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.mosai.utils.EncodeUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * HTTP请求
 *
 * @author Rays 2016年4月13日
 *
 */
public class AsyncHttp {
    public static final String API_TOKEN = "API_TOKEN";
    public static final int METHOD_POST = 0;
    public static final int METHOD_GET = 1;
    public static final int METHOD_PUT = 2;
	private static final AsyncHttp INSTANCE = new AsyncHttp();
	private AsyncHttpClient client = new AsyncHttpClient();

	private AsyncHttp() {

	}

	public static AsyncHttp getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
        client.setTimeout(30000); // 设置链接超时，如果不设置，默认为15s
        client.setCookieStore(new PersistentCookieStore(context.getApplicationContext()));
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(socketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public AsyncHttpClient getClient() {
		return client;
	}

    protected void execute(Context context, String url, int method,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, null, null, method, null, responseHandler);
    }

    protected void execute(Context context, String url, int method, String contentType,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, null, null, method, contentType, responseHandler);
    }

    protected void execute(Context context, String url, Map<String, Object> map, int method,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, null, map, method, null, responseHandler);
    }

    protected void execute(Context context, String url, Map<String, Object> map, int method, String contentType,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, null, map, method, contentType, responseHandler);
    }

    protected void execute(Context context, String url, RequestParams params, int method,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, params, null, method, null, responseHandler);
    }

    protected void execute(Context context, String url, RequestParams params, int method, String contentType,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, params, null, method, contentType, responseHandler);
    }

    protected void execute(Context context, String url, RequestParams params, Map<String, Object> map, int method,
                           AsyncHttpResponseHandler responseHandler){
        execute(context, url, params, map, method, null, responseHandler);
    }
    protected void postJsonBody(Context context,String url,HashMap<String,Object> hashMap,AsyncHttpResponseHandler responseHandler){
//        url = EncodeUtil.encodeUrl(url);
        client.addHeader(API_TOKEN, UserPF.getInstance().getString(UserPF.API_TOKEN, ""));
        if(hashMap==null){
            client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE,ContentType.APPLICATION_JSON.toString());
            client.post(context,url,null,null,responseHandler);
        }else{
            JSONObject jsonObject = new JSONObject(hashMap);
            try {
                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                client.post(context,url,stringEntity,ContentType.APPLICATION_JSON.toString(),responseHandler);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    protected void putJsonBody(Context context,String url,HashMap<String,Object> hashMap,AsyncHttpResponseHandler responseHandler){
//        url = EncodeUtil.encodeUrl(url);
        client.addHeader(API_TOKEN, UserPF.getInstance().getString(UserPF.API_TOKEN, ""));
        if(hashMap==null){
            client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE,ContentType.APPLICATION_JSON.toString());
            client.post(context,url,null,null,responseHandler);
        }else{
            JSONObject jsonObject = new JSONObject(hashMap);
            try {
                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                client.put(context,url,stringEntity,ContentType.APPLICATION_JSON.toString(),responseHandler);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }
    protected void execute(Context context, String url, RequestParams params, Map<String, Object> map, int method, String contentType,
                           AsyncHttpResponseHandler responseHandler){
        params = getRequestParams(params, map);
        if (contentType == null) {
            client.removeHeader(AsyncHttpClient.HEADER_CONTENT_TYPE);
        } else {
            client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, contentType);
        }
        client.addHeader(API_TOKEN, UserPF.getInstance().getString(UserPF.API_TOKEN, ""));
        switch (method) {
            case METHOD_POST:
                client.post(context, url, params, responseHandler);
                break;
            case METHOD_GET:
                url = EncodeUtil.encodeUrl(url);
                client.get(context, url, params, responseHandler);
                break;
            case METHOD_PUT:
                client.put(context, url, params, responseHandler);
                break;
            default:
                break;
        }
        LogUtils.i(url + (params == null?"":("?" + params.toString())));
    }

    private RequestParams getRequestParams(RequestParams params, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            if (params == null) {
                params = new RequestParams();
            }
            params.put("json", JSON.toJSONString(map));
        }
        return params;
    }

}
