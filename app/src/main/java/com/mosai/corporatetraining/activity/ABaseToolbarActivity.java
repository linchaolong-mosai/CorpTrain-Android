package com.mosai.corporatetraining.activity;

import android.os.Bundle;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/19 0019 13:38
 * 邮箱：zhounianbin@mastercom.cn
 */
public abstract class ABaseToolbarActivity extends BaseToolbarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContent());
        initView();
        initDatas();
        addListener();
    }

    protected abstract void initDatas();
    protected abstract int setContent();
    protected abstract void initView();
    protected abstract void addListener();
}
