package com.itutorgroup.liveh2h.train.fragment;

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

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.activity.CourseDetailActivity;
import com.itutorgroup.liveh2h.train.adpter.SearchCourseResultAdapter;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.bean.usercourse.UserCourseRoot;
import com.itutorgroup.liveh2h.train.comparotor.CreatTimeComparator;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/25 0025 16:30
 * 邮箱：nianbin@mosainet.com
 */
public class CourseListFragment extends BaseFragment{
    private boolean isFirst=true;
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
        if(isFirst){
            view = inflater.inflate(R.layout.view_courselist,container,false);
            listView = ViewUtil.findViewById(view,R.id.lv);
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isFirst){
            adapter = new SearchCourseResultAdapter(context, courses, R.layout.item_listformat_course_searchresult);
            listView.setAdapter(adapter);
            getDatas();
            addListener();
            isFirst=false;
        }
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

    public void getDatas(){

        final int type = getArguments().getInt("type",0);
        AppAction.getUserCourseByType(context,type, new HttpResponseHandler(context,UserCourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                List<Courses> courses = userCourseRoot.getCourses();
                CourseListFragment.this.courses.clear();
                if(type == AppAction.SEARCH_USER_COURSE_FILTER_TYPE_MANDATORY){
                    //Mandatory courses
                    for(Courses courses1:courses){
                        int completePercent = courses1.getAttendeeInfo().getCompletePercent();
                        if (completePercent<100){
                            CourseListFragment.this.courses.add(courses1);
                        }
                    }
                }else if (type == AppAction.SEARCH_USER_COURSE_FILTER_TYPE_FINISHED) {
                    //Completed courses
                    CourseListFragment.this.courses.addAll(courses);
                }
                else if (type == AppAction.SEARCH_COURSE_FILTER_TYPE_ALL){
                    //Enrolled courses
                    for(Courses courses1:courses){
                        int completePercent = courses1.getAttendeeInfo().getCompletePercent();
                        boolean isMandatory = courses1.getInviteeInfo().getMandatory();
                        if(!isMandatory&&(completePercent>0&&completePercent<100)){
                            CourseListFragment.this.courses.add(courses1);
                        }
                    }
                }
                Collections.sort(CourseListFragment.this.courses,new CreatTimeComparator());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
