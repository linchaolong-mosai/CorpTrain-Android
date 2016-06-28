package com.itutorgroup.liveh2h.train.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.itutorgroup.liveh2h.train.network.progress.DefaultProgressIndicator;
import com.itutorgroup.liveh2h.train.network.progress.TextProgressIndicator;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;
import com.itutorgroup.liveh2h.train.widget.HintDialog;
import com.orhanobut.logger.Logger;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年06月24日 15:06
 * 邮箱：nianbin@mosainet.com
 */
public class BaseFragment extends Fragment{
    private TextProgressIndicator textProgressIndicator;
    private Toast toast;
    private HintDialog hintDialog;
    protected DefaultProgressIndicator progressIndicator;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAnalyticsTrackName();
    }
    protected String getAnalyticsTrackName(){
        return null;
    }
    protected void setAnalyticsTrackName(){
//        Logger.t("event").e("onResume");
        if(!TextUtils.isEmpty(getAnalyticsTrackName())){
            AnalyticsUtils.setScreen(mContext,getAnalyticsTrackName());
        }
    }
    public void showTextProgressDialog(String message) {
        if (textProgressIndicator == null)
            textProgressIndicator = TextProgressIndicator.newInstance(mContext);
        textProgressIndicator.showDialog(message);
    }

    public void dismissTextProgressDialog() {
        if (textProgressIndicator != null)
            textProgressIndicator.dismissDialg();
    }
    public void showProgressDialog() {
        if (progressIndicator == null) {
            progressIndicator = DefaultProgressIndicator.newInstance(mContext);

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
            hintDialog = new HintDialog(mContext);
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
        toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示Toast
     */
    public void showToast(int resId) {
        showToast(getResources().getText(resId));
    }
}
