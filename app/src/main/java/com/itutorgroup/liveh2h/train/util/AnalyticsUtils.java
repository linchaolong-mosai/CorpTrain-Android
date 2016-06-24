package com.itutorgroup.liveh2h.train.util;

import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.itutorgroup.liveh2h.train.MyApplication;
import com.itutorgroup.liveh2h.train.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年06月24日 13:18
 * 邮箱：nianbin@mosainet.com
 */
public class AnalyticsUtils {
    public static void setTracker(Context context,int arrayId){
        setTracker(context,context.getResources().getStringArray(arrayId)[0]);
    }
    public static void setEvent(Context context,int arrayId){
        if(!context.getResources().getBoolean(R.bool.isAnlytics))
            return;
        Tracker mTracker = MyApplication.INSTANCE.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(context.getResources().getStringArray(arrayId)[1])
                .setAction(context.getResources().getStringArray(arrayId)[2])
                .build());
    }
    public static void setTracker(Context context,String screenName){
        if(!context.getResources().getBoolean(R.bool.isAnlytics))
            return;
        Tracker mTracker = MyApplication.INSTANCE.getDefaultTracker();
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    public static void setEvent(Context context,String categoryName,String actionName){
        if(!context.getResources().getBoolean(R.bool.isAnlytics))
            return;
        Tracker mTracker = MyApplication.INSTANCE.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(categoryName)
                .setAction(actionName)
                .build());
    }
    public static void setAnalyticsConfig(Context context,String screenName,String categoryName,String actionName){
        setTracker(context,screenName);
        setEvent(context,categoryName,actionName);
    }
    public static void setAnalyticsConfigFromXml(Context context,int arrayId){
        String[] config = context.getResources().getStringArray(arrayId);
        setAnalyticsConfig(context,config[0],config[1],config[2]);
    }
}
