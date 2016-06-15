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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.SelectedCallback;
import com.mosai.corporatetraining.activity.ClassResourceActivity;
import com.mosai.corporatetraining.activity.CourseDetailActivity;
import com.mosai.corporatetraining.adpter.ClassAdapter;
import com.mosai.corporatetraining.bean.classesforcourse.Classes;
import com.mosai.corporatetraining.bean.classesforcourse.ClassesRoot;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.event.Event;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.ui.RatingDialog;
import com.mosai.corporatetraining.util.ViewUtil;
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


public class CourseDetailsFragment extends Fragment implements SegmentedControlView.OnSelectionChangedListener{
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
    private void addListener(){
        scv.setOnSelectionChangedListener(this);
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
}
