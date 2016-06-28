package com.itutorgroup.liveh2h.train.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.constants.TrackName;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.network.progress.DefaultProgressIndicator;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;
import com.itutorgroup.liveh2h.train.util.ViewUtil;
import com.mosai.utils.ToastUtils;

/**
 * Created by Rays on 16/5/16.
 */
public class UpdateNameActivity extends BaseToolbarActivity implements TextView.OnEditorActionListener {
    private EditText etFirstName, etLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        initViews();
        initListener();
        initData();
    }

    private void initViews() {
        etFirstName = ViewUtil.findViewById(this, R.id.etFirstName);
        etLastName = ViewUtil.findViewById(this, R.id.etLastName);
    }

    private void initListener() {
        etLastName.setOnEditorActionListener(this);
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
        if (TextUtils.isEmpty(etFirstName.getText())) {
            showHintDialog(R.string.first_name_cannot_be_empty);
            return;
        }
        if (TextUtils.isEmpty(etLastName.getText())) {
            showHintDialog(R.string.last_name_cannot_be_empty);
            return;
        }
        final String name = etFirstName.getText().toString() + " " + etLastName.getText().toString();
        AppAction.changeName(context, name, new HttpResponseHandler(context,HttpResponse.class, DefaultProgressIndicator.newInstance(context)) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                changeNameEvent();
                UserPF.getInstance().putString(UserPF.USER_NAME, name);
                ToastUtils.showToast(context,getString(R.string.change_name_successfully));
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
    /****************************************Analytics**************************/
    @Override
    public String getAnalyticsTrackScreenName() {
        return TrackName.ChangeNameScreen;
    }
    private void changeNameEvent(){
        AnalyticsUtils.setEvent(context,R.array.ChangeName);
    }
    /****************************************Analytics**************************/
}
