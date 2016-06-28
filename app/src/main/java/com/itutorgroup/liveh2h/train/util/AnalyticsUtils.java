package com.itutorgroup.liveh2h.train.util;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.itutorgroup.liveh2h.train.MyApplication;
import com.itutorgroup.liveh2h.train.R;
import com.orhanobut.logger.Logger;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年06月24日 13:18
 * 邮箱：nianbin@mosainet.com
 */
public class AnalyticsUtils {
    public static void setEvent(Context context,int arrayId){
        if(!context.getResources().getBoolean(R.bool.isAnalytics))
            return;
        if(!context.getResources().getBoolean(R.bool.isEventAnalytics))
            return;
        if(((Activity)context).isFinishing())
            return;
        Tracker mTracker = MyApplication.INSTANCE.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(context.getResources().getStringArray(arrayId)[0])
                .setAction(context.getResources().getStringArray(arrayId)[1])
                .build());
        Logger.t("event").e(String.format("Category:%s,Action:%s",context.getResources().getStringArray(arrayId)[0],context.getResources().getStringArray(arrayId)[1]));
    }
    public static void setScreen(Context context, String screenName){
        if(!context.getResources().getBoolean(R.bool.isAnalytics))
            return;
        if(!context.getResources().getBoolean(R.bool.isScreenAnalytics))
            return;
        Tracker mTracker = MyApplication.INSTANCE.getDefaultTracker();
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Logger.t("event").e("ScreenName:"+screenName);
    }
}
