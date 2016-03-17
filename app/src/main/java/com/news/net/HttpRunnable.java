package com.news.net;

import android.content.Context;
import android.util.Log;

import com.news.app.Constants;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

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

    private String httpUrl=null;
    private Map<String,Object> param=null;


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
    public void httpUrlPostRequest(String url,Map<String,Object> param) {
        this.httpUrl=url;
        this.param=param;
        requestStatus = 2;
        currentRequest = new Thread(this);
        currentRequest.start();
    }

    /**
     * @Description:GET请求触发
     * @throws
     */
    public void httpUrlGetRequest(String url,Map<String,Object> param) {
        this.httpUrl=url;
        this.param=param;
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
        //拼接参数字符串
        httpUrl=getHttpUrlAndParam(httpUrl,param);
        Log.i("http","url="+httpUrl);
        try {
            URL url=new URL(httpUrl);//MalformedURLException
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

                listener.action(HttpListener.EVENT_NETWORD_EEEOR,null);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();// url is error
        } catch (SocketException e){
            e.printStackTrace();
            listener.action(HttpListener.EVENT_CLOSE_SOCKET,null);
        }catch (SocketTimeoutException e){
            e.printStackTrace();
            listener.action(HttpListener.EVENT_NETWORD_EEEOR, null);
        }catch (IOException e) {
            e.printStackTrace();
            listener.action(HttpListener.EVENT_GET_DATA_EEEOR, null);
        }catch (Exception e){
            e.printStackTrace();
            listener.action(HttpListener.EVENT_NETWORD_EEEOR, null);
        }

    }

    /**
     * @desc:http post---httpUrlConnnection
     * @author：Administrator on 2015/12/21 13:55
     */
    public void httpPostRequest(){
        httpUrl=getHttpUrlAndParam(httpUrl,param);
        String data =httpUrl.split("[?]")[1];
        Log.i("http","url="+httpUrl);
        Log.i("http","param="+data);
        try {
            URL url=new URL(httpUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME);
            conn.setReadTimeout(TIME);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不使用Cache
            conn.setRequestProperty("Charset", ENCODING);
            conn.setRequestProperty("Content-Length",
                    String.valueOf(data.length()));
            conn.setRequestProperty("Content-Type", "text/*;charset=utf-8");
            conn.setRequestMethod("POST");
            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            outStream.write(data.getBytes());
            outStream.flush();
            outStream.close();
            if (conn == null) {
                return;
            }
            int responseCode = conn.getResponseCode();
            if(responseCode==200){
               input=conn.getInputStream();
                if(input!=null){
                 listener.action(HttpListener.EVENT_GET_DATA_SUCCESS,readStream(input));
                  }
              }else if(responseCode==404){
                input=conn.getErrorStream();
                if(input!=null){
                    listener.action(HttpListener.EVENT_GET_DATA_SUCCESS,readStream(input));
                  }else{
                    listener.action(HttpListener.EVENT_NOT_NETWORD,null);
                 }

             }else{
                listener.action(HttpListener.EVENT_NOT_NETWORD,null);
            }
         
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }catch(SocketException e){
            e.printStackTrace();
            listener.action(HttpListener.EVENT_CLOSE_SOCKET, null);
        }catch(SocketTimeoutException e){
            e.printStackTrace();
            listener.action(HttpListener.EVENT_NETWORD_EEEOR, null);
        }catch (IOException e) {
            e.printStackTrace();
            listener.action(HttpListener.EVENT_GET_DATA_EEEOR, null);
        }catch(Exception e){
            e.printStackTrace();
            listener.action(HttpListener.EVENT_NETWORD_EEEOR, null);
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // boolean isHasNet=NetUtils.isNetworkConnected(context);
        if(true){
            if(requestStatus== Constants.HTTP_GET){
                  httpGetRequest();
              }else if(requestStatus==Constants.HTTP_POST){
                  httpPostRequest();
             }
          }else{
             listener.action(HttpListener.EVENT_NOT_NETWORD,null);
         }
    }

    /**
     * @desc:拼接字符串
     * @author：Administrator on 2015/12/21 17:05
     */
    public String getHttpUrlAndParam(String url,Map<String,Object> param)
             {
        StringBuilder buf = new StringBuilder(url);
        Set<Map.Entry<String, Object>> entrys = null;
        if(param!=null&&!param.isEmpty()) {
            if (buf.indexOf("?") == -1)
                buf.append("?");
                entrys=param.entrySet();
            for (Map.Entry<String, Object> entry : entrys) {
                try {
                    buf.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
             }
             buf.deleteCharAt(buf.length() - 1);
              }

       return buf.toString();
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


    /**
     * @Title: isRunning
     * @Description: 判断是否正在访问
     * @return
     * @throws
     */
    public boolean isRunning() {
        if (currentRequest != null && currentRequest.isAlive()) {
            return true;
        }
        return false;
    }



    /**
     * 取消当前HTTP连接处理
     */
    public void cancelHttpRequest() {
        if (currentRequest != null && currentRequest.isAlive()) {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            input = null;
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn = null;
            currentRequest = null;
            System.gc();
        }
    }




}
