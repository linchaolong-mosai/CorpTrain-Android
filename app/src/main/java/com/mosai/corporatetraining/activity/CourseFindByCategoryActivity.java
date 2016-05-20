package com.mosai.corporatetraining.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.adpter.CourseFindByCategoryAdapter;
import com.mosai.corporatetraining.bean.CourseFindByCategory;
import com.mosai.corporatetraining.bean.CoursesFindByCategory;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;

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

    protected void addListener() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                back();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(context, CourseFindByCategoryActivity.class);
//                intent.putExtra("courseId", courseFindByCategories.get(position).getCourseId());
//                startActivity(intent);
            }
        });
        getDatas();
    }

        private void getDatas(){
        AppAction.getCourselist(context, categoryId,new HttpResponseHandler(CoursesFindByCategory.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                CoursesFindByCategory categories = (CoursesFindByCategory) response;
                    CourseFindByCategoryActivity.this.courseFindByCategories.clear();
                    CourseFindByCategoryActivity.this.courseFindByCategories.addAll(categories.getCourses());
                    courseFindByCategoryAdapter.notifyDataSetChanged();
                    dismissProgressDialog();
            }
            @Override
            public void onStart() {
                showProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                showHintDialog(response.message.toString());
                dismissProgressDialog();

            }
        });
    }
//    private void getDatas() {
//        AppAction.getCourselist(context, categoryId, new HttpResponseHandler(HttpResponse.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
////            CoursesFindByCategory categories = (CoursesFindByCategory) response;
////            CourseFindByCategoryActivity.this.courseFindByCategories.clear();
////            CourseFindByCategoryActivity.this.courseFindByCategories.addAll(categories.getCourses());
//                List<CourseFindByCategory> lists = new ArrayList<CourseFindByCategory>();
//                try {
//                    JSONObject jsonObject = new JSONObject(responseString);
//                    JSONArray courses = jsonObject.getJSONArray("courses");
//                    for (int i = 0; i < courses.length(); i++) {
//                        JSONObject course = courses.getJSONObject(i);
//                        CourseFindByCategory courseFindByCategory = new CourseFindByCategory();
//                        courseFindByCategory.setCourseId(course.optString("courseId"));
//                        courseFindByCategory.setCreatorId(course.optString("creatorId"));
//                        courseFindByCategory.setCtCompanyId(course.optString("ctCompanyId"));
//                        courseFindByCategory.setSubject(course.optString("subject"));
//                        courseFindByCategory.setCompany(course.optString("company"));
//                        courseFindByCategory.setPublished(course.optBoolean("published"));
//                        courseFindByCategory.setArchived(course.optBoolean("archived"));
//                        courseFindByCategory.setShareType(course.optInt("shareType"));
//                        courseFindByCategory.setViewCount(course.optInt("viewCount"));
//                        courseFindByCategory.setDescription(course.optString("description"));
//                        courseFindByCategory.setImageVersion(course.optInt("imageVersion"));
//                        courseFindByCategory.setImageName(course.optString("imageName"));
//                        courseFindByCategory.setRating(course.optInt("rating"));
//                        //"createTime": 1460210088790,
//                        courseFindByCategory.setUpdateTime(course.optInt("updateTime"));
//                        // "publishTime": 1461671086630,
//                        courseFindByCategory.setDuration(course.optInt("duration"));
//                        lists.add(courseFindByCategory);
//                    }
//                    CourseFindByCategoryActivity.this.courseFindByCategories.clear();
//                    CourseFindByCategoryActivity.this.courseFindByCategories.addAll(lists);
//                    courseFindByCategoryAdapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                courseFindByCategoryAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onStart() {
//                showProgressDialog();
//            }
//
//            @Override
//            public void onResponeseFail(int statusCode, HttpResponse response) {
//                showHintDialog(response.message.toString());
//                dismissProgressDialog();
//
//            }
//        });
//    }
}
