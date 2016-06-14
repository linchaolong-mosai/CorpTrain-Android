package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.activity.ClassResourceActivity;
import com.mosai.corporatetraining.adpter.ClassAdapter;
import com.mosai.corporatetraining.bean.classesforcourse.Classes;
import com.mosai.corporatetraining.bean.classesforcourse.ClassesRoot;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;


public class CourseDetailsFragment extends Fragment {
    private List<Classes> classes = new ArrayList<>();
    private ClassAdapter adapter;
    private Courses courses;
    private Context context;
    private TextView tvSubject,tvDec;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    private Button btnJoinCourse;
    private View view;
    private ListView lv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_course_details, container, false);
        btnJoinCourse = ViewUtil.findViewById(view,R.id.btn_joincourse);
        tvDec = ViewUtil.findViewById(view,R.id.tv_description);
        tvSubject = ViewUtil.findViewById(view,R.id.tv_title);
        lv = ViewUtil.findViewById(view,R.id.lv);
        courses = (Courses)getArguments().getSerializable("course");
         return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSubject.setText(courses.getCourseInfo().getSubject());
        tvDec.setText(courses.getCourseInfo().getDescription());
        adapter = new ClassAdapter(context,classes,R.layout.item_listformat_class);
        lv.setAdapter(adapter);
        addListener();
        getClasses();

    }
    private void addListener(){
        if(!courses.getInviteeInfo().getMandatory()&&courses.getAttendeeInfo().getCompletePercent()==0){
            btnJoinCourse.setVisibility(View.VISIBLE);
        }else{
            btnJoinCourse.setVisibility(View.GONE);
        }
        btnJoinCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinCourse();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes classe = classes.get(position);
                Intent intent = new Intent(context, ClassResourceActivity.class);
                intent.putExtra("classes",classe);
                intent.putExtra("courses",courses);
                startActivity(intent);
            }
        });

    }
    public void onEventMainThread(boolean flag)
    {
//        enrolled.getDatas();

    }
    private void updateMycourse(){
        EventBus.getDefault().post(courses);
    }
    MaterialDialog dialog;
    private void joinCourse(){
        AppAction.joinCourse(context, courses.getCourseInfo().getCourseId(), new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                dialog = new MaterialDialog(context)
//                        .setTitle("Tips")
                        .setMessage(context.getResources().getString(R.string.enrolled_success))
                        .setCanceledOnTouchOutside(false)
                        .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    dialog.show();
                    btnJoinCourse.setVisibility(View.GONE);
                updateMycourse();
            }

        });
    }
    private void getClasses(){
        AppAction.getClassesByCourseId(context, courses.getCourseInfo().getCourseId(), new HttpResponseHandler(context,ClassesRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                ClassesRoot classesRoot = (ClassesRoot) response;
                classes.clear();
                classes.addAll(classesRoot.getClasses());
                getClassesPercent();
//                adapter.notifyDataSetChanged();
            }
        });
    }
    private void getClassesPercent(){
        AppAction.getClassesPercentByCourseId(context, courses.getCourseInfo().getCourseId(), new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject ids = jsonObject.optJSONObject("completed_percent_list");
                    Iterator<String> keys = ids.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        for(Classes classes1:classes){
                            if(TextUtils.equals(key,classes1.getClassInfo().getClassId())){
                                classes1.getClassInfo().percent = ids.optInt(key);
                                continue;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
