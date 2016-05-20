package com.mosai.corporatetraining.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.local.UserPF;

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
                if (UserPF.getInstance().getBoolean(UserPF.IS_LOGIN, false)) {
                    intent.setClass(context, MainActivity.class);
                } else {
                    intent.setClass(context, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
