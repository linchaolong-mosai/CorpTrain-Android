package com.mosai.corporatetraining.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.util.LogUtils;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/18 0018 14:25
 * 邮箱：zhounianbin@mastercom.cn
 */
public class CourseCoverAdapter extends CommonAdapter<Courses> {
    private DisplayImageOptions options;
    public CourseCoverAdapter(Context context, List<Courses> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
       options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.bg_course_default_cover_little)
                .showImageOnFail(R.drawable.bg_course_default_cover_little)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
        Courses course = listDatas.get(position);
            String imgurl = String.format("%s%s/%s",AppAction.IMG_RESOURSE_COURSE_URL,course.getCourseInfo().getCourseId(),course.getCourseInfo().getImageName());
            LogUtils.e("imgurl:"+imgurl);
            String name = course.getCourseInfo().getSubject();
            int focus = course.getCourseInfo().getViewCount();
            ImageView ivImgurl = holder.getView(R.id.iv_courseicon);
            TextView tvName = holder.getView(R.id.tv_name);
            TextView tvFocus = holder.getView(R.id.tv_focus);
            if(!TextUtils.isEmpty(name)){
                setImage(imgurl,ivImgurl);
            }



            tvName.setText(name);
            tvFocus.setText(focus+"");
    }
    private void setImage(String url,ImageView iv){
        ImageLoader.getInstance().displayImage(url, iv, options, null);

    }
}
