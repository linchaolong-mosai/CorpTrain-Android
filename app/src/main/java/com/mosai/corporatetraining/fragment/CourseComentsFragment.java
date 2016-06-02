package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.activity.CourseDetailActivity;
import com.mosai.corporatetraining.adpter.CourseCommentAdapter;
import com.mosai.corporatetraining.bean.coursecomment.Comments;
import com.mosai.corporatetraining.bean.coursecomment.CourseCommentRoot;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.utils.Tools;

import java.util.ArrayList;
import java.util.List;


public class CourseComentsFragment extends Fragment {
    private CourseCommentAdapter adapter;
    private List<Comments> comments = new ArrayList<>();
    private Context context;
    private EditText etReply;

    private ListView lvComment;
    private View view;
    private Courses course;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        course = (Courses)getArguments().getSerializable("course");
        view = inflater.inflate(R.layout.fragment_course_coments, container, false);
        etReply = ViewUtil.findViewById(view,R.id.et_reply);
        lvComment = ViewUtil.findViewById(view,R.id.lv_comment);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CourseCommentAdapter(context,comments,R.layout.item_listformat_course_comment);
        lvComment.setAdapter(adapter);
        addListener();
        getCommentlist(false);
    }

    private void addListener() {
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etReply.getText().toString())){
                    submitComment(etReply.getText().toString());
                    Tools.hideSoftInput(etReply);
                    etReply.setText("");
                }
            }
        });
        etReply.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEND){
                    view.findViewById(R.id.btn_send).performClick();
                    return true;
                }
                return false;
            }
        });
    }
    private void submitComment(String comment){
        AppAction.submitCourseComment(context, course.getCourseInfo().getCourseId(), comment, new HttpResponseHandler(HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                        getCommentlist(true);
            }

            @Override
            public void onResponeseStart() {
                ((CourseDetailActivity)context).showProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                ((CourseDetailActivity)context).showHintDialog(response.message);
            }

            @Override
            public void onResponesefinish() {
                ((CourseDetailActivity)context).dismissProgressDialog();
            }
        });
    }
    private void getCommentlist(final boolean fresh){
        if(course!=null){
            AppAction.getCommentsByCourseId(context, course.getCourseInfo().getCourseId(), new HttpResponseHandler(CourseCommentRoot.class) {
                @Override
                public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                    CourseCommentRoot courseCommentRoot = (CourseCommentRoot) response;
                    comments.clear();
                    comments.addAll(courseCommentRoot.getComments());
                    adapter.notifyDataSetChanged();
                    if(fresh){
                        lvComment.setSelection(comments.size()-1);
                    }
                }
            });
        }
    }
}
