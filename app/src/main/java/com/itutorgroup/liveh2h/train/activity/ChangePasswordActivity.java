package com.itutorgroup.liveh2h.train.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.constants.TrackName;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.entity.UserInfoResponse;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.network.progress.DefaultProgressIndicator;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.utils.ToastUtils;

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
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
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
        if(!currentPassword.equals(UserPF.getInstance().getString(UserPF.PASSWORD,""))){
            showHintDialog(R.string.incorrect_current_password);
            return;
        }


        if(newPassword.length()>20||newPassword.length()<5){
            showHintDialog(R.string.password_format);
            return;
        }
        AppAction.changePassword(context, currentPassword, newPassword, new HttpResponseHandler(context,HttpResponse.class, DefaultProgressIndicator.newInstance(context)) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                changePasswordEvent();
                UserPF.getInstance().putString(UserPF.PASSWORD,etNewPassword.getText().toString());
                loginTogetApiToken(UserPF.getInstance().getString(UserPF.USER_EMAIL,""),UserPF.getInstance().getString(UserPF.PASSWORD,""));
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
    private void loginTogetApiToken(String email,String password){
        AppAction.login(context, email, password, new HttpResponseHandler(context,UserInfoResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                UserPF.getInstance().saveUserInfo(userInfoResponse);
                UserPF.getInstance().putString(UserPF.PASSWORD,etNewPassword.getText().toString());
                ToastUtils.showToast(context,getString(R.string.change_password_success));
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
/****************************************Analytics**************************/
    @Override
    public String getAnalyticsTrackName() {
        return TrackName.ChangePasswordScreen;
    }
    private void changePasswordEvent(){
        AnalyticsUtils.setEvent(context,R.array.ChangePassword);
    }
    /****************************************Analytics**************************/
}
