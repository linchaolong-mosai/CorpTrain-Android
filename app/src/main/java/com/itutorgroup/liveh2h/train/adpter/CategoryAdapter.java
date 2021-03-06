package com.itutorgroup.liveh2h.train.adpter;

import android.content.Context;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.Categories;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;

import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/19 0019 13:45
 * 邮箱：zhounianbin@mastercom.cn
 */
public class CategoryAdapter extends CommonAdapter<Categories>{
    public CategoryAdapter(Context context, List<Categories> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
            Categories category = listDatas.get(position);
            TextView textView = holder.getView(R.id.tv_creater);
            textView.setText(category.getName());
    }
}
