package com.itutorgroup.liveh2h.train.adpter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.constants.Constants;
import com.mosai.ui.HorizontalProgressBarWithNumber;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;

import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/26 0026 15:18
 * 邮箱：nianbin@mosainet.com
 */
public class ClassResourceAdapter extends CommonAdapter<Resources> {
//    private DisplayImageOptions options;
    private Context mContext;
    public ClassResourceAdapter(Context context, List<Resources> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
        mContext = context;
    }

    @Override
    protected void fillData(CommonViewHolder holder, final int position) {
        final Resources resources = listDatas.get(position);
        TextView tvClassName = holder.getView(R.id.tv_classname);
        TextView tvDesc = holder.getView(R.id.tv_duedate);
        tvDesc.setText(resources.percent+"%");
        tvClassName.setText(resources.getName());
        ImageView ivIcon = holder.getView(R.id.iv_class);
        HorizontalProgressBarWithNumber horizontalProgressBarWithNumber = holder.getView(R.id.hprogressbar);
        TextView tvState = holder.getView(R.id.tv_state);
        if(resources.getResourceType()== Constants.ResourceTypeQuiz||resources.getResourceType()==Constants.ResourceTypeSurvey){
            if(resources.getResourceType()== Constants.ResourceTypeQuiz){
                tvState.setText(mContext.getString(R.string.start_quiz));
            }else{
                tvState.setText(mContext.getString(R.string.open_survey));
            }
            horizontalProgressBarWithNumber.setVisibility(View.GONE);
        }else{
            if(resources.exist){
                tvState.setText(mContext.getString(R.string.open));
                horizontalProgressBarWithNumber.setVisibility(View.GONE);
            }else{

                if(resources.showProgress){
                    horizontalProgressBarWithNumber.setMax(100);
                    int progress = (int)((resources.currentcount*0.01f)/(resources.totalcount*0.01f)*100);
                    tvState.setText(mContext.getString(R.string.downloading));
                    horizontalProgressBarWithNumber.setVisibility(View.VISIBLE);
                    horizontalProgressBarWithNumber.setProgress(progress);
                }else{
                    tvState.setText(mContext.getString(R.string.download));
                    horizontalProgressBarWithNumber.setVisibility(View.GONE);
                }
            }

        }

        //判断图标类型
        if(resources.getResourceType()== Constants.ResourceTypeSurvey){
                ivIcon.setImageResource(R.drawable.ic_survey);
        }else if(resources.getResourceType()== Constants.ResourceTypeVideo){
            ivIcon.setImageResource(R.drawable.ic_video);
        }else if(resources.getResourceType()== Constants.ResourceTypeQuiz){
            ivIcon.setImageResource(R.drawable.ic_quiz);
        }else if(resources.getResourceType()== Constants.ResourceTypeImage){
            ivIcon.setImageResource(R.drawable.ic_img);
        }else {
            ivIcon.setImageResource(R.drawable.ic_class);
        }
        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickStateTextViewCallback!=null){
                    clickStateTextViewCallback.callback(resources,position);
                }
            }
        });
    }

    public void setClickStateTextViewCallback(ClickStateTextViewCallback clickStateTextViewCallback) {
        this.clickStateTextViewCallback = clickStateTextViewCallback;
    }

    private ClickStateTextViewCallback clickStateTextViewCallback;
    public interface ClickStateTextViewCallback{
        void callback(Resources resources,int position);
    }
}
