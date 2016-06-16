package com.itutorgroup.liveh2h.train.activity;

import android.support.v7.widget.Toolbar;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.util.ViewUtil;

/**
 * Created by Rays on 16/5/11.
 */
public class BaseToolbarActivity extends BaseActivity {
    protected Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbat();
    }

    protected void initToolbat() {
        toolbar = ViewUtil.findViewById(this, R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}
