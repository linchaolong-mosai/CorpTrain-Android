package com.mosai.corporatetraining.fragment;

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
import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.activity.CategoryActivity;
import com.mosai.corporatetraining.activity.CourseDetailActivity;
import com.mosai.corporatetraining.activity.MainActivity;
import com.mosai.corporatetraining.activity.SearchCourseMainActivity;
import com.mosai.corporatetraining.adpter.CourseCoverAdapter;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.bean.usercourse.UserCourseRoot;
import com.mosai.corporatetraining.comparotor.ViewCountComparator;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.LogUtils;
import com.mosai.ui.HorizontalListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * me
 */
public class DiscoverFragment extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{
    private View view;
    private HorizontalListView hlvNewcourses,hlvRecommended;
    private TextView tvNewcourses,tvRecommended;
    private Context mContext;
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
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
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

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        for(Courses course : hotCourses){
            String imgurl = String.format("%s%s/%s",AppAction.IMG_RESOURSE_COURSE_URL,course.getCourseInfo().getCourseId(),course.getCourseInfo().getImageName());
            String name = course.getCourseInfo().getSubject();
            TextSliderView textSliderView = new TextSliderView(mContext);
            // initialize a SliderLayout
            textSliderView
                    .empty(R.drawable.bg_course_default_cover)
                    .error(R.drawable.bg_course_default_cover)
                    .description(name)


                    .image(imgurl)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);



            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putSerializable("extra",course);
            mDemoSlider.addSlider(textSliderView);
            mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }

    }
    private void initDatas(){
//        initSlider();
        initSroller();
        getDatas();
    }
    private void getDatas(){
        AppAction.getUserCourses(mContext, new HttpResponseHandler(UserCourseRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                UserCourseRoot userCourseRoot = (UserCourseRoot) response;
                List<Courses> courses = userCourseRoot.getCourses();
                newCourses.clear();
                newCourses.addAll(courses);
                Collections.sort(newCourses,new ViewCountComparator());
                for(Courses courses1:newCourses){
                    LogUtils.e(courses1.getCourseInfo().getViewCount()+"");
                }

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
                newCourseCoverAdapter.notifyDataSetChanged();
                recommendedCourseCoverAdapter.notifyDataSetChanged();
                initSlider();
            }
            @Override
            public void onResponeseStart() {
                ((MainActivity)mContext).showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                ((MainActivity)mContext).dismissProgressDialog();
            }


            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                ((MainActivity)mContext).showHintDialog(response.message.toString());


            }

        });
    }
    private void initSroller(){
        newCourseCoverAdapter = new CourseCoverAdapter(mContext,newCourses,R.layout.item_listformat_course);
        recommendedCourseCoverAdapter = new CourseCoverAdapter(mContext,recommendCourses,R.layout.item_listformat_course);
        hlvNewcourses.setAdapter(newCourseCoverAdapter);
        hlvRecommended.setAdapter(recommendedCourseCoverAdapter);
        }

    //广告栏点击回调
    @Override
    public void onSliderClick(BaseSliderView slider) {
//        ToastUtils.showToast(mContext,slider.getBundle().get("extra") + "");
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
                Intent intent = new Intent(mContext, CourseDetailActivity.class);
                intent.putExtra("course",recommendCourses.get(position));
                startActivity(intent);
            }
        });
        hlvNewcourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, CourseDetailActivity.class);
                intent.putExtra("course",newCourses.get(position));
                startActivity(intent);
            }
        });
        view.findViewById(R.id.tv_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CategoryActivity.class));
            }
        });
        view.findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SearchCourseMainActivity.class));
            }
        });
    }
    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFratListenerQuizQuestion) {
            mListener = (OnFratListenerQuizQuestion) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFratListenerQuizQuestion");
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFratListenerQuizQuestion {
        void onFragmentInteraction(Uri uri);
    }*/
}
