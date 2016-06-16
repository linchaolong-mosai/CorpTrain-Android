package com.itutorgroup.liveh2h.train.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.network.progress.DefaultProgressIndicator;
import com.itutorgroup.liveh2h.train.util.Validator;
import com.itutorgroup.liveh2h.train.util.ViewUtil;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    private Button btnSend, btnLogin;
    private TextView tvEmail;
    private EditText etEmail;

    @Override
    protected boolean openTokenExpireBroadcast() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();
        initListener();
        initData();
    }

    private void initViews() {
        btnSend = ViewUtil.findViewById(this, R.id.btnSend);
        btnLogin = ViewUtil.findViewById(this, R.id.btnLogin);
        tvEmail = ViewUtil.findViewById(this, R.id.tvEmail);
        etEmail = ViewUtil.findViewById(this, R.id.etEmail);
    }

    private void initListener() {
        btnSend.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        ViewUtil.setEdittextLabelVisibility(etEmail, tvEmail);

    }

    private void initData() {
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            etEmail.setText(email);
            etEmail.setSelection(email.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                forgotPassword();
                break;
            case R.id.btnLogin:
                finish();
                break;
        }
    }

    private void forgotPassword() {
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            showHintDialog(R.string.email_cannot_be_empty);
            return;
        }
        if (!Validator.isEmail(email)) {
            showHintDialog(R.string.email_format_error);
            return;
        }
        AppAction.forgotPassword(context, email, new HttpResponseHandler(context,HttpResponse.class, DefaultProgressIndicator.newInstance(context)) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                showHintDialog(R.string.forgot_success_hint).setTitles(R.string.success);
            }
            @Override
            public void onResponeseStart() {
                showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                dismissProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                showHintDialog(response.message);
            }
        });
    }
}
