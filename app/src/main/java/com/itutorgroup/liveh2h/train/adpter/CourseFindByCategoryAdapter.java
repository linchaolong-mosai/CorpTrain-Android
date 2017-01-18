package com.itutorgroup.liveh2h.train.adpter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.CourseFindByCategory;
import com.itutorgroup.liveh2h.train.bean.usercourse.CourseInfo;
import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.util.Utils;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;
import com.mosai.utils.DateTimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.sql.Date;
import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/19 0019 16:31
 * 邮箱：zhounianbin@mastercom.cn
 */
public class CourseFindByCategoryAdapter extends CommonAdapter<Courses> {
    private DisplayImageOptions options;
    public CourseFindByCategoryAdapter(Context context, List<Courses> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.bg_course_default_cover_little)
                .showImageOnFail(R.drawable.bg_course_default_cover_little)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
        Courses courseFindByCategory = listDatas.get(position);
        CourseInfo courseInfo = courseFindByCategory.getCourseInfo();
        String imgurl = Utils.getImgUrl(courseInfo.getCourseId(), courseInfo.getImageName());
        String subject = courseInfo.getSubject();
        int viewcount = courseInfo.getViewCount();
        float rating = courseInfo.getRating();
        int lession = courseFindByCategory.getClassCount();
        long publishtime = courseInfo.getPublishTime();

        TextView tvLesson = holder.getView(R.id.tv_lessoncount);
        TextView tvDueDate = holder.getView(R.id.tv_duedate);
        ImageView iv = holder.getView(R.id.iv_courseicon);
        TextView tvViewcount = holder.getView(R.id.tv_viewcount);
        TextView tvRatecount = holder.getView(R.id.tv_ratecount);
        TextView tvSubject = holder.getView(R.id.coursename);

        setImage(imgurl,iv);
        tvViewcount.setText(viewcount+"");
        tvRatecount.setText(rating+"");
        tvSubject.setText(subject);
        tvLesson.setText(lession+" lesson");

        tvDueDate.setText(mContext.getString(R.string.creat_date)+" "+DateTimeUtil.getFormatDate(new Date(publishtime)));
    }
    private void setImage(String url,ImageView iv){
        ImageLoader.getInstance().displayImage(url, iv, options, null);

    }
}
