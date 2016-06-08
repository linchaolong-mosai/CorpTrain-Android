package com.mosai.corporatetraining.activity;

import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.quiz.QuizSummary;
import com.mosai.corporatetraining.bean.resourseforclass.Resources;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.ui.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class QuizSummaryActivity extends ABaseToolbarActivity {
    @BindView(R.id.civ_avator)
    CircleImageView civAvator;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_isPass)
    TextView tvIsPass;
    @BindView(R.id.tv_correct)
    TextView tvCorrect;
    @BindView(R.id.tv_incorrect)
    TextView tvIncorrect;
    @BindView(R.id.donut_progress)
    DonutProgress donutProgress;
    @BindView(R.id.tv_accuracy)
    TextView tvAccuracy;
    private Resources resources;
    private DisplayImageOptions options;

    @Override
    protected void initDatas() {
        resources = (Resources) getIntent().getSerializableExtra("resource");
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.blank_user_small)
                .showImageOnFail(R.drawable.blank_user_small)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoader.getInstance().displayImage(UserPF.getInstance().getAvatarUrl(),civAvator,options);

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
//        getSummary();
        setSummary();
    }
    private void setSummary(){
        int correctcount=getIntent().getIntExtra("correctcount",0);
        int incorrectcount=getIntent().getIntExtra("incorrectcount",0);
        int accuracy=getIntent().getIntExtra("accuracy",0);
        tvTips.setTextColor(getResources().getColor(accuracy>=60?R.color.colorPrimary:R.color.red));
        tvIsPass.setTextColor(getResources().getColor(accuracy>=60?R.color.colorPrimary:R.color.red));
        tvTips.setText(accuracy>=60?getString(R.string.congratulation):getString(R.string.sorry));
        tvIsPass.setText(accuracy>=60?getString(R.string.congratulationtips):getString(R.string.sorry_tips));
        tvIsPass.setText(accuracy>=60?getString(R.string.congratulationtips):getString(R.string.sorry_tips));
        tvCorrect.setText(correctcount+"");
        tvIncorrect.setText(incorrectcount+"");
        tvAccuracy.setText(accuracy+"%");
        donutProgress.setProgress(accuracy);
    }
    private void getSummary() {

        AppAction.getQuizSummary(context, "user", resources.getClassId(), resources.getResourceId(), new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    QuizSummary summary = new Gson().fromJson(new JSONObject(responseString).getJSONObject("summary").toString(), QuizSummary.class);
                    /**
                     * {"returnCode":0,"summary":{"passingGrade":0.0,"grade":0,
                     * "passCount":2,"totalAnswer":3,"passRatio":0.666666666666,
                     * "pass":true}}
                     */
                tvTips.setTextColor(getResources().getColor(summary.pass?R.color.colorPrimary:R.color.red));
                    tvIsPass.setTextColor(getResources().getColor(summary.pass?R.color.colorPrimary:R.color.red));
                tvTips.setText(summary.pass?getString(R.string.congratulation):getString(R.string.sorry));
                    tvIsPass.setText(summary.pass?getString(R.string.congratulationtips):getString(R.string.sorry_tips));
                    tvCorrect.setText(summary.passCount+"");
                    tvIsPass.setText(summary.pass?getString(R.string.congratulationtips):getString(R.string.sorry_tips));
                    tvCorrect.setText(summary.passCount+"");
                    tvIncorrect.setText(String.valueOf(summary.totalAnswer-summary.passCount));
                    tvAccuracy.setText(((int)(summary.passRatio*100))+"%");
                    donutProgress.setProgress(((int)(summary.passRatio*100)));
                } catch (JSONException e) {

                    e.printStackTrace();

                }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        back();
    }

}
