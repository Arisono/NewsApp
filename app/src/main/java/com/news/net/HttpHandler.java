package com.news.net;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2015/12/21.
 */
public class HttpHandler extends Handler {
    private Context context;
    private HttpDataCallBack callBack;

    public HttpHandler(Context ct,HttpDataCallBack callBack){
        this.context=ct;
        this.callBack=callBack;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case HttpListener.EVENT_GET_DATA_SUCCESS:
                    if(msg.obj==null){
                       callBack.onFailed();
                      }else{
                       callBack.processData(msg.obj,true);
                     }
                break;
            case HttpListener.EVENT_CLOSE_SOCKET:
                callBack.onFailed();

                break;
            case HttpListener.EVENT_GET_DATA_EEEOR:
                callBack.onFailed();

                break;
            case HttpListener.EVENT_NOT_NETWORD:
                callBack.onFailed();

                break;
            default:

                break;

        }
        callBack.onFinish();
    }
}
