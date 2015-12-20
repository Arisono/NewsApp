package com.news.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Arsiono on 2015/12/20.
 */
public class HttpRunnable implements  Runnable{

    private Context context;
    /** 当前访问线程 */
    private Thread currentRequest = null;
    /** http访问结果监听器 */
    private HttpListener listener;
    /** 访问链接 */
    HttpURLConnection conn = null;
    /** 拿到的流 */
    InputStream input = null;
    private static final String ENCODING = "UTF-8";
    public static final int GET_MOTHOD = 1;
    private static final int TIME = 40 * 1000;
    public static final int POST_MOTHOD = 2;
    /**
     * 1： get请求 2： post请求
     */
    private int requestStatus = 1;


    public HttpRunnable(Context mContext, HttpListener listener,
                        int mRequeststatus) {
        this.context = mContext;
        this.requestStatus = mRequeststatus;
        this.listener = listener;
    }


    /**
     * @Description:Post请求触发
     * @throws
     */
    public void httpostRequest() {
        requestStatus = 2;
        currentRequest = new Thread(this);
        currentRequest.start();
    }

    /**
     * @Description:GET请求触发
     * @throws
     */
    public void httpGetRequeest() {
        requestStatus = 1;
        currentRequest = new Thread(this);
        currentRequest.start();
    }


    /**
     * 对请求的字符串进行编码
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String requestEncodeStr(String requestStr)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(requestStr, ENCODING);
    }

    /**
     * @desc: http get()  ---httpUrlConnection
     * @author: Arison
     * @create: 2015/12/20 12:56
     */
    private void  httpGetRequest(){
        try {
            URL url=new URL("");//MalformedURLException
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//IOException
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIME);
            conn.setReadTimeout(TIME);
            int responseCode = conn.getResponseCode();
            if (responseCode==200){
                input=conn.getInputStream();
                if (input!=null){
                    listener.action(HttpListener.EVENT_GET_DATA_SUCCESS,readStream(input));
                }
            }else{


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }catch (SocketTimeoutException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

    }

    /**
     * @desc: 读取输入流的方法
     * @author: Arison
     * @create: 2015/12/20 18:33
     */
    private Object readStream(InputStream inStream) throws Exception {
        String result;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        result = new String(outStream.toByteArray(), ENCODING);
        outStream.close();
        inStream.close();
        return result;
    }
}
