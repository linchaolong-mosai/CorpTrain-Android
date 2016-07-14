package com.itutorgroup.liveh2h.train;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.itutorgroup.liveh2h.train.common.AbnormalHandler;
import com.itutorgroup.liveh2h.train.entity.UserInfoResponse;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.AsyncHttp;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.network.https.AuthImageDownloader;
import com.itutorgroup.liveh2h.train.util.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.orhanobut.logger.Logger;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * Created by Rays on 16/5/9.
 */
public class MyApplication extends Application {

    public static MyApplication INSTANCE;
    private Tracker mTracker;
    public enum APP_DISTRIBUTION{
        GOOGLE_PLAY_STORE,
        CHINESE_MARKET
    }

    public APP_DISTRIBUTION getAppDistribution() {
        return appDistribution;
    }

    private APP_DISTRIBUTION appDistribution;


    @Override
    public void onCreate() {
        super.onCreate();
        appDistribution = APP_DISTRIBUTION.GOOGLE_PLAY_STORE;
        INSTANCE = this;
        AsyncHttp.getInstance().init(this);
        UserPF.getInstance().init(this);
        initImageLoader();
        initAbnormalHandler();
        initDirs();
        initLogger();
        forceUpdateCheck();
        setActivityLifecycleCallbacks();
    }

    private void forceUpdateCheck(){
        AppAction.forceUpdate(getApplicationContext(), new  HttpResponseHandler(getApplicationContext(), com.itutorgroup.liveh2h.train.entity.HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, com.itutorgroup.liveh2h.train.entity.HttpResponse response, String responseString) throws PackageManager.NameNotFoundException {
                if (responseString.contains("\"returnCode\":0") && responseString.contains("\"shouldForceUpdate\":true")) {
                    String versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
                    int index = responseString.indexOf("\"minimalVersion\":");
                    String fuVersionName = responseString.substring(index+17,index+21);
                    Log.e(versionName,"force Update version:"+fuVersionName);
                    if (Float.parseFloat(versionName) < Float.parseFloat(fuVersionName)) {
                        Toast.makeText(getApplicationContext(), R.string.forceUpdateReminder, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void initLogger(){
        Logger.init("znb").setMethodCount(4);
    }
    private void initAbnormalHandler() {
        if (BuildConfig.DEBUG) {
            AbnormalHandler crashHandler = AbnormalHandler.getInstance();
            crashHandler.init(getApplicationContext());

        }
    }

    /**
            * Gets the default {@link Tracker} for this {@link Application}.
            * @return tracker
    */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
             mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    private void initImageLoader() {
        //开始构建
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .imageDownloader(new AuthImageDownloader(this))
                .build();
        ImageLoader.getInstance().init(config);
        MemoryCacheUtils.removeFromCache(UserPF.getInstance().getAvatarUrl(), ImageLoader.getInstance().getMemoryCache());
        DiskCacheUtils.removeFromCache(UserPF.getInstance().getAvatarUrl(), ImageLoader.getInstance().getDiskCache());
    }
    public void initMaterialDir(){
        File file = new File(Utils.getMaterialsDir(this));
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    public void initDirs() {
        initMaterialDir();
    }

    private int appCount = 0;



    private void setActivityLifecycleCallbacks() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }
    public  boolean isForeground() {
        return getAppCount() > 0;
    }
    public boolean foregound = false;

}
