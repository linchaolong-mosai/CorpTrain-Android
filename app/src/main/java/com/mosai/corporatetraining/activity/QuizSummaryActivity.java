package com.mosai.corporatetraining.activity;

import android.view.View;

import com.google.gson.Gson;
import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.quiz.QuizSummary;
import com.mosai.corporatetraining.bean.resourseforclass.Resources;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class QuizSummaryActivity extends ABaseToolbarActivity {
    private Resources resources;
    @Override
    protected void initDatas() {
        resources = (Resources) getIntent().getSerializableExtra("resource");
    }

    @Override
    protected int setContent() {
        return R.layout.activity_quiz_summary;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {
        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                back();
            }
        });
        findViewById(R.id.btn_retake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }
    private void getSummary(){

        AppAction.getQuizSummary(context, "user", resources.getClassId(), resources.getResourceId(), new HttpResponseHandler(HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    QuizSummary summary = new Gson().fromJson(new JSONObject(responseString).getJSONObject("summary").toString(),QuizSummary.class);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        back();
    }
}
