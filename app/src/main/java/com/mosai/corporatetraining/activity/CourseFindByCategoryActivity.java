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
