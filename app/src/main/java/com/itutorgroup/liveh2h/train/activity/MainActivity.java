package com.itutorgroup.liveh2h.train.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.fragment.DiscoverFragment;
import com.itutorgroup.liveh2h.train.fragment.MeFragment;
import com.itutorgroup.liveh2h.train.fragment.MyCoursesFragment;
import com.itutorgroup.liveh2h.train.util.LogUtils;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.utils.ToastUtils;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseToolbarActivity {
    private Fragment currentFragment;
    private ImageView ivDiscover, ivLearn, ivMe;
    private TextView tvDisicover, tvLearn, tvMe;
    private LinearLayout llDiscover, llLearn, llMe;

    @Override
    protected boolean openTokenExpireBroadcast() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListener();
        /////////动态注册广播
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
        EventBus.getDefault().register(this);
    }
    public void onEventMainThread(boolean flag){
        
    }
    private void initBottomlayout() {
        llDiscover = (LinearLayout) findViewById(R.id.ll_discover);
        llLearn = (LinearLayout) findViewById(R.id.ll_learn);
        llMe = (LinearLayout) findViewById(R.id.ll_me);
        ivDiscover = (ImageView) findViewById(R.id.iv_discover);
        ivLearn = (ImageView) findViewById(R.id.iv_learn);
        ivMe = (ImageView) findViewById(R.id.iv_me);
        tvDisicover = (TextView) findViewById(R.id.tv_discover);
        tvLearn = (TextView) findViewById(R.id.tv_learn);
        tvMe = (TextView) findViewById(R.id.tv_me);
        addBottomListener();
        llDiscover.performClick();
    }
    private void changeSelect(){
        ivDiscover.setSelected(false);
        ivLearn.setSelected(false);
        ivMe.setSelected(false);
        tvDisicover.setSelected(false);
        tvLearn.setSelected(false);
        tvMe.setSelected(false);
    }
    private void addBottomListener(){
        llDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect();
                ViewUtil.setStatusBarBg(((Activity)context), getResources().getColor(R.color.colorPrimaryDark));
                tvDisicover.setSelected(true);
                ivDiscover.setSelected(true);
                show(DiscoverFragment.class.getSimpleName());
            }
        });
        llLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect();
                ViewUtil.setStatusBarBg(((Activity)context), getResources().getColor(R.color.colorPrimaryDark));
                tvLearn.setSelected(true);
                ivLearn.setSelected(true);
                show(MyCoursesFragment.class.getSimpleName());
            }
        });
        llMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect();
                ViewUtil.setStatusBarBg(((Activity)context), getResources().getColor(R.color.colorPrimary));
                tvMe.setSelected(true);
                ivMe.setSelected(true);
                show(MeFragment.class.getSimpleName());
            }
        });
    }

    private void initViews() {
        initBottomlayout();
    }

    private void initListener() {

//        addBottomListener();
    }

    private void initData() {
        show(DiscoverFragment.class.getSimpleName());
    }

    private void show(String tag) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment == null) {
            if (TextUtils.equals(tag, DiscoverFragment.class.getSimpleName())) {
                fragment = DiscoverFragment.newInstance();
            } else if (TextUtils.equals(tag, MyCoursesFragment.class.getSimpleName())) {
                fragment = MyCoursesFragment.newInstance();
            } else {
                fragment = MeFragment.newInstance();
            }
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.container, fragment, tag);
        } else {
            transaction.hide(currentFragment).show(fragment);
        }
        transaction.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        this.menu = menu;
//        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    private long lastKeyBackTime = 0;
    @Override
    public void onBackPressed() {
        long nowtime = SystemClock.elapsedRealtime();
        if (lastKeyBackTime == 0 || nowtime - lastKeyBackTime > 1500) {
            ToastUtils.showToast(this,getString(R.string.press_exit_message));
            lastKeyBackTime = nowtime;
        } else {
            lastKeyBackTime = 0;
            finish();
//            moveTaskToBack(true);
        }
    }
    /////////////监听网络状态变化的广播接收器
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    public BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.e("net changed");
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable()) {
                    Event.NetChange netChange = new Event.NetChange();
                    netChange.netChange=true;
                    EventBus.getDefault().post(netChange);
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        /////////解除广播
        if(myNetReceiver!=null){
            unregisterReceiver(myNetReceiver);
        }
    }
}
