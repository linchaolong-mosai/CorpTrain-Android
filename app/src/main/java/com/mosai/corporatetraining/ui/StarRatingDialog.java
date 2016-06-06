package com.mosai.corporatetraining.ui;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;

import com.mosai.corporatetraining.R;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/24 0024 22:34
 * 邮箱：zhounianbin@mastercom.cn
 */
public class StarRatingDialog {
    private MaterialDialog mMaterialDialog;
    public void setTitle(String title){
        if(mMaterialDialog!=null){
            mMaterialDialog.setTitle(title);
        }
    }
    public void setMessage(String message){

        if(mMaterialDialog!=null){
            mMaterialDialog.setTitle(message);
        }
    }
    public interface onSubmitCallback{
        void submitCallback(float rating);
    }
    private onSubmitCallback onSubmitCallback;

    public void setOnSubmitCallback(StarRatingDialog.onSubmitCallback onSubmitCallback) {
        this.onSubmitCallback = onSubmitCallback;
    }

    private RatingBar ratingBar;
    public StarRatingDialog(Context context){
        View view = View.inflate(context, R.layout.view_star_rating,null);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
        mMaterialDialog = new MaterialDialog(context)
//                .setTitle("Rating")
                .setContentView(view)
                .setPositiveButton(context.getString(R.string.submit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        if(onSubmitCallback!=null)
                        onSubmitCallback.submitCallback(ratingBar.getRating());
                    }
                });
        mMaterialDialog.setCanceledOnTouchOutside(true);
//                .setNegativeButton("CANCEL", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mMaterialDialog.dismiss();
//                    }
//                });
    }
    public void show(){
        if(mMaterialDialog!=null){
            mMaterialDialog.show();
        }
    }
}
