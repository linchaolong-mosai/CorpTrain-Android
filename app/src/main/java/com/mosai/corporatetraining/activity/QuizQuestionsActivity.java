package com.mosai.corporatetraining.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.quiz.Question;
import com.mosai.corporatetraining.bean.quiz.Questions;
import com.mosai.corporatetraining.bean.resourseforclass.Resources;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.fragment.QuizQuestionFragment;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsActivity extends ABaseToolbarActivity implements QuizQuestionFragment.OnFratListenerQuizQuestion{
    private ViewPager viewPager;
    private TextView tvPage,tvTitle;
    private LinearLayout llNext;
    private ImageView ivLast;
    private Questions questions;
    private Resources resources;
    private int count;
    private int index;
    @Override
    protected void initDatas() {
        resources = (Resources) getIntent().getSerializableExtra("resource");
        questions = (Questions) getIntent().getSerializableExtra("questions");
        count = questions.getQuestions().size();
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        for(Question question : questions.getQuestions()){
            QuizQuestionFragment fragment = QuizQuestionFragment.newInstance(question);
            fragments.add(fragment);
        }
        tvPage.setText(String.format("%d/%d",index+1,count));
    }
    private List<QuizQuestionFragment> fragments = new ArrayList<>();
    @Override
    protected int setContent() {
        return R.layout.activity_quiz_questions;
    }

    @Override
    protected void initView() {
        viewPager = ViewUtil.findViewById(this,R.id.viewPager);
        tvPage = ViewUtil.findViewById(this,R.id.tv_page);
        tvTitle = ViewUtil.findViewById(this,R.id.tv_title);
        llNext = ViewUtil.findViewById(this,R.id.ll_next);
        ivLast = ViewUtil.findViewById(this,R.id.iv_last_step);
        ivLast.setVisibility(View.GONE);
//        llNext.setVisibility(count==0?View.GONE:View.VISIBLE);
    }
    private int submitCount;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            submitCount+=1;
            if(submitCount>=count){
//                getSummary();
                Intent intent = new Intent(context,QuizSummaryActivity.class);
                intent.putExtra("resource",resources);
                startActivityForResult(intent,0);
            }
        }
    };

    private void submitQuizAnswers(){
        for (QuizQuestionFragment fragment:fragments){
            AppAction.submitQuizAnswer(context, resources.getClassId(), fragment.question.getQuestionId(), fragment.adapter.index, new HttpResponseHandler(HttpResponse.class) {
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
    private void checkIndex(){
        if(index>=count){
            if(count==index){
            submitQuizAnswers();
                MyLog.e("znb","submitQuizAnswers");
            }else{
                index-=1;
            }
            return;
        }
        String page = String.format("%d/%d",index+1,count);
        tvPage.setText(page);
        viewPager.setCurrentItem(index);
        if(index==0){
            ivLast.setVisibility(View.GONE);
            llNext.setVisibility(View.VISIBLE);
        }else if(index==count-1){
            ivLast.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        }else{
            ivLast.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        }
    }
    private void nextPage(){
        index+=1;
        checkIndex();
    }
    private void lastPage(){
        index-=1;
        checkIndex();
    }
    @Override
    protected void addListener() {
        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPage();
            }
        });
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
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
    @Override
    public void onFragmentInteraction(Question question) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            back();
        }else{

            //重做
//            index-=1;
            index =0;
            submitCount=0;
            checkIndex();
//            viewPager.setCurrentItem(index);
        }
    }
}
