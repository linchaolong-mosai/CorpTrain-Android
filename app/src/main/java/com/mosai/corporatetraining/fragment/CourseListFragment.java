package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.activity.CourseDetailActivity;
import com.mosai.corporatetraining.adpter.SearchCourseResultAdapter;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.bean.usercourse.UserCourseRoot;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/25 0025 16:30
 * 邮箱：nianbin@mosainet.com
 */
public class CourseListFragment extends Fragment{
    private Context context;
    private View view;
    private SearchCourseResultAdapter adapter;
    private List<Courses> courses = new ArrayList<>();
    private ListView listView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_courselist,container,false);
        listView = ViewUtil.findViewById(view,R.id.lv);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SearchCourseResultAdapter(context, courses, R.layout.item_listformat_course_searchresult);
        listView.setAdapter(adapter);
        getDatas();
        addListener();
    }

    private void addListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("course",courses.get(position));
                startActivity(intent);
            }
        });
    }

    private void getDatas(){
        int type = getArguments().getInt("type",0);
        AppAction.getUserCourseByType(context,type, new HttpResponseHandler(UserCourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                List<Courses> courses = userCourseRoot.getCourses();
                CourseListFragment.this.courses = courses;
                adapter.notifyDataSetChanged();
            }

        });
    }
}
