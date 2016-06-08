package com.mosai.corporatetraining.adpter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.bean.quiz.Answer;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;

import java.util.List;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/27 0027 13:28
 * 邮箱：nianbin@mosainet.com
 */
public class QuizQuestionAdapter extends CommonAdapter<Answer> {
    public int index = -1;

    public QuizQuestionAdapter(Context context, List<Answer> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
    }

    @Override
    protected void fillData(CommonViewHolder holder, final int position) {
        Answer answer = listDatas.get(position);
        final ImageView ivAnwser = holder.getView(R.id.iv_answer);
        final LinearLayout ll = holder.getView(R.id.ll);
        TextView tvAnswer = holder.getView(R.id.tv_answer);
        tvAnswer.setText(answer.getAnswer());
        holder.getMConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
            }
        });
//        ll.setOnClickListener(null);
//        ivAnwser.setOnClickListener(null);
//        ll.setSelected(index == position);
//        ivAnwser.setSelected(index == position);
        ll.setBackgroundResource(index==position?R.color.pressed_bgcolor_green:R.color.white);
        ivAnwser.setImageResource(index == position ? R.drawable.ic_check_checked : R.drawable.ic_check_unchecked);
    }
}
