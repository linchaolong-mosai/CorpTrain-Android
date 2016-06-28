package com.itutorgroup.liveh2h.train.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.SelectedCallback;
import com.itutorgroup.liveh2h.train.activity.ClassResourceActivity;
import com.itutorgroup.liveh2h.train.activity.CourseDetailActivity;
import com.itutorgroup.liveh2h.train.adpter.ClassAdapter;
import com.itutorgroup.liveh2h.train.bean.classesforcourse.Classes;
import com.itutorgroup.liveh2h.train.bean.classesforcourse.ClassesRoot;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.constants.TrackName;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.ui.RatingDialog;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.ui.SegmentedControlView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;


public class CourseDetailsFragment extends BaseFragment implements SegmentedControlView.OnSelectionChangedListener{
    private ImageView ivIcon;
    public RatingBar ratingBar;
    private List<Classes> classes = new ArrayList<>();
    private ClassAdapter adapter;
    private Courses courses;
    private Context context;
    private TextView tvSubject,tvDec;
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
    public Button btnJoinCourse;
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
        lv = ViewUtil.findViewById(view,R.id.lv);
        tvDec = ViewUtil.findViewById(view,R.id.tv_description);
        tvSubject = ViewUtil.findViewById(view,R.id.tv_title);
        tvDec.setVisibility(View.VISIBLE);
        tvSubject.setVisibility(View.VISIBLE);
        ivIcon = ViewUtil.findViewById(view, R.id.iv_icon);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
        courses = (Courses)getArguments().getSerializable("course");
        ratingDialog = new RatingDialog(context);
        scv = (SegmentedControlView) view.findViewById(R.id.scv);
        scv.setColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));
        scv.setEqualWidth(true);
        scv.setStretch(true);

        try {
            scv.setItems(new String[]{getString(R.string.course_details), getString(R.string.course_comments)}, new String[]{"one", "two"});
            scv.setDefaultSelection(0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.bg_course_default_cover)
                .showImageOnFail(R.drawable.bg_course_default_cover)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
        tvSubject.setText(courses.getCourseInfo().getSubject());
        tvDec.setText(courses.getCourseInfo().getDescription());
        adapter = new ClassAdapter(context,classes,R.layout.item_listformat_class);
        lv.setAdapter(adapter);
        ratingBar.setRating(courses.getCourseInfo().getRating());
        String imgurl = String.format("%s%s/%s", AppAction.IMG_RESOURSE_COURSE_URL, courses.getCourseInfo().getCourseId(), courses.getCourseInfo().getImageName());
        ImageLoader.getInstance().displayImage(imgurl, ivIcon);
        addListener();
        getClasses();

    }
    public void checkJoin(Courses courses){
        if(!courses.getInviteeInfo().getMandatory()&&courses.getAttendeeInfo().getCompletePercent()==0){
            btnJoinCourse.setVisibility(View.VISIBLE);
        }else{
            btnJoinCourse.setVisibility(View.GONE);
        }
    }
    private void addListener(){
        scv.setOnSelectionChangedListener(this);
//        checkJoin(courses);
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
                startActivityForResult(intent,0);
//                startActivity(intent);
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
    public void onEventMainThread(boolean flag)
    {
//        enrolled.getDatas();

    }
    public void onEventMainThread(Event.SubmitRate submitRate){
        ratingBar.setRating(submitRate.rate);
    }
    private void updateMycourse(){
        EventBus.getDefault().post(courses);
    }
    MaterialDialog dialog;
    private void joinCourse(){
        AppAction.joinCourse(context, courses.getCourseInfo().getCourseId(), new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                JoinACourseEvent();
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
                getClassesEvent();
                ClassesRoot classesRoot = (ClassesRoot) response;
                classes.clear();
                classes.addAll(classesRoot.getClasses());
                getClassesPercent(true);
//                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * get Classes percent
     * @param first 是否第一次进入
     */
    private void getClassesPercent(final boolean first){
        if(classes.size()==0)
            return;
        AppAction.getClassesPercentByCourseId(context, courses.getCourseInfo().getCourseId(), new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    boolean updateMyCourses = false;
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject ids = jsonObject.optJSONObject("completed_percent_list");
                    Iterator<String> keys = ids.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        for(Classes classes1:classes){
                            if(TextUtils.equals(key,classes1.getClassInfo().getClassId())){
                                if(!first){
//                                    if(classes1.getClassInfo().percent!=100&&ids.optInt(key)==100)
                                        updateMyCourses &= (classes1.getClassInfo().percent!=100&&ids.optInt(key)==100);
                                }
                                classes1.getClassInfo().percent = ids.optInt(key);
                                continue;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    updateMyCourses(updateMyCourses);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void updateMyCourses(boolean update){
        if (!update)
            return;
        EventBus.getDefault().post(new Event.UpdateMyCourses());

    }
    private RatingDialog ratingDialog;
    public void rating() {
        if(!ratingDialog.isShowing()){

            ratingDialog.show();
        }
    }
    private DisplayImageOptions options;
    private void submitRating(final float rating){
        AppAction.submitCourseRating(context, courses.getCourseInfo().getCourseId(), rating, new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                submitCourseRatingEvent();
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

    @Override
    public void newSelection(String identifier, String value) {
        if ("two".equals(value)) {
            selectedCallback.callback(1);
            try {
                scv.setDefaultSelection(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getClassesPercent(false);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /****************************************Analytics**************************/

    private void JoinACourseEvent(){
        AnalyticsUtils.setEvent(context,R.array.JoinACourse);
    }
    private void submitCourseRatingEvent(){
        AnalyticsUtils.setEvent(context,R.array.SubmitCourseRating);
    }
    private void getClassesEvent(){
        AnalyticsUtils.setEvent(context,R.array.GetClasses);
    }
    /****************************************Analytics**************************/
}
