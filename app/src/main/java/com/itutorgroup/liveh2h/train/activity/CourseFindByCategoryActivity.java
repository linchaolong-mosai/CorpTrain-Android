package com.itutorgroup.liveh2h.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.adpter.CourseFindByCategoryAdapter;
import com.itutorgroup.liveh2h.train.bean.CourseFindByCategory;
import com.itutorgroup.liveh2h.train.bean.CoursesFindByCategory;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.bean.usercourse.UserCourseRoot;
import com.itutorgroup.liveh2h.train.constants.TrackName;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

public class CourseFindByCategoryActivity extends BaseToolbarActivity {
    private ListView listView;
    private List<CourseFindByCategory> courseFindByCategories = new ArrayList<>();
    private CourseFindByCategoryAdapter courseFindByCategoryAdapter;
    private String categoryId;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        initDatas();
        addListener();
        ((TextView) findViewById(R.id.tv_title)).setText(name);
    }

    protected void initDatas() {
        courseFindByCategoryAdapter = new CourseFindByCategoryAdapter(context, courseFindByCategories, R.layout.item_listformat_course_searchresult);
        listView.setAdapter(courseFindByCategoryAdapter);
        categoryId = getIntent().getStringExtra("categoryId");
        name = getIntent().getStringExtra("name");
    }

    protected int setContent() {
        return R.layout.activity_category;
    }

    protected void initView() {
        listView = (ListView) findViewById(R.id.lv);
    }
//    public void onEventMainThread(Event.UpdateViewcount updateViewcount){
//        getDatas();
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    protected void addListener() {
//        EventBus.getDefault().register(this);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Intent intent = new Intent(context, CourseDetailActivity.class);
//                intent.putExtra("course",courseFindByCategories.get(position));
//                startActivity(intent);
                AppAction.getAllUserCoursesByFilter(context, courseFindByCategories.get(position).getSubject(), new HttpResponseHandler(context,UserCourseRoot.class) {
                    @Override
                    public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                        UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                        List<Courses> courses = userCourseRoot.getCourses();
                        for(Courses courses1:courses){
                            if(courses1.getCourseInfo().getCourseId().equals(courseFindByCategories.get(position).getCourseId())){
                                Intent intent = new Intent(context,CourseDetailActivity.class);
                                intent.putExtra("course",courses1);
                                startActivityForResult(intent,0);
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
        });
        getDatas(true);
    }

        private void getDatas(final boolean showTips){
        AppAction.getCourselist(context, categoryId,new HttpResponseHandler(context,CoursesFindByCategory.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                getCoursesUnderCategoryEvent();
                CoursesFindByCategory categories = (CoursesFindByCategory) response;
                    CourseFindByCategoryActivity.this.courseFindByCategories.clear();
                    CourseFindByCategoryActivity.this.courseFindByCategories.addAll(categories.getCourses());
                    courseFindByCategoryAdapter.notifyDataSetChanged();
            }
            @Override
            public void onResponeseStart() {
                if(showTips)
                showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                if(showTips)
                dismissProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                if(showTips)
                showHintDialog(response.message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getDatas(false);
    }

    @Override
    protected String getAnalyticsTrackScreenName() {
        return TrackName.CourseUnderCategoryScreen;
    }

    /****************************************Analytics**************************/
    private void getCoursesUnderCategoryEvent(){
        AnalyticsUtils.setEvent(context,R.array.GetCoursesUnderCategory);
    }
    /****************************************Analytics**************************/
}
