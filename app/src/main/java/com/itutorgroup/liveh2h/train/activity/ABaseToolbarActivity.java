package com.itutorgroup.liveh2h.train.activity;

import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/19 0019 13:38
 * 邮箱：zhounianbin@mastercom.cn
 */
public abstract class ABaseToolbarActivity extends BaseToolbarActivity{
    protected void beforeSetContent(){


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContent();
        setContentView(setContent());
        ButterKnife.bind(this);
        initView();
        initDatas();
        addListener();
    }

    protected abstract void initDatas();
    protected abstract int setContent();
    protected abstract void initView();
    protected abstract void addListener();
}
