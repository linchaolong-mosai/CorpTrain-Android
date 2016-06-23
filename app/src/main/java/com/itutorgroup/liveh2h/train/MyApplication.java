package com.itutorgroup.liveh2h.train;

import android.app.Application;

import com.itutorgroup.liveh2h.train.common.AbnormalHandler;
import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.network.AsyncHttp;
import com.itutorgroup.liveh2h.train.util.Utils;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.orhanobut.logger.Logger;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

/**
 * Created by Rays on 16/5/9.
 */
public class MyApplication extends Application {

    public static MyApplication INSTANCE;
    private Tracker mTracker;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        AsyncHttp.getInstance().init(this);
        UserPF.getInstance().init(this);
        initImageLoader();
        initAbnormalHandler();
        initDirs();
        initLogger();
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
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
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
}
