package com.news.base.activity;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.news.util.base.LogUtils;

/**
 * Created by Administrator on 2016/3/25.
 * 基本的抽象父类 BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {

    public String TAG;//具体子类的Tag
    public Context ct;
    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=initTag();
        LogUtils.i(TAG, "onCreate()");
        ct=this;
        activity=this;
        initView();
        initData();
    }

    public abstract void initView();
    public abstract void initData();
    /**
     * @desc:初始化tag
     * @author：Administrator on 2016/5/3 16:11
     */
    public abstract String initTag();

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy()");
    }
}
