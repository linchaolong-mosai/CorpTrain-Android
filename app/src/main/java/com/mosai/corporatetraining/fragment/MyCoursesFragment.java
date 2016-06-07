package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.ui.CantScrollViewPager;
import com.mosai.ui.SegmentedControlView;

import de.greenrobot.event.EventBus;


/**
 * me
 */
public class MyCoursesFragment extends Fragment implements SegmentedControlView.OnSelectionChangedListener {
    private final static int COUNT = 3;
    private final static int ONE = 0;
    private final static int TWO = 1;
    private final static int THREE = 2;
    private Context context;
    private View view;
    private CourseListFragment mandatory,enrolled,completed;

    private SegmentedControlView scv;
    private CantScrollViewPager viewPager;

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

    //加入课程成功后调用
    public void onEventMainThread(Courses courses)
    {
        enrolled.getDatas();

    }
    public MyCoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MeFragment.
     */
    public static MyCoursesFragment newInstance() {
        MyCoursesFragment fragment = new MyCoursesFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(this.toString(), "onCreateView()");
        view =  inflater.inflate(R.layout.fragment_my_courses, container, false);
        viewPager = (CantScrollViewPager) view.findViewById(R.id.viewPager);
        scv = (SegmentedControlView) view.findViewById(R.id.scv);
        scv.setColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));
        scv.setEqualWidth(true);
        scv.setStretch(true);

        try {
            scv.setItems(new String[]{getString(R.string.mandatory_courses), getString(R.string.enrolled_courses),getString(R.string.completed_courses)}, new String[]{"one", "two","three"});
            scv.setDefaultSelection(0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void newSelection(String identifier, String value) {
        if ("one".equals(value)) {
            viewPager.setCurrentItem(ONE);
        } else if ("two".equals(value)) {
            viewPager.setCurrentItem(TWO);
        } else{
            viewPager.setCurrentItem(THREE);
        }
    }

    private class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case ONE:
                    return mandatory;
                case TWO:
                    return enrolled;
                case THREE:
                    return completed;
            }
            return null;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return COUNT;
        }

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        mandatory = new CourseListFragment();
        bundle.putInt("tag",ONE);
        bundle.putInt("type", AppAction.SEARCH_USER_COURSE_FILTER_TYPE_MANDATORY);
        mandatory.setArguments(bundle);


        bundle = new Bundle();
        enrolled = new CourseListFragment();
        bundle.putInt("tag",TWO);
        bundle.putInt("type", AppAction.SEARCH_COURSE_FILTER_TYPE_ALL);
        enrolled.setArguments(bundle);

        bundle = new Bundle();
        completed = new CourseListFragment();
        bundle.putInt("tag",THREE);
        bundle.putInt("type", AppAction.SEARCH_USER_COURSE_FILTER_TYPE_FINISHED);
        completed.setArguments(bundle);

        viewPager.setAdapter(new TabAdapter(((AppCompatActivity)context).getSupportFragmentManager()));
        scv.setOnSelectionChangedListener(this);
    }
}
