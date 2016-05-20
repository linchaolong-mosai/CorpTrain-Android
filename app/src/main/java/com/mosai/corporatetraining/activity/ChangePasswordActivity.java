package com.mosai.corporatetraining.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.network.progress.DefaultProgressIndicator;
import com.mosai.corporatetraining.util.ViewUtil;

/**
 * Created by Rays on 16/5/16.
 */
public class ChangePasswordActivity extends BaseToolbarActivity implements TextView.OnEditorActionListener {
    private EditText etCurrentPassword, etNewPassword, etConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
        initListener();
        initData();
    }

    private void initViews() {
        etCurrentPassword = ViewUtil.findViewById(this, R.id.etCurrentPassword);
        etNewPassword = ViewUtil.findViewById(this, R.id.etNewPassword);
        etConfirmNewPassword = ViewUtil.findViewById(this, R.id.etConfirmNewPassword);
    }

    private void initListener() {
        etConfirmNewPassword.setOnEditorActionListener(this);
    }

    private void initData() {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            update();
            return true;
        }
        return false;
    }

    private void update() {
        String currentPassword = etCurrentPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String confirmNewPassword = etConfirmNewPassword.getText().toString();
        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword)
                || TextUtils.isEmpty(confirmNewPassword)) {
            showHintDialog(R.string.password_cannot_be_empty);
            return;
        }
        if (!TextUtils.equals(newPassword, confirmNewPassword)) {
            showHintDialog(R.string.incorrect_password);
            return;
        }
        AppAction.changePassword(context, currentPassword, newPassword, new HttpResponseHandler(HttpResponse.class, DefaultProgressIndicator.newInstance(context)) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                finish();
            }
        });
    }
}
