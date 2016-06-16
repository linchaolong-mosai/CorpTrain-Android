package com.itutorgroup.liveh2h.train.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.network.progress.DefaultProgressIndicator;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.utils.ToastUtils;

/**
 * Created by Rays on 16/5/16.
 */
public class FeedbackActivity extends BaseToolbarActivity implements TextView.OnEditorActionListener {
    private EditText etSubject, etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
        initListener();
        initData();
    }

    private void initViews() {
        etSubject = ViewUtil.findViewById(this, R.id.etSubject);
        etText = ViewUtil.findViewById(this, R.id.etText);
    }

    private void initListener() {
        etText.setOnEditorActionListener(this);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void initData() {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            update();
            return true;
        }
        return false;
    }

    private void update() {
        String subject = etSubject.getText().toString();
        if (TextUtils.isEmpty(subject)) {
            showHintDialog(R.string.subject_cannot_be_empty);
            return;
        }
        String text = etText.getText().toString();
        if (TextUtils.isEmpty(etText.getText())) {
            showHintDialog(R.string.feedback_cannot_be_empty);
            return;
        }
        AppAction.submitFeedback(context, subject, text, new HttpResponseHandler(context,HttpResponse.class, DefaultProgressIndicator.newInstance(context)) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                ToastUtils.showToast(context,getString(R.string.feedback_successfully));
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
