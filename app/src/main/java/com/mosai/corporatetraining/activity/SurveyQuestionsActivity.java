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
import com.mosai.corporatetraining.bean.resourseforclass.Resources;
import com.mosai.corporatetraining.bean.survey.SurveyQuestion;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.event.Event;
import com.mosai.corporatetraining.fragment.SurveyQuestionFragment;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.utils.MyLog;
import com.mosai.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;

public class SurveyQuestionsActivity extends ABaseToolbarActivity implements SurveyQuestionFragment.OnSurveyQuetsionFragmentInteractionListener{
    private ViewPager viewPager;
    private TextView tvPage,tvTitle;
    private LinearLayout llNext;
    private ImageView ivLast;
    private List<SurveyQuestion> questions;
    private Resources resources;
    private int count;
    private int index;
    private List<SurveyQuestionFragment> fragments = new ArrayList<>();
    @Override
    protected void initDatas() {
        resources = (Resources) getIntent().getSerializableExtra("resource");
        questions = (List<SurveyQuestion>) getIntent().getSerializableExtra("questions");
        count = questions.size();
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        for(SurveyQuestion question : questions){
            SurveyQuestionFragment fragment = SurveyQuestionFragment.newInstance(questions.indexOf(question), question);
            fragments.add(fragment);
        }
        tvPage.setText(String.format("%d/%d",index+1,count));
    }

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
    }
    private int submitCount;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            submitCount+=1;
            if(submitCount>=count){
                submitPercent();
            }
        }
    };
    private void submitPercent(){
        AppAction.submitResourcePercent(context, resources.getClassId(), resources.getResourceId(), 100, new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                Event.SubmitPercent submitPercent = new Event.SubmitPercent();
                submitPercent.resources = resources;
                EventBus.getDefault().post(submitPercent);
                ToastUtils.showToast(context,getString(R.string.submit_successfully));
                finish();
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
    private void submitSurveyAnwsers(){
        submitCount=0;
        for (SurveyQuestionFragment fragment:fragments){

            AppAction.submitSurveyAnswer(context, resources.getClassId(), fragment.question.questionId, fragment.adapter.index, new HttpResponseHandler(context,HttpResponse.class) {
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
                    index=count-1;
                    showHintDialog(response.message);
                }
            });
        }
    }
    private MaterialDialog submitDialog;
    private void checkIndex() {
        if (index >= count) {
            if (count == index) {
                //提交答案
                for (SurveyQuestionFragment fragment : fragments) {
                    if (fragment.adapter.index == -1) {
                        index -= 1;
                        ToastUtils.showToast(context, getString(R.string.quiz_answer_unfinished));
                        return;
                    }
                }
                submitDialog = new MaterialDialog(context)
                        .setMessage(R.string.submit_message)
                        .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyLog.e("znb", "submitQuizAnswers");
//                                submitQuizAnswers();
                                submitSurveyAnwsers();
                                submitDialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                submitDialog.dismiss();
                                index-=1;
                            }
                        });
                submitDialog.show();
            } else {
                index -= 1;
            }
            return;
        }
        String page = String.format("%d/%d", index + 1, count);
        tvPage.setText(page);
        viewPager.setCurrentItem(index);
        if (index == 0) {
            ivLast.setVisibility(View.GONE);
            llNext.setVisibility(View.VISIBLE);
        } else if (index == count - 1) {
            ivLast.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        } else {
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
        EventBus.getDefault().register(this);
    }
    public void onEventMainThread(Event.SubmitPercent submitPercent){

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFragmentInteraction(int index, int answer) {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            back();
        }else{
            index =0;
            submitCount=0;
            checkIndex();
        }
    }
}
