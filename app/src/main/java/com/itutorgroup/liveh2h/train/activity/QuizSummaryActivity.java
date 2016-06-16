package com.itutorgroup.liveh2h.train.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.quiz.QuizSummary;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.mosai.ui.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

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
                .showImageForEmptyUri(R.drawable.ic_blank_user_small)
                .showImageOnFail(R.drawable.ic_blank_user_small)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoader.getInstance().displayImage(UserPF.getInstance().getAvatarUrl(),civAvator,options);

    }
    public void onEventMainThread(Event.SubmitPercent submitPercent){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int setContent() {
        return R.layout.activity_quiz_summary;
    }

    @Override
    protected void initView() {

    }
    private void submitPercent(){
        AppAction.submitResourcePercent(context, resources.getClassId(), resources.getResourceId(), 100, new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                Event.SubmitPercent submitPercent = new Event.SubmitPercent();
                submitPercent.resources = resources;
                EventBus.getDefault().post(submitPercent);
                setResult(RESULT_OK);
                back();
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
    protected void addListener() {
        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPercent();
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
        EventBus.getDefault().register(this);
    }
    private int accuracy;
    private void setSummary(){
        int correctcount=getIntent().getIntExtra("correctcount",0);
        int incorrectcount=getIntent().getIntExtra("incorrectcount",0);
        accuracy=getIntent().getIntExtra("accuracy",0);
        int passingGrade = getIntent().getIntExtra("passingGrade",0);
        tvTips.setTextColor(getResources().getColor(accuracy>=passingGrade?R.color.colorPrimary:R.color.red));
        tvIsPass.setTextColor(getResources().getColor(accuracy>=passingGrade?R.color.colorPrimary:R.color.red));
        tvTips.setText(accuracy>=passingGrade?getString(R.string.congratulation):getString(R.string.sorry));
        tvIsPass.setText(accuracy>=passingGrade?getString(R.string.congratulationtips):getString(R.string.sorry_tips));
        tvIsPass.setText(accuracy>=passingGrade?getString(R.string.congratulationtips):getString(R.string.sorry_tips));
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
                    tvAccuracy.setVisibility(View.INVISIBLE);
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
        submitPercent();
    }
    private boolean firstCome=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            if(firstCome){
                startAnimation();
                firstCome=false;
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }
    private int tempProgress;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(tempProgress<accuracy){
                tempProgress+=1;
                donutProgress.setProgress(tempProgress);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                },dTime);
            }else{
                tvAccuracy.setVisibility(View.VISIBLE);
            }
        }
    };
    private int dTime;
    private void startAnimation(){
        if(accuracy==0){
            tvAccuracy.setVisibility(View.VISIBLE);
        }else{
            tvAccuracy.setVisibility(View.INVISIBLE);
            dTime = 1000/accuracy;
            donutProgress.setProgress(0);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            },dTime);
        }
    }
}
