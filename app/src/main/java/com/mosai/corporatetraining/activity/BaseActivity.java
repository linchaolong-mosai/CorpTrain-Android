package com.mosai.corporatetraining.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mosai.corporatetraining.broadcast.TokenExpireReceiver;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.network.AsyncHttp;
import com.mosai.corporatetraining.network.progress.DefaultProgressIndicator;
import com.mosai.corporatetraining.util.AppManager;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.corporatetraining.widget.HintDialog;
import com.mosai.utils.SwitchingAnim;

/**
 * 公共父类
 * 
 * @author Rays
 *
 */
public class BaseActivity extends AppCompatActivity {
	protected Context context;
	private Toast toast;
    private HintDialog hintDialog;
    private DefaultProgressIndicator progressIndicator;
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

    private void unregister() {
        registerTokenExpireBroadcast();
    }
    private void register() {
        unregisterTokenExpireBroadcast();
    }
    public void back() {
        SwitchingAnim.backward(this);
    }

    public void forword() {
        SwitchingAnim.forward(this);
    }

    protected boolean openTokenExpireBroadcast(){
        return true;
    }
    private void registerTokenExpireBroadcast(){
        if(openTokenExpireBroadcast()){
            if(tokenexpireIntentFilter==null && tokenExpireReceiver==null){
                tokenexpireIntentFilter = new IntentFilter(TokenExpireReceiver.action);
                tokenExpireReceiver = new TokenExpireReceiver();
                registerReceiver(tokenExpireReceiver,tokenexpireIntentFilter);
                tokenExpireReceiver.setTokenExpireCallback(new TokenExpireReceiver.TokenExpireCallback() {
                    @Override
                    public void teCallback() {
                        //登录超时
                        AppManager.getAppManager().finishAllActivity();
//                        UserPF.getInstance().putBoolean(UserPF.IS_LOGIN, false);
                        UserPF.getInstance().putString(UserPF.PASSWORD,"");
                        startActivity(new Intent(BaseActivity.this,LoginActivity.class));
                    }
                });
            }
        }
    }
    private void unregisterTokenExpireBroadcast(){
        if(openTokenExpireBroadcast()){

        }
    }
}
