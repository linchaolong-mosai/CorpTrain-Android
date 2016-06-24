package com.itutorgroup.liveh2h.train.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.itutorgroup.liveh2h.train.broadcast.TokenExpireReceiver;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.AsyncHttp;
import com.itutorgroup.liveh2h.train.network.progress.DefaultProgressIndicator;
import com.itutorgroup.liveh2h.train.network.progress.TextProgressIndicator;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;
import com.itutorgroup.liveh2h.train.util.AppManager;
import com.itutorgroup.liveh2h.train.util.LogUtils;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.itutorgroup.liveh2h.train.widget.HintDialog;
import com.mosai.utils.SwitchingAnim;

/**
 * 公共父类
 *
 * @author Rays
 */
public class BaseActivity extends AppCompatActivity {
    private TextProgressIndicator textProgressIndicator;
    protected Context context;
    private Toast toast;
    private HintDialog hintDialog;
    protected DefaultProgressIndicator progressIndicator;
    private IntentFilter tokenexpireIntentFilter;
    private TokenExpireReceiver tokenExpireReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        AppManager.getAppManager().addActivity(this);
        register();
//        textProgressIndicator = TextProgressIndicator.newInstance(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewUtil.initStatusBar(this);

    }
    public void showTextProgressDialog(String message) {
        if (textProgressIndicator == null)
            textProgressIndicator = TextProgressIndicator.newInstance(this);
        textProgressIndicator.showDialog(message);
    }

    public void dismissTextProgressDialog() {
        if (textProgressIndicator != null)
            textProgressIndicator.dismissDialg();
    }
    public void showProgressDialog() {
        if (progressIndicator == null) {
            progressIndicator = DefaultProgressIndicator.newInstance(context);

        }
        progressIndicator.show();
    }

    public void dismissProgressDialog() {
        if (progressIndicator != null && progressIndicator.isShowing()) {
            progressIndicator.dismiss();
        }
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(int resId) {
        return showHintDialog(getText(resId));
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(CharSequence text) {
        if (hintDialog == null) {
            hintDialog = new HintDialog(context);
        }
        hintDialog.setMessages(text);
        if (!hintDialog.isShowing()) {
            hintDialog.show();
        }
        return hintDialog;
    }

    /**
     * 隐藏提示框
     */
    public void dismissHintDialog() {
        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }
    }

    /**
     * 显示Toast
     */
    public void showToast(CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示Toast
     */
    public void showToast(int resId) {
        showToast(getResources().getText(resId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissHintDialog();
        dismissProgressDialog();
        AsyncHttp.getInstance().getClient().cancelRequests(this, true);
        AppManager.getAppManager().finishActivity(this);
        unregister();
    }

    private void register() {
        registerTokenExpireBroadcast();
    }

    private void unregister() {
        unregisterTokenExpireBroadcast();
    }

    public void back() {
        SwitchingAnim.backward(this);
        unregister();
    }

    public void forword() {
        SwitchingAnim.forward(this);
    }

    protected boolean openTokenExpireBroadcast() {
        return false;
    }

    private void registerTokenExpireBroadcast() {
        if (openTokenExpireBroadcast()) {
            if (tokenexpireIntentFilter == null && tokenExpireReceiver == null) {
                tokenexpireIntentFilter = new IntentFilter(TokenExpireReceiver.action);
                tokenExpireReceiver = new TokenExpireReceiver();
                registerReceiver(tokenExpireReceiver, tokenexpireIntentFilter);
                tokenExpireReceiver.setTokenExpireCallback(new TokenExpireReceiver.TokenExpireCallback() {
                    @Override
                    public void teCallback() {
                        //token过时
                        LogUtils.e("Go to Login");
                        AppManager.getAppManager().finishAllActivity();
//                        UserPF.getInstance().putBoolean(UserPF.IS_LOGIN, false);
                        UserPF.getInstance().putString(UserPF.PASSWORD, "");
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                    }
                });
            }
        }
    }

    private void unregisterTokenExpireBroadcast() {
        if (openTokenExpireBroadcast()) {
            if (tokenexpireIntentFilter != null && tokenExpireReceiver != null) {
                unregisterReceiver(tokenExpireReceiver);
                tokenexpireIntentFilter = null;
                tokenExpireReceiver = null;
            }
        }
    }



    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    public String getAnalyticsTrackName() {
        return null;
    }

    public void setAnalyticsTrackName() {
        if (!TextUtils.isEmpty(getAnalyticsTrackName())) {
            AnalyticsUtils.setTracker(context, getAnalyticsTrackName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAnalyticsTrackName();
    }
}
