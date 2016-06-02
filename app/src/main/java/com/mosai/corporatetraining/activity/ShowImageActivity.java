package com.mosai.corporatetraining.activity;

import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.lidroid.xutils.http.HttpHandler;
import com.mosai.corporatetraining.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowImageActivity extends ABaseToolbarActivity {
    @BindView(R.id.iv_photo)
    PhotoView photoView;
    //    private HttpUtils httpUtils = new HttpUtils();
    private HttpHandler<?> httpHandler;

    @Override
    protected void initDatas() {

        String url = getIntent().getStringExtra("url");
        final String path = getIntent().getStringExtra("path");
        final String filepath = getIntent().getStringExtra("filepath");
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(this)
                    .load(url)
                    .into(photoView, new Callback() {
                        @Override
                        public void onSuccess() {
                            attacher.update();
                        }

                        @Override
                        public void onError() {
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

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (httpHandler != null) {
            httpHandler.cancel();
        }
    }
}
