package com.mosai.corporatetraining.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.CourseRoot;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.fragment.CourseComentsFragment;
import com.mosai.corporatetraining.fragment.CourseDetailsFragment;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.ui.StarRatingDialog;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.ui.CantScrollViewPager;
import com.mosai.ui.SegmentedControlView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class CourseDetailActivity extends ABaseToolbarActivity implements SegmentedControlView.OnSelectionChangedListener {
    private DisplayImageOptions options;
    private ImageView ivIcon;
    private CourseDetailsFragment courseDetailsFragment;
    private CourseComentsFragment courseComentsFragment;
    private SegmentedControlView scv;
    private CantScrollViewPager viewPager;
    private final static int COUNT = 2;
    private final static int ONE = 0;
    private final static int TWO = 1;
    private Courses course;
    private RatingBar ratingBar;
    private TextView tvTitle;
    @Override
    protected void initDatas() {
        ratingBar.setRating(course.getCourseInfo().getRating());
        tvTitle.setText(course.getCourseInfo().getSubject());
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.bg_course_default_cover)
                .showImageOnFail(R.drawable.bg_course_default_cover)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
        String imgurl = String.format("%s%s/%s", AppAction.IMG_RESOURSE_COURSE_URL, course.getCourseInfo().getCourseId(), course.getCourseInfo().getImageName());
        ImageLoader.getInstance().displayImage(imgurl, ivIcon);
        getCourseByCourseId();
    }

    @Override
    protected int setContent() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void initView() {
        course = (Courses) getIntent().getSerializableExtra("course");
//        mycourse = getIntent().getBooleanExtra("mycourse",false);
        starRatingDialog = new StarRatingDialog(context);
        ivIcon = ViewUtil.findViewById(this, R.id.iv_icon);

        Bundle bundle = new Bundle();
        courseDetailsFragment = new CourseDetailsFragment();
        bundle.putInt("tag", ONE);
        bundle.putSerializable("course",course);
//        bundle.putBoolean("mycourse",mycourse);
        courseDetailsFragment.setArguments(bundle);

        courseComentsFragment = new CourseComentsFragment();
        bundle = new Bundle();
        bundle.putInt("tag", TWO);
        bundle.putSerializable("course",course);

        courseComentsFragment.setArguments(bundle);
        viewPager = (CantScrollViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        scv = (SegmentedControlView) findViewById(R.id.scv);
        scv.setColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));
        scv.setEqualWidth(true);
        scv.setStretch(true);

        try {
            scv.setItems(new String[]{getString(R.string.course_details), getString(R.string.course_comments)}, new String[]{"one", "two"});
            scv.setDefaultSelection(0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ratingBar = ViewUtil.findViewById(this,R.id.ratingbar);
        tvTitle = ViewUtil.findViewById(this,R.id.tv_title);
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
        scv.setOnSelectionChangedListener(this);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        starRatingDialog.setOnSubmitCallback(new StarRatingDialog.onSubmitCallback() {
            @Override
            public void submitCallback(float rating) {
//                ToastUtils.showToast(context,rating+"");
//                ratingBar.setRating(rating);
                submitRating(rating);
//                course.getCourseInfo().setRating((int) rating);




            }
        });
    }
    private void submitRating(final float rating){
        AppAction.submitCourseRating(this, course.getCourseInfo().getCourseId(), rating, new HttpResponseHandler(HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                ratingBar.setRating(rating);
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
    @Override
    public void newSelection(String identifier, String value) {
        // TODO Auto-generated method stub
        if ("one".equals(value)) {
            viewPager.setCurrentItem(ONE);
        } else if ("two".equals(value)) {
            viewPager.setCurrentItem(TWO);
        }
    }

    private StarRatingDialog starRatingDialog;

    public void rating(View view) {
        starRatingDialog.show();
    }

    private void getCourseByCourseId(){

        AppAction.getCourseByCourseId(this, course.getCourseInfo().getCourseId(), new HttpResponseHandler(CourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                CourseRoot courseRoot = (CourseRoot) response;
                    ratingBar.setRating(courseRoot.getCourse().getRating());
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

}
