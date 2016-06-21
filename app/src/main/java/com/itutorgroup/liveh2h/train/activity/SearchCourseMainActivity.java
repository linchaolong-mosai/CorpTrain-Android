package com.itutorgroup.liveh2h.train.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.adpter.SearchCourseResultAdapter;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.bean.usercourse.UserCourseRoot;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.ui.ClearEditText;
import com.mosai.utils.ToastUtils;
import com.mosai.utils.Tools;

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
                startActivityForResult(intent,0);
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
                filter(cetCourses.getText().toString());
            }
        });
        cetCourses.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    //过滤
                    filter(v.getText().toString());
                    Tools.hideSoftInput(cetCourses);
                    return true;
                }
                return false;
            }
        });
        getDatas(true);

    }

    private void filter(String filter) {
        coursesTemp.clear();
        if (!TextUtils.isEmpty(filter)) {
            for (Courses course : courses) {
                if (course.getCourseInfo().getSubject().contains(filter)) {
                    coursesTemp.add(course);
                }
            }
        }else{
            Tools.hideSoftInput(cetCourses);
        }
        adapter.notifyDataSetChanged();
        if(coursesTemp.size()==0&&!TextUtils.isEmpty(cetCourses.getText().toString())){
            ToastUtils.showToast(context,getString(R.string.search_noresult));
        }
    }

    private void getDatas(final boolean showTips) {
        AppAction.getUserCourses(this, new HttpResponseHandler(context,UserCourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                List<Courses> courses = userCourseRoot.getCourses();
                SearchCourseMainActivity.this.courses.clear();
                SearchCourseMainActivity.this.courses.addAll(courses);
                Tools.showSoftInput(cetCourses);
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
        filter(cetCourses.getText().toString());
    }
}
