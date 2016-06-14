package com.mosai.corporatetraining.adpter;

import android.content.Context;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.coursecomment.Comments;
import com.mosai.corporatetraining.util.Utils;
import com.mosai.ui.CircleImageView;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/25 0025 11:14
 * 邮箱：zhounianbin@mastercom.cn
 */
public class CourseCommentAdapter extends CommonAdapter<Comments>{
    private DisplayImageOptions options;

    public CourseCommentAdapter(Context context, List<Comments> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.ic_avator_grey)
                .showImageOnFail(R.drawable.ic_avator_grey)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
        CircleImageView circleImageView = holder.getView(R.id.civ_avator);
        TextView tvCreater = holder.getView(R.id.tv_creater);
        TextView tvComment = holder.getView(R.id.tv_comment);
        Comments comments = listDatas.get(position);
        tvComment.setText(comments.getCourseCommentInfo().getComment());
        tvCreater.setText(comments.getCreatorInfo().getCreator());

        ImageLoader.getInstance().displayImage(Utils.getAvatar(comments.getCreatorInfo().getUserSn()),circleImageView,options);
    }
}
