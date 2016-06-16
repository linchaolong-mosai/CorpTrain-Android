package com.itutorgroup.liveh2h.train.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.bean.survey.SurveyQuestion;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.fragment.SurveyQuestionFragment;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestionsActivity2 extends ABaseToolbarActivity implements SurveyQuestionFragment.OnSurveyQuetsionFragmentInteractionListener {
    private ViewPager viewPager;
    private TextView tvPage, tvTitle;
    private List<SurveyQuestion> questions;
    private Resources resources;
    private int count, index;
    private List<SurveyQuestionFragment> fragments = new ArrayList<>();

    @Override
    protected void initDatas() {

    }

    @Override
    protected int setContent() {
        return R.layout.activity_survey_questions;
    }

    @Override
    protected void initView() {
        questions = (List<SurveyQuestion>) getIntent().getSerializableExtra("questions");
        resources = (Resources) getIntent().getSerializableExtra("resource");
        count = questions.size();
        tvPage = ViewUtil.findViewById(this,R.id.tv_page);

        viewPager = ViewUtil.findViewById(this,R.id.viewPager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        for (SurveyQuestion question : questions) {
            SurveyQuestionFragment fragment = SurveyQuestionFragment.newInstance(questions.indexOf(question), question);
            fragments.add(fragment);


        }
        tvPage.setText(String.format("%d/%d", index + 1, count));
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void addListener() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    public void onFragmentInteraction(int index, int answer) {
        questions.get(index).answer = answer;
        if (index < count - 1) {
            this.index = index + 1;
            tvPage.setText(String.format("%d/%d", this.index + 1, count));
            viewPager.setCurrentItem(this.index);
        }else{
            submitServeyAnwsers();
        }
    }

    private int submitCount;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            submitCount+=1;
            if(submitCount>=count){
//                getSummary();
//                Intent intent = new Intent(context,QuizSummaryActivity.class);
//                intent.putExtra("resource",resources);
//                startActivityForResult(intent,0);
                back();
            }
        }
    };
    private void submitServeyAnwsers(){
        for (SurveyQuestion question:questions){

            AppAction.submitSurveyAnswer(context, resources.getClassId(), question.questionId, question.answer, new HttpResponseHandler(context,HttpResponse.class) {
                @Override
                public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                    handler.sendEmptyMessage(0);

                }

                @Override
                public void onResponeseStart() {
                    showProgressDialog();
                }

                @Override
                public void onResponesefinish() {
                    dismissProgressDialog();
                }

                @Override
                public void onResponeseFail(int statusCode, HttpResponse response) {
                    showHintDialog(response.message);
                }
            });
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
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return count;
        }

    }
}
