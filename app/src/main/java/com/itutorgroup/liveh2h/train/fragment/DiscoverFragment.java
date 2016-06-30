package com.itutorgroup.liveh2h.train.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.activity.CategoryActivity;
import com.itutorgroup.liveh2h.train.activity.CourseDetailActivity;
import com.itutorgroup.liveh2h.train.activity.MainActivity;
import com.itutorgroup.liveh2h.train.activity.SearchCourseMainActivity;
import com.itutorgroup.liveh2h.train.adpter.CourseCoverAdapter;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.bean.usercourse.UserCourseRoot;
import com.itutorgroup.liveh2h.train.comparotor.CreatTimeComparator;
import com.itutorgroup.liveh2h.train.comparotor.ViewCountComparator;
import com.itutorgroup.liveh2h.train.constants.TrackName;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;
import com.itutorgroup.liveh2h.train.util.LogUtils;
import com.itutorgroup.liveh2h.train.util.Utils;
import com.mosai.ui.HorizontalListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * me
 */
public class DiscoverFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{
    private View view;
    private HorizontalListView hlvNewcourses,hlvRecommended;
    private TextView tvNewcourses,tvRecommended;
    private Context context;
    private SliderLayout mDemoSlider;
    private CourseCoverAdapter newCourseCoverAdapter, recommendedCourseCoverAdapter;
    public DiscoverFragment() {
        // Required empty public constructor
    }
    private List<Courses> hotCourses = new ArrayList<>();
    private List<Courses> newCourses = new ArrayList<>();
    private List<Courses> recommendCourses = new ArrayList<>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MeFragment.
     */
    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    //网络恢复
    public void onEventMainThread(Event.NetChange netChange){
        if(netChange.netChange){
           getDatas();
        }
    }
    //加入课程成功后调用
    public void onEventMainThread(Courses courses)
    {
        Iterator<Courses> coursesIterator = recommendCourses.iterator();
        while(coursesIterator.hasNext()){
            Courses temp = coursesIterator.next();
            if(temp.getCourseInfo().getCourseId().equals(courses.getCourseInfo().getCourseId())){
                recommendCourses.remove(temp);
                recommendedCourseCoverAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
    //断网之后重新加载数据
    public void onEventMainThread(boolean flag){
        if(flag){
            getDatas();
        }
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
        Log.i(this.toString(), "onCreateView()");
        view =  inflater.inflate(R.layout.fragment_discover, container, false);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        tvNewcourses = (TextView) view.findViewById(R.id.tv_newcourses);
        tvRecommended = (TextView)view.findViewById(R.id.tv_recommended);
        hlvNewcourses = (HorizontalListView)view.findViewById(R.id.hlv_newcourses);
        hlvRecommended = (HorizontalListView)view.findViewById(R.id.hlv_recommended);


//        initDatas();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatas();
        addListener();
    }

    private void initSlider(){

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        for(Courses course : hotCourses){
            String imgurl = Utils.getImgUrl(course.getCourseInfo().getCourseId(),course.getCourseInfo().getImageName());
            String name = course.getCourseInfo().getSubject();
            TextSliderView textSliderView = new TextSliderView(context);
            textSliderView
                    .empty(R.drawable.bg_course_default_cover)
                    .error(R.drawable.bg_course_default_cover)
                    .description(name)
                    .image(imgurl)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putSerializable("extra",course);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

    }
    private void initDatas(){
        initSroller();
        getDatas();
    }
    private void getDatas(){
        AppAction.getUserCourses(context, new HttpResponseHandler(context,UserCourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                getCoursesEvent();
                UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                List<Courses> courses = userCourseRoot.getCourses();
                newCourses.clear();
                newCourses.addAll(courses);
                Collections.sort(newCourses,new ViewCountComparator());
                hotCourses.clear();
                if(newCourses.size()>6){
                    hotCourses.addAll(newCourses.subList(0,6));
                }else{
                    hotCourses.addAll(newCourses);
                }
                recommendCourses.clear();
                for(Courses courses1 : courses){
                    if(!courses1.getInviteeInfo().getMandatory()){
                        recommendCourses.add(courses1);
                    }

                }
                Collections.sort(newCourses,new CreatTimeComparator());
                newCourseCoverAdapter.notifyDataSetChanged();
                recommendedCourseCoverAdapter.notifyDataSetChanged();
                initSlider();
            }
            @Override
            public void onResponeseStart() {
                ((MainActivity) context).showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                ((MainActivity) context).dismissProgressDialog();
            }


            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                ((MainActivity) context).showHintDialog(response.message.toString());


            }

        });
    }
    private void initSroller(){
        newCourseCoverAdapter = new CourseCoverAdapter(context,newCourses,R.layout.item_listformat_course);
        recommendedCourseCoverAdapter = new CourseCoverAdapter(context,recommendCourses,R.layout.item_listformat_course);
        hlvNewcourses.setAdapter(newCourseCoverAdapter);
        hlvRecommended.setAdapter(recommendedCourseCoverAdapter);
        }

    //广告栏点击回调
    @Override
    public void onSliderClick(BaseSliderView slider) {
        Courses courses = (Courses) slider.getBundle().get("extra");
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra("course",courses);
        startActivity(intent);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void addListener(){
        hlvRecommended.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("course",recommendCourses.get(position));
                startActivity(intent);
            }
        });
        hlvNewcourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("course",newCourses.get(position));
                startActivity(intent);
            }
        });
        view.findViewById(R.id.tv_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CategoryActivity.class));
            }
        });
        view.findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SearchCourseMainActivity.class));
            }
        });
    }

    @Override
    public String getAnalyticsTrackName() {
        return TrackName.DiscoverScreen;
    }
    private void getCoursesEvent(){
        AnalyticsUtils.setEvent(context,R.array.GetCourses);
    }
}
