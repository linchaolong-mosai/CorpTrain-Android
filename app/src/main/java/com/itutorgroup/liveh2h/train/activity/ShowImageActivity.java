package com.itutorgroup.liveh2h.train.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.lidroid.xutils.http.HttpHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
            ImageLoader.getInstance().displayImage(url, photoView,options,new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    showProgressDialog();
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    attacher.update();
                    dismissProgressDialog();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else {
            ImageLoader.getInstance().displayImage("file://"+filepath,photoView);
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
                    showTextProgressDialog(context.getString(R.string.uploading));
                }

                @Override
                public void onResponesefinish() {

                    dismissTextProgressDialog();
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
