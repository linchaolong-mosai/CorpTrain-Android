package com.itutorgroup.liveh2h.train.fragment;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.SelectedCallback;
import com.itutorgroup.liveh2h.train.activity.CourseDetailActivity;
import com.itutorgroup.liveh2h.train.adpter.CourseCommentAdapter;
import com.itutorgroup.liveh2h.train.bean.coursecomment.Comments;
import com.itutorgroup.liveh2h.train.bean.coursecomment.CourseCommentRoot;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.ui.RatingDialog;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.ui.SegmentedControlView;
import com.mosai.utils.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class CourseComentsFragment extends Fragment implements SegmentedControlView.OnSelectionChangedListener{
    private CourseCommentAdapter adapter;
    private List<Comments> comments = new ArrayList<>();
    private Context context;
    private EditText etReply;
    public RatingBar ratingBar;
    private ListView lvComment;
    private View view;
    private Courses course;
    private ImageView ivIcon;
    private SegmentedControlView scv;
    private SelectedCallback selectedCallback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof SelectedCallback){
            selectedCallback = (SelectedCallback) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        course = (Courses)getArguments().getSerializable("course");
        view = inflater.inflate(R.layout.fragment_course_coments, container, false);
        etReply = ViewUtil.findViewById(view,R.id.et_reply);
        lvComment = ViewUtil.findViewById(view,R.id.lv_comment);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
        ivIcon = ViewUtil.findViewById(view, R.id.iv_icon);
        ratingDialog = new RatingDialog(context);
//        lvComment.findViewById(R.id.tv_title).setVisibility(View.GONE);
//        lvComment.findViewById(R.id.tv_description).setVisibility(View.GONE);
        scv = (SegmentedControlView) view.findViewById(R.id.scv);
        scv.setColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));
        scv.setEqualWidth(true);
        scv.setStretch(true);

        try {
            scv.setItems(new String[]{getString(R.string.course_details), getString(R.string.course_comments)}, new String[]{"one", "two"});
            scv.setDefaultSelection(1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return view;
    }
    private DisplayImageOptions options;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CourseCommentAdapter(context,comments,R.layout.item_listformat_course_comment);
        lvComment.setAdapter(adapter);
        ratingBar.setRating(course.getCourseInfo().getRating());
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.bg_course_default_cover)
                .showImageOnFail(R.drawable.bg_course_default_cover)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
        String imgurl = String.format("%s%s/%s", AppAction.IMG_RESOURSE_COURSE_URL, course.getCourseInfo().getCourseId(), course.getCourseInfo().getImageName());
        ImageLoader.getInstance().displayImage(imgurl, ivIcon);
        addListener();
        getCommentlist(false);
    }

    private void addListener() {
        scv.setOnSelectionChangedListener(this);
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
        view.findViewById(R.id.rl_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            rating();
            }
        });
        ratingDialog.setCallback(new RatingDialog.Callback() {
            @Override
            public void callback(float rate) {
                submitRating(rate);
            }
        });
    }
    private void submitComment(String comment){
        AppAction.submitCourseComment(context, course.getCourseInfo().getCourseId(), comment, new HttpResponseHandler(context,HttpResponse.class) {
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
            AppAction.getCommentsByCourseId(context, course.getCourseInfo().getCourseId(), new HttpResponseHandler(context,CourseCommentRoot.class) {
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
    private RatingDialog ratingDialog;
    public void rating() {
        if(!ratingDialog.isShowing()){

            ratingDialog.show();
        }
    }
    private void submitRating(final float rating){
        AppAction.submitCourseRating(context, course.getCourseInfo().getCourseId(), rating, new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                ratingBar.setRating(rating);
                Event.SubmitRate submitRate = new Event.SubmitRate();
                submitRate.rate = rating;
                EventBus.getDefault().post(submitRate);
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
    public void onEventMainThread(Event.SubmitRate submitRate){
        ratingBar.setRating(submitRate.rate);
    }

    @Override
    public void newSelection(String identifier, String value) {
        if ("one".equals(value)) {
            selectedCallback.callback(0);
            try {
                scv.setDefaultSelection(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
