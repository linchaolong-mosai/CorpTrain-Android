package com.itutorgroup.liveh2h.train.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.SelectedCallback;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.bean.usercourse.UserCourseRoot;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.fragment.CourseComentsFragment;
import com.itutorgroup.liveh2h.train.fragment.CourseDetailsFragment;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.ui.CantScrollViewPager;

import java.util.List;

import butterknife.BindView;

public class CourseDetailActivity extends ABaseToolbarActivity implements SelectedCallback {
    private CourseDetailsFragment courseDetailsFragment;
    private CourseComentsFragment courseComentsFragment;

    private CantScrollViewPager viewPager;
    private final static int COUNT = 2;
    private final static int ONE = 0;
    private final static int TWO = 1;
    private Courses course;
    private TextView tvTitle;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @Override
    protected void initDatas() {
        tvTitle.setText(course.getCourseInfo().getSubject());
        getCourseByName();
    }

    @Override
    protected int setContent() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void initView() {
        course = (Courses) getIntent().getSerializableExtra("course");
        Bundle bundle = new Bundle();
        courseDetailsFragment = new CourseDetailsFragment();
        bundle.putInt("tag", ONE);
        bundle.putSerializable("course",course);
        courseDetailsFragment.setArguments(bundle);

        courseComentsFragment = new CourseComentsFragment();
        bundle = new Bundle();
        bundle.putInt("tag", TWO);
        bundle.putSerializable("course",course);

        courseComentsFragment.setArguments(bundle);
        viewPager = (CantScrollViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tvTitle = ViewUtil.findViewById(this,R.id.tv_title);
    }

    @Override
    public void callback(int postion) {
            viewPager.setCurrentItem(postion,false);
    }


    private class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case ONE:
                    return courseDetailsFragment;
                case TWO:
                    return courseComentsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return COUNT;
        }

    }

    @Override
    protected void addListener() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppAction.updateCourseFavorite(context,course.getCourseInfo().getCourseId(),!ivFavorite.isSelected(),new HttpResponseHandler(context,HttpResponse.class) {
                    @Override
                    public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                        ivFavorite.setSelected(!ivFavorite.isSelected());
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
        });
        viewPager.setCurrentItem(0);
    }







    private void getCourseByName(){

        AppAction.getAllUserCoursesByFilter(context, course.getCourseInfo().getSubject(), new HttpResponseHandler(context,UserCourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                List<Courses> courses = userCourseRoot.getCourses();
                for(Courses courses1:courses){
                    if(courses1.getCourseInfo().getCourseId().equals(course.getCourseInfo().getCourseId())){
//                        ratingBar.setRating(courses1.getCourseInfo().getRating());
                        courseComentsFragment.ratingBar.setRating(courses1.getCourseInfo().getRating());
                        courseDetailsFragment.ratingBar.setRating(courses1.getCourseInfo().getRating());
                        ivFavorite.setSelected(courses1.getAttendeeInfo().getFavorite());
                        break;
                    }
                }
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
                showHintDialog(response.message.toString());


            }
        });
    }

}
