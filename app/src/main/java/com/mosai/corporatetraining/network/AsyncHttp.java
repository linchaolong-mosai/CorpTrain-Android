package com.mosai.corporatetraining.network;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.util.LogUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.HashMap;
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
        client.setTimeout(15000); // 设置链接超时，如果不设置，默认为15s
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
        LogUtils.i(url);
        client.addHeader("apiToken", UserPF.getInstance().getString(UserPF.API_TOKEN, ""));
//        client.addHeader("Content-Type","application/json");
        if(hashMap==null){
            client.addHeader("Content-Type","application/json");
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
    protected void execute(Context context, String url, RequestParams params, Map<String, Object> map, int method, String contentType,
                           AsyncHttpResponseHandler responseHandler,String charset){
        params = getRequestParams(params, map);
        LogUtils.i(url + (params == null?"":("?" + params.toString())));
        if (contentType == null) {
            client.removeHeader(AsyncHttpClient.HEADER_CONTENT_TYPE);
        } else {
            client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, contentType);
        }
        client.addHeader("apiToken", UserPF.getInstance().getString(UserPF.API_TOKEN, ""));

        switch (method) {
            case METHOD_POST:
                client.post(context, url, params, responseHandler);
                break;
            case METHOD_GET:
                client.get(context, url, params, responseHandler);
                break;
            case METHOD_PUT:
                JSONObject json = new JSONObject(map);
                try {
                    StringEntity body = new StringEntity(json.toString());
//                    body.setContentEncoding(charset==null?"utf-8":charset);
                    client.put(context, url, body, ContentType.APPLICATION_JSON.toString(), responseHandler);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                client.put(context, url, params, responseHandler);
                break;
        }
    }
    protected void execute(Context context, String url, RequestParams params, Map<String, Object> map, int method, String contentType,
                           AsyncHttpResponseHandler responseHandler){
       execute(context,url,params,map,method,contentType,responseHandler,null);
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
