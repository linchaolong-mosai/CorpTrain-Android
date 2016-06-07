package com.mosai.corporatetraining.ui;

import android.app.Dialog;
import android.content.Context;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/6/7 0007 11:39
 * 邮箱：nianbin@mosainet.com
 */
public class RatringDialog extends Dialog{
    private RatingBar ratingbar;
    private TextView tvSubmit;
    private Context mContext;
    public RatringDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
    }
}
