package com.mosai.corporatetraining.network.progress;

import android.app.Dialog;
import android.content.Context;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.widget.HintDialog;

/**
 * Created by Rays on 16/5/12.
 */
public class DefaultProgressIndicator extends Dialog implements IProgressIndicator {
//    private TextView messages;
    private Context context;

    public static DefaultProgressIndicator newInstance(Context context) {
        return new DefaultProgressIndicator(context);
    }

    public static DefaultProgressIndicator newInstance(Context context, int themeResId) {
        return new DefaultProgressIndicator(context);
    }

    public DefaultProgressIndicator(Context context) {
        this(context, R.style.dialog);
    }

    public DefaultProgressIndicator(Context context, int themeResId) {
        super(context, themeResId);
        initViews(context);
    }

    private void initViews(Context context) {
        this.context = context;
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
//        messages = ViewUtil.findViewById(view, R.id.messages);
        this.setContentView(R.layout.dialog_loading);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    /*public void setMessages(CharSequence text) {
        messages.setText(text);
    }

    public void setMessages(int resid) {
        messages.setText(resid);
    }*/

    @Override
    public void showErrorInfo(String message) {
        HintDialog hintDialog = new HintDialog(context);
        hintDialog.setMessages(message);
        hintDialog.show();
    }

    @Override
    public void showProgress() {
        show();
    }

    @Override
    public void dismissProgress() {
        if (isShowing()) {
            dismiss();
        }
    }
}
