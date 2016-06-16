package com.itutorgroup.liveh2h.train.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.quiz.Questions;
import com.itutorgroup.liveh2h.train.bean.quiz.Quiz;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.ViewUtil;

import butterknife.BindView;

public class QuizActivity extends ABaseToolbarActivity {
    @BindView(R.id.tv_lessonname)
    TextView tvLessonname;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    private TextView tvTitle, tvLessonName, tvDes;
    private Quiz quiz;
    private Resources resource;

    @Override
    protected void initDatas() {
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");
        resource = (Resources) getIntent().getSerializableExtra("resource");
        tvDes.setText(quiz.description);
        tvLessonName.setText(quiz.title);
    }

    @Override
    protected int setContent() {
        return R.layout.activity_quiz;
    }

    @Override
    protected void initView() {
        tvTitle = ViewUtil.findViewById(this, R.id.tv_title);
        tvLessonName = ViewUtil.findViewById(this, R.id.tv_lessonname);
        tvDes = ViewUtil.findViewById(this, R.id.tv_description);
    }

    @Override
    protected void addListener() {
        findViewById(R.id.btn_start_quiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuestions();
            }
        });
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void getQuestions() {
        AppAction.getQuestionslistByQuizId(context, resource.getResourceId(), new HttpResponseHandler(context,Questions.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                Questions questions = (Questions) response;
                Intent intent = new Intent(context, QuizQuestionsActivity.class);
                intent.putExtra("questions", questions);
                intent.putExtra("resource", resource);
                intent.putExtra("quiz",quiz);
                startActivity(intent);
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

}
