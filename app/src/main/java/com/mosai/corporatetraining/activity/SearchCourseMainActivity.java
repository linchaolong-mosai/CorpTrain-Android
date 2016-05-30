package com.mosai.corporatetraining.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.adpter.SearchCourseResultAdapter;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.bean.usercourse.UserCourseRoot;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.ui.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/24 0024 13:07
 * 邮箱：zhounianbin@mastercom.cn
 */
public class SearchCourseMainActivity extends ABaseToolbarActivity {
    private SearchCourseResultAdapter adapter;
    private List<Courses> courses = new ArrayList<>();
    private ListView listView;
    private ClearEditText cetCourses;
    private List<Courses> coursesTemp = new ArrayList<>();

    @Override
    protected void initDatas() {
        adapter = new SearchCourseResultAdapter(context, coursesTemp, R.layout.item_listformat_course_searchresult);
        listView.setAdapter(adapter);
    }

    @Override
    protected int setContent() {
        return R.layout.activity_searchcourses_main;
    }

    @Override
    protected void initView() {
        cetCourses = ViewUtil.findViewById(this, R.id.cet_search);
        listView = (ListView) findViewById(R.id.lv);
    }

    @Override
    protected void addListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("course", coursesTemp.get(position));
                startActivity(intent);
            }
        });
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        cetCourses.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
//                filter(cetCourses.getText().toString());
            }
        });
//        cetCourses.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    //过滤
//                    filter(v.getText().toString());
//                    Tools.hideSoftInput(cetCourses);
//                    return true;
//                }
//                return false;
//            }
//        });
        getDatas();
    }

    private void filter(String filter) {
        coursesTemp.clear();
        if (!TextUtils.isEmpty(filter)) {
            for (Courses course : courses) {
                if (course.getCourseInfo().getSubject().contains(filter)) {
                    coursesTemp.add(course);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void getDatas() {
        AppAction.getUserCourses(this, new HttpResponseHandler(UserCourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                List<Courses> courses = userCourseRoot.getCourses();
                SearchCourseMainActivity.this.courses = courses;
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
}
