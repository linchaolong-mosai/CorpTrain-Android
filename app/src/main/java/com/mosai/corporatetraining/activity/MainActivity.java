package com.mosai.corporatetraining.activity;

import android.app.Activity;
import android.os.Bundle;
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

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.fragment.DiscoverFragment;
import com.mosai.corporatetraining.fragment.MeFragment;
import com.mosai.corporatetraining.fragment.MyCoursesFragment;
import com.mosai.corporatetraining.util.ViewUtil;

public class MainActivity extends BaseToolbarActivity {
    private Menu menu;
    private Fragment currentFragment;
    private ImageView ivDiscover, ivLearn, ivMe;
    private TextView tvDisicover, tvLearn, tvMe;
    private LinearLayout llDiscover, llLearn, llMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initTabLayout();
        initViews();
        initListener();
//        initData();
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
//                getSupportActionBar().setTitle(R.string.discover);
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
//                getSupportActionBar().setTitle(R.string.my_courses);
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
//                getSupportActionBar().setTitle(R.string.me);
                show(MeFragment.class.getSimpleName());
            }
        });
    }
//    private void initTabLayout() {
//        TabLayout tabLayout = ViewUtil.findViewById(this, R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setTag(0).setIcon(R.drawable.main_tab_discover_selector));
//        tabLayout.addTab(tabLayout.newTab().setTag(1).setIcon(R.drawable.main_tab_learn_selector));
//        tabLayout.addTab(tabLayout.newTab().setTag(2).setIcon(R.drawable.main_tab_me_selector));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                switch ((int) tab.getTag()) {
//                    case 1:
//                        menu.findItem(R.id.search).setVisible(false);
//                        menu.findItem(R.id.category).setVisible(false);
//                        menu.findItem(R.id.calendar).setVisible(true);
//                        menu.findItem(R.id.dropCourse).setVisible(true);
//                        getSupportActionBar().setTitle(R.string.my_courses);
//                        show(MyCoursesFragment.class.getSimpleName());
//
//                        break;
//                    case 2:
//                        menu.findItem(R.id.search).setVisible(false);
//                        menu.findItem(R.id.category).setVisible(false);
//                        menu.findItem(R.id.calendar).setVisible(false);
//                        menu.findItem(R.id.dropCourse).setVisible(false);
//                        getSupportActionBar().setTitle(R.string.me);
//                        show(MeFragment.class.getSimpleName());
//                        break;
//                    default:
//                        menu.findItem(R.id.dropCourse).setVisible(false);
//                        menu.findItem(R.id.calendar).setVisible(false);
//                        menu.findItem(R.id.search).setVisible(true);
//                        menu.findItem(R.id.category).setVisible(true);
//                        getSupportActionBar().setTitle(R.string.discover);
//                        show(DiscoverFragment.class.getSimpleName());
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }

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
}
