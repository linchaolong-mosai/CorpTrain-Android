package com.mosai.corporatetraining.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.util.ViewUtil;

/**
 * login input
 * @author Rays 2016年4月11日
 *
 */
public class SelectLoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btnLogin, btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login);
		initViews();
		initListener();
		initData();
	}

	private void initViews() {
        btnLogin = ViewUtil.findViewById(this, R.id.btnLogin);
        btnRegister = ViewUtil.findViewById(this, R.id.btnRegister);
	}

	private void initListener() {
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
            startActivity(new Intent(context, LoginActivity.class));
			break;
		case R.id.btnRegister:
			startActivity(new Intent(context, RegisterActivity.class));
			break;
		default:
			break;
		}
	}

}
