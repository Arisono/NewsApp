package com.news.app;


import android.util.Log;

import com.news.util.base.AppUtils;
import com.news.util.imageloader.UniversalAndroidImageLoader;

/**
 * Created by Arsiono on 2015/12/20.
 */
public class Application extends android.app.Application{

    private String TAG="Application";

    private static Application INSTANCE = null;

    public static Application getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init(){
        INSTANCE=this;
        //初始化图片加载器
        UniversalAndroidImageLoader.init(getApplicationContext());
        appLog();
    }


    public void appLog(){
        Log.i(TAG, "Application:appLog:31:App Name:" + AppUtils.getAppName(this));
        Log.i(TAG, "Application:appLog:31:App VerisonName:" + AppUtils.getAppVersionName(this));
        Log.i(TAG,"Application:appLog:36:App MaxMemory:"+AppUtils.getAppMaxMemory()+"kB");
    }

}
