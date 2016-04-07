package com.news.app;


import com.news.util.imageloader.UniversalAndroidImageLoader;

/**
 * Created by Arsiono on 2015/12/20.
 */
public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init(){
        //初始化图片加载器
        UniversalAndroidImageLoader.init(getApplicationContext());
    }

}
