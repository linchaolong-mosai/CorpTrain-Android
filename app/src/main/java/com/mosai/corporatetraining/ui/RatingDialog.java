package com.mosai.corporatetraining.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mosai.corporatetraining.R;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/6/7 0007 11:39
 * 邮箱：nianbin@mosainet.com
 */
public class RatingDialog extends Dialog{
    private RatingBar ratingbar;
    private TextView tvSubmit;
    private Context mContext;
    public RatingDialog(Context context) {
        super(context);
        mContext = context;
        init();
    }
    private void init() {
        View view = View.inflate(mContext, R.layout.view_rating_dialog,null);
        ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
        tvSubmit = (TextView) view.findViewById(R.id.tv_submit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.callback(ratingbar.getRating());
                    if(isShowing()){
                        dismiss();
                    }
                }
            }
        });
        setContentView(view);
    }
    public interface Callback{
        void callback(float rate);
    }
    private Callback callback;
    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
