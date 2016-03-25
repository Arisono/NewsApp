package com.news.util.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;

import java.util.Map;

/**
 * 网络工具类
 * Created by Administrator on 2015/12/21.
 */
public class NetUtils {

     /**
      * @desc:判断网络是否连接
      * @author：Administrator on 2015/12/21 15:20
      */
    public static boolean isNetworkConnected(Context ct) {
        ConnectivityManager cm = (ConnectivityManager) ct
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni!=null&&ni.isConnectedOrConnecting();
    } 
    
    
    /**
     * @desc:HttpUrlConnection
     * @param: url,param
     * @author：Administrator on 2015/12/21 15:20
     */
    public static HttpRunnable httpResquest(Context ct,
                                    String url,
                                    Map<String,Object> param,
                                    int requestType,
                                    HttpDataCallBack callBack){
        final  HttpHandler handler=new HttpHandler(ct,callBack);
        HttpRunnable task=new HttpRunnable(ct, new HttpListener() {
            @Override
            public void action(int actionCode, Object object) {
                //处理
                Message msg=new Message();
                switch (actionCode){
                    case HttpListener.EVENT_GET_DATA_SUCCESS:
                        msg.obj = object;
                        msg.what =HttpListener.EVENT_GET_DATA_SUCCESS;
                        break;
                    case HttpListener.EVENT_GET_DATA_EEEOR:
                        msg.what =HttpListener.EVENT_GET_DATA_EEEOR;
                        break;
                    case HttpListener.EVENT_NETWORD_EEEOR:
                        msg.what =HttpListener.EVENT_NETWORD_EEEOR;
                        break;
                    case HttpListener.EVENT_CLOSE_SOCKET:
                        msg.what =HttpListener.EVENT_CLOSE_SOCKET;
                        break;
                    case HttpListener.EVENT_NOT_NETWORD:
                        msg.what =HttpListener.EVENT_NOT_NETWORD;
                        break;
                    default:
                        break;
                }
                handler.sendMessage(msg);
            }
        },requestType);

        callBack.onStart();

        if(requestType==HttpRunnable.GET_MOTHOD){
            task.httpUrlGetRequest(url,param);
          }else if(requestType==HttpRunnable.POST_MOTHOD){
            task.httpUrlPostRequest(url, param);
         }
       return  task;
    }
}
