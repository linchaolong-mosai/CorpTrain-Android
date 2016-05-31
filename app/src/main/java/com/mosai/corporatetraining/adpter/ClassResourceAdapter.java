package com.mosai.corporatetraining.adpter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.resourseforclass.Resources;
import com.mosai.corporatetraining.constants.Constants;
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
public class ClassResourceAdapter extends CommonAdapter<Resources>{
//    private DisplayImageOptions options;

    public ClassResourceAdapter(Context context, List<Resources> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
//        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
//                .showImageForEmptyUri(R.drawable.ic_class)
//                .showImageOnFail(R.drawable.ic_class)
//                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
        Resources resources = listDatas.get(position);
//        ImageView ivIcon = holder.getView(R.id.iv_class);
//        Utils.displayImage(classes.getClassInfo().getClassId(),classes.getClassInfo().getImageName(),ivIcon,options);
//        ImageLoader.getInstance().displayImage(Utils.getClassImgUrl(classes.getClassInfo().getClassId(),classes.getClassInfo().getImageName()),ivIcon);
        TextView tvClassName = holder.getView(R.id.tv_classname);
//        TextView tvDuedate = holder.getView(R.id.tv_duedate);
        tvClassName.setText(resources.getName());

        HorizontalProgressBarWithNumber horizontalProgressBarWithNumber = holder.getView(R.id.hprogressbar);

        if(resources.getResourceType()== Constants.ResourceTypeQuiz||resources.getResourceType()==Constants.ResourceTypeSurvey){
            horizontalProgressBarWithNumber.setVisibility(View.GONE);
        }else{
            if(resources.showProgress){
                horizontalProgressBarWithNumber.setVisibility(View.VISIBLE);
                horizontalProgressBarWithNumber.setMax(100);
                int progress = (int)((resources.currentcount*0.01f)/(resources.totalcount*0.01f)*100);
                horizontalProgressBarWithNumber.setProgress(progress);
            }else{
                horizontalProgressBarWithNumber.setVisibility(View.GONE);
            }

        }
    }
}
