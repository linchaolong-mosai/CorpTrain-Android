package com.mosai.corporatetraining.activity;

import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.lidroid.xutils.http.HttpHandler;
import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.resourseforclass.Resources;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.event.Event;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowImageActivity extends ABaseToolbarActivity {
    @BindView(R.id.iv_photo)
    PhotoView photoView;
    //    private HttpUtils httpUtils = new HttpUtils();
    private HttpHandler<?> httpHandler;
    public void onEventMainThread(Event.SubmitPercent submitPercent){

    }
    private Resources resources;
    @Override
    protected void initDatas() {
        resources = (Resources) getIntent().getSerializableExtra("resource");
        String url = getIntent().getStringExtra("url");
        final String filepath = getIntent().getStringExtra("filepath");
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        if (!TextUtils.isEmpty(url)) {
            showProgressDialog();
            Picasso.with(this)
                    .load(url)
                    .into(photoView, new Callback() {
                        @Override
                        public void onSuccess() {
                            attacher.update();
                            dismissProgressDialog();
                        }

                        @Override
                        public void onError() {
                            dismissProgressDialog();
                        }
                    });
        } else {
            photoView.setImageBitmap(BitmapFactory.decodeFile(filepath));
        }

    }

    @Override
    protected int setContent() {
        return R.layout.activity_show_image;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (httpHandler != null) {
            httpHandler.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
    private void submitPercent(){
        if(resources.percent!=100){
            AppAction.submitResourcePercent(context, resources.getClassId(), resources.getResourceId(), 100, new HttpResponseHandler(context,HttpResponse.class) {
                @Override
                public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                    Event.SubmitPercent submitPercent = new Event.SubmitPercent();
                    submitPercent.resources = resources;
                    EventBus.getDefault().post(submitPercent);
                    ShowImageActivity.super.back();
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

        }else{
            ShowImageActivity.super.back();
        }
    }
    @Override
    public void back() {
        submitPercent();

    }
}
