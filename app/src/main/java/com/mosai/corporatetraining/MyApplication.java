package com.mosai.corporatetraining;

import android.app.Application;

import com.mosai.corporatetraining.common.AbnormalHandler;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.network.AsyncHttp;
import com.mosai.corporatetraining.util.Utils;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by Rays on 16/5/9.
 */
public class MyApplication extends Application {
    public static MyApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        AsyncHttp.getInstance().init(this);
        UserPF.getInstance().init(this);
        initImageLoader();
        initAbnormalHandler();
        initDirs();
    }

    private void initAbnormalHandler() {
        if (BuildConfig.DEBUG) {
            AbnormalHandler crashHandler = AbnormalHandler.getInstance();
            crashHandler.init(getApplicationContext());

        }
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
//        MemoryCacheUtils.removeFromCache(UserPF.getInstance().getAvatarUrl(), ImageLoader.getInstance().getMemoryCache());
//        DiskCacheUtils.removeFromCache(UserPF.getInstance().getAvatarUrl(), ImageLoader.getInstance().getDiskCache());
        ImageLoader.getInstance().getDiskCache().remove(UserPF.getInstance().getAvatarUrl());
        ImageLoader.getInstance().getMemoryCache().remove(UserPF.getInstance().getAvatarUrl());
    }

    private void initDirs() {
        File file = new File(Utils.getMaterialsDir(this));
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
