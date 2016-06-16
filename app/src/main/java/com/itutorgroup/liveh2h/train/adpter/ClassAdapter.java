package com.itutorgroup.liveh2h.train.adpter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.classesforcourse.Classes;
import com.itutorgroup.liveh2h.train.util.Utils;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;
import com.mosai.utils.DateTimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/26 0026 15:18
 * 邮箱：nianbin@mosainet.com
 */
public class ClassAdapter extends CommonAdapter<Classes> {
    private DisplayImageOptions options;

    public ClassAdapter(Context context, List<Classes> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.ic_class)
                .showImageOnFail(R.drawable.ic_class)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
        Classes classes = listDatas.get(position);
        ImageView ivIcon = holder.getView(R.id.iv_class);
        Utils.displayImage(classes.getClassInfo().getClassId(),classes.getClassInfo().getImageName(),ivIcon,options);
//        ImageLoader.getInstance().displayImage(Utils.getClassImgUrl(classes.getClassInfo().getClassId(),classes.getClassInfo().getImageName()),ivIcon);
        TextView tvClassName = holder.getView(R.id.tv_classname);
        TextView tvDuedate = holder.getView(R.id.tv_duedate);
        tvClassName.setText(classes.getClassInfo().getSubject());
        String desc = String.format("Due\t%s\t%d",DateTimeUtil.getFormatDate(new Date(classes.getClassInfo().getCreateTime()))
        ,classes.getClassInfo().percent)+"%";
        tvDuedate.setText(classes.getClassInfo().percent+"%");
    }
}
