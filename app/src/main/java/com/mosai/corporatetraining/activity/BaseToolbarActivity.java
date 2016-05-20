package com.mosai.corporatetraining.activity;

import android.support.v7.widget.Toolbar;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.util.ViewUtil;

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
