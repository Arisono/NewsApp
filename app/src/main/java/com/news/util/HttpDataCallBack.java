package com.news.util;


/**
 * Created by Administrator on 2015/12/21.
 */
public interface HttpDataCallBack {

    public abstract void onStart();

    public abstract void processData(Object paramObject, boolean paramBoolean);

    public abstract void onFinish();

    public abstract void onFailed();
}
