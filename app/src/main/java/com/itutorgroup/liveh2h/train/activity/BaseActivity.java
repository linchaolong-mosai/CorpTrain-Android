package com.itutorgroup.liveh2h.train.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.itutorgroup.liveh2h.train.MyApplication;
import com.itutorgroup.liveh2h.train.NetworkChangedInterface;
import com.itutorgroup.liveh2h.train.R;
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
import com.mosai.utils.ToastUtils;
import com.orhanobut.logger.Logger;

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

        MyApplication.INSTANCE.foregound = false;
    }

    private void register() {
        registerTokenExpireBroadcast();
        registerHomeKeyEvent();
        registerNetworkChanged();
    }

    private void registerHomeKeyEvent() {
        //注册广播
        if (openHomeKeyEventBroadcast())
            registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
                    Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

    }

    private void unregisterHomeKeyEvent() {
        if (openHomeKeyEventBroadcast() && mHomeKeyEventReceiver != null)
            unregisterReceiver(mHomeKeyEventReceiver);
    }

    private void unregister() {
        unregisterTokenExpireBroadcast();
        unregisterHomeKeyEvent();
        unregisterNetworkChanged();
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

    protected boolean openHomeKeyEventBroadcast() {
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

    //字体不跟随系统设置变化
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAnalyticsTrackScreenName();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.t("lifecircle").e(MyApplication.INSTANCE.getAppCount() + "");
        checkForegound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.t("lifecircle").e(MyApplication.INSTANCE.getAppCount() + "");
        checkBackgound();
    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    if (MyApplication.INSTANCE.getAppCount() > 0)
                        Logger.t("ground").e("Background");
                    ToastUtils.showToast(context, "Background");
                    MyApplication.INSTANCE.foregound = true;
                    didEnterBackgroundEvent();
                }
            }
        }
    };

    protected String getAnalyticsTrackScreenName() {
        return null;
    }

    protected void setAnalyticsTrackScreenName() {
        if (!TextUtils.isEmpty(getAnalyticsTrackScreenName())) {
            AnalyticsUtils.setScreen(context, getAnalyticsTrackScreenName());
        }
    }

    protected void didEnterBackgroundEvent() {
        AnalyticsUtils.setEvent(context, R.array.DidEnterBackground);
    }

    protected void willEnterForeground() {
        AnalyticsUtils.setEvent(context, R.array.WillEnterForeground);
    }

    private void checkForegound() {
        if (MyApplication.INSTANCE.foregound) {
            if (MyApplication.INSTANCE.getAppCount() > 0) {
                MyApplication.INSTANCE.foregound = false;
                willEnterForeground();
                Logger.t("ground").e("Foreground");
            }
        }
    }

    private void checkBackgound() {
        if (MyApplication.INSTANCE.getAppCount() <= 0) {
            Logger.t("ground").e("Background");
            MyApplication.INSTANCE.foregound = true;
            didEnterBackgroundEvent();
        }
    }


    /*********************************************
     * 监听网络变化
     ***********************************/
    private IntentFilter networkChangedFilter;
    private NetworkChangedInterface networkChangedInterface;
    public BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.e("net changed");
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    if (networkChangedInterface != null)
                        networkChangedInterface.networkChanged(netInfo.isAvailable());
                }
            }
        }
    };

    private void registerNetworkChanged() {
        if (this instanceof NetworkChangedInterface) {
            networkChangedInterface = (NetworkChangedInterface) this;
            networkChangedFilter = new IntentFilter();
            networkChangedFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(myNetReceiver, networkChangedFilter);
        }

    }

    private void unregisterNetworkChanged() {
        if (this instanceof NetworkChangedInterface) {
            if (networkChangedInterface != null) {
                if (myNetReceiver != null && networkChangedFilter != null) {
                    unregisterReceiver(myNetReceiver);
                }
                myNetReceiver = null;
                networkChangedFilter = null;
            }
        }
    }
    /**************************************监听网络变化*********************************/
}
