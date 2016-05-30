package com.mosai.corporatetraining.adpter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.mosai.corporatetraining.R;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;

import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/30 0030 11:05
 * 邮箱：nianbin@mosainet.com
 */
public class SurveyQuestionAdapter extends CommonAdapter<String> {
    public int index=-1;
    public SurveyQuestionAdapter(Context context, List<String> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
    }

    @Override
    protected void fillData(CommonViewHolder holder, final int position) {
        Button btnAnswer = holder.getView(R.id.btn_anwser);
        btnAnswer.setText(listDatas.get(position));
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                if(clickCallback!=null){
                    clickCallback.callback(index);

                }
                notifyDataSetChanged();
            }
        });
        btnAnswer.setSelected(index==position?true:false);
    }
    public interface ClickCallback{
        void callback(int index);
    }
    private ClickCallback clickCallback;

    public void setClickCallback(ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }
}
