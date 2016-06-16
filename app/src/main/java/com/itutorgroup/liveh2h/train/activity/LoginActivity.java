package com.itutorgroup.liveh2h.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.entity.CurrentCtUserResponse;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.entity.UserInfoResponse;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.Validator;
import com.itutorgroup.liveh2h.train.util.ViewUtil;

/**
 * login input
 * @author Rays 2016年4月11日
 *
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
	private TextView tvEmail, tvPassword, tvShow;
	private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
		initViews();
		initListener();
		initData();
	}

	private void initViews() {
		tvEmail = ViewUtil.findViewById(this, R.id.tvEmail);
		tvPassword = ViewUtil.findViewById(this, R.id.tvPassword);
        btnLogin = ViewUtil.findViewById(this, R.id.btnLogin);
        btnRegister = ViewUtil.findViewById(this, R.id.btnRegister);
		etEmail = ViewUtil.findViewById(this, R.id.etEmail);
		etPassword = ViewUtil.findViewById(this, R.id.etPassword);
        tvShow = ViewUtil.findViewById(this, R.id.tvShow);
	}

	private void initListener() {
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
        tvShow.setOnClickListener(this);
		ViewUtil.setEditorAction(etPassword, btnLogin);
		ViewUtil.setEdittextLabelVisibility(etEmail, tvEmail);
		ViewUtil.setEdittextLabelVisibility(etPassword, tvPassword);
//        etPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String text;
//                if (s.length() == 0) {
//                    text = getString(R.string.help);
//                } else {
//                    Object obj = tvShow.getTag(tvShow.getId());
//                    if (obj == null || ((boolean) obj)) {
//                        text = getString(R.string.show);
//                        tvShow.setTag(tvShow.getId(), true);
//                    } else {
//                        text = getString(R.string.hide);
//                        tvShow.setTag(tvShow.getId(), false);
//                    }
//                }
//                tvShow.setText(text);
//                tvShow.setTag(text);
//            }
//        });
	}

	private void initData() {
//        etEmail.setText("ctuser@whatever.com");
//        etPassword.setText("tutormeet1");
		String email = UserPF.getInstance().getString(UserPF.USER_EMAIL, "");
        String password = UserPF.getInstance().getString(UserPF.PASSWORD, "");
		if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
			etEmail.setText(email);
            etPassword.setText(password);
            login();
//			etPassword.requestFocus();
		}
        if(!TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            etEmail.setText(email);
            etPassword.setText(password);
        }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			login();
			break;
		case R.id.btnRegister:
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
        case R.id.tvShow:
            showHandler();
            break;
		default:
			break;
		}
	}

    private void showHandler() {
        if (TextUtils.equals((CharSequence) tvShow.getTag(), getString(R.string.help))) {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            intent.putExtra("email", etEmail.getText().toString());
            startActivity(intent);
        }
//        else {
//            if ((boolean) tvShow.getTag(tvShow.getId())) {
//                tvShow.setText(R.string.hide);
//                tvShow.setTag(tvShow.getId(), false);
//                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            } else {
//                tvShow.setText(R.string.show);
//                tvShow.setTag(tvShow.getId(), true);
//                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            }
//            etPassword.setSelection(etPassword.length());
//        }
    }

    private void login() {
		String email = etEmail.getText().toString();
		if (TextUtils.isEmpty(email)) {
			showHintDialog(R.string.email_cannot_be_empty);
			return;
		}
		if (!Validator.isEmail(email)) {
			showHintDialog(R.string.email_format_error);
			return;
		}
		String password = etPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			showHintDialog(R.string.password_cannot_be_empty);
			return;
		}
		AppAction.login(context, email, password, new HttpResponseHandler(context,UserInfoResponse.class) {
			@Override
			public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
				UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                userInfoResponse.password = etPassword.getText().toString();
				UserPF.getInstance().saveUserInfo(userInfoResponse);
                getCurrentCtUser();
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

    private void getCurrentCtUser() {
        AppAction.getCurrentCtUser(context, new HttpResponseHandler(context,CurrentCtUserResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                CurrentCtUserResponse currentCtUserResponse = (CurrentCtUserResponse) response;
                UserPF.getInstance().saveCtUser(currentCtUserResponse);
//                startActivity(new Intent(context, MainActivity.class));
//				finish();
				startActivity(new Intent(context, MainActivity.class));
				finish();
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
