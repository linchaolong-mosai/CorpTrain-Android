package com.itutorgroup.liveh2h.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.constants.TrackName;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                Intent intent = new Intent();
                String email = UserPF.getInstance().getString(UserPF.USER_EMAIL, "");
                String password = UserPF.getInstance().getString(UserPF.PASSWORD, "");
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    intent.setClass(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent.setClass(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1500);
        launchAppEvent();
    }

    /****************************************Analytics**************************/
    private void launchAppEvent(){
        AnalyticsUtils.setEvent(context,R.array.LaunchApp);
    }
    /****************************************Analytics**************************/
}
