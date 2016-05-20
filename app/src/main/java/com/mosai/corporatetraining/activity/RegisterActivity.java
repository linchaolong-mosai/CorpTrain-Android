package com.mosai.corporatetraining.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.util.Validator;
import com.mosai.corporatetraining.util.ViewUtil;

/**
 * login input
 * @author Rays 2016年4月11日
 *
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvFirstName, tvLastName, tvEmail, tvPassword, tvRePassword, tvShowPassword, tvShowRePassword;
    private EditText etFirstName, etLastName, etEmail, etPassword, etRePassword;
    private Button btnSignUp, btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
		initViews();
		initListener();
		initData();
	}

	private void initViews() {
        tvFirstName = ViewUtil.findViewById(this, R.id.tvFirstName);
        tvLastName = ViewUtil.findViewById(this, R.id.tvLastName);
		tvEmail = ViewUtil.findViewById(this, R.id.tvEmail);
		tvPassword = ViewUtil.findViewById(this, R.id.tvPassword);
        tvRePassword = ViewUtil.findViewById(this, R.id.tvRePassword);
        tvShowPassword = ViewUtil.findViewById(this, R.id.tvShowPassword);
        tvShowRePassword = ViewUtil.findViewById(this, R.id.tvShowRePassword);
        etFirstName = ViewUtil.findViewById(this, R.id.etFirstName);
        etLastName = ViewUtil.findViewById(this, R.id.etLastName);
        etEmail = ViewUtil.findViewById(this, R.id.etEmail);
        etPassword = ViewUtil.findViewById(this, R.id.etPassword);
        etRePassword = ViewUtil.findViewById(this, R.id.etRePassword);
        btnSignUp = ViewUtil.findViewById(this, R.id.btnSignUp);
        btnLogin = ViewUtil.findViewById(this, R.id.btnLogin);
	}

	private void initListener() {
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
		ViewUtil.setEditorAction(etRePassword, btnSignUp);
		ViewUtil.setEdittextLabelVisibility(etFirstName, tvFirstName);
		ViewUtil.setEdittextLabelVisibility(etLastName, tvLastName);
		ViewUtil.setEdittextLabelVisibility(etEmail, tvEmail);
		ViewUtil.setEdittextLabelVisibility(etPassword, tvPassword);
		ViewUtil.setEdittextLabelVisibility(etRePassword, tvRePassword);
        ViewUtil.setPasswordShow(etRePassword, tvShowRePassword);
        ViewUtil.setPasswordShow(etPassword, tvShowPassword);
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSignUp:
            register();
			break;
		case R.id.btnLogin:
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
			break;
		default:
			break;
		}
	}

    private void register() {
        String firstName = etFirstName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            showHintDialog(R.string.first_name_cannot_be_empty);
            return;
        }
        String lastName = etLastName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            showHintDialog(R.string.last_name_cannot_be_empty);
            return;
        }
        final String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            showHintDialog(R.string.email_cannot_be_empty);
            return;
        }
        if (!Validator.isEmail(email)) {
            showHintDialog(R.string.email_format_error);
            return;
        }
        final String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showHintDialog(R.string.password_cannot_be_empty);
            return;
        }
        String rePassword = etRePassword.getText().toString();
        if (TextUtils.isEmpty(rePassword)) {
            showHintDialog(R.string.re_enter_password_cannot_be_empty);
            return;
        }
        if (!TextUtils.equals(password, rePassword)) {
            showHintDialog(R.string.incorrect_password);
            return;
        }
    }

}
