package com.mosai.corporatetraining.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.LogUtils;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.ui.ActionSheetDialog;
import com.mosai.ui.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import pl.aprilapps.easyphotopicker.EasyImage;

public class PersonalInfoActivity extends BaseToolbarActivity implements View.OnClickListener {
    private TextView tvName, tvCompanyName, tvPhone;
    private View rlName, rlPhone;
    private UserPF userPF = UserPF.getInstance();
    private CircleImageView ivMyicon;
    private ActionSheetDialog actionSheetDialog;
    private Activity mContext;
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        mContext = this;
        setImageloaderOptions();
        initViews();
        initListener();
        initData();
    }

    private void initViews() {
        ivMyicon = ViewUtil.findViewById(this,R.id.ivMyicon);
        tvName = ViewUtil.findViewById(this, R.id.tvName);
        tvCompanyName = ViewUtil.findViewById(this, R.id.tvCompanyName);
        tvPhone = ViewUtil.findViewById(this, R.id.tvPhone);
        rlName = ViewUtil.findViewById(this, R.id.rlName);
        rlPhone = ViewUtil.findViewById(this, R.id.rlPhone);
        //初始化ActionSheetDialog
        actionSheetDialog = new ActionSheetDialog(this)
                .builder()
                .setCanceledOnTouchOutside(false)
                .addSheetItem(getString(R.string.photo_album), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                EasyImage.openGallery(mContext, 0);
                            }

                        })
                .addSheetItem(getString(R.string.photograph), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                EasyImage.openCamera(mContext, 0);
                            }

                        });
    }

    private void initListener() {
        ivMyicon.setOnClickListener(this);
        rlName.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void initData() {
        tvCompanyName.setText(userPF.getString(UserPF.CT_COMPANY_NAME, getString(R.string.unbound)));
        ImageLoader.getInstance().displayImage(UserPF.getInstance().getAvatarUrl(),ivMyicon,options);
        LogUtils.e(UserPF.getInstance().getAvatarUrl());
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvName.setText(userPF.getString(UserPF.USER_NAME, ""));
        tvPhone.setText(userPF.getString(UserPF.PHONE, getString(R.string.to_bind)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlPhone:
                startActivity(new Intent(context, UpdatePhoneActivity.class));
                break;
            case R.id.rlName:
                startActivity(new Intent(context, UpdateNameActivity.class));
                break;
            case  R.id.ivMyicon:
                actionSheetDialog.show();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {
                LogUtils.e("onImagePickerError");
            }

            @Override
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
                LogUtils.e("onImagePicked");
                HandleReturnPic(file);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                LogUtils.e("onCanceled");

            }
        });

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            try {
                Uri uri = UCrop.getOutput(data);
                final String path = new File(new URI(uri.toString())).getAbsolutePath();
                LogUtils.e(String.format("Ucop Success,filepath:%s",path));
                uploadFile(path);
//                ivMyicon.setImageBitmap(BitmapFactory.decodeFile(path));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
    }
    private void HandleReturnPic(File imageFile) {
        UCrop.Options options = new UCrop.Options();
        int color = getResources().getColor(R.color.colorPrimary);
        //options.setCropFrameColor(color);
        options.setToolbarColor(color);
        options.setActiveWidgetColor(color);
        options.setLogoColor(color);
        options.setStatusBarColor(color);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(10);
        options.setDimmedLayerColor(color);
        UCrop.of(Uri.fromFile(imageFile), Uri.fromFile(imageFile))
                .withMaxResultSize(320, 480)
                .withOptions(options)
                .start(mContext);
    }
    private void setImageloaderOptions(){
         options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.ic_blank_user_small)
                .showImageOnFail(R.drawable.ic_blank_user_small)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }
    private void uploadFile(String path){
        AppAction.uploadMyicon(this, path, new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {

                String ivurl = UserPF.getInstance().getAvatarUrl();
                ImageLoader.getInstance().getDiskCache().remove(ivurl);
                ImageLoader.getInstance().getMemoryCache().remove(ivurl);
                ImageLoader.getInstance().displayImage(ivurl, ivMyicon, options);

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
