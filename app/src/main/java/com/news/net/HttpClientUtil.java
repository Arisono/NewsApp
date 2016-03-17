package com.news.net;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** http client
 * Created by Arsiono on 2015/12/21.
 */
public class HttpClientUtil {

    /**
     * @author LiuJie
     * @功能:Get 参数,请求头
     */
    public static Response sendGetHeaderRequest(String url,
                                                Map<String, Object> params,
                                                LinkedHashMap<String, Object> headers
                                                ) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            StringBuilder buf = new StringBuilder(url);
            if (url.indexOf("?") == -1)
                buf.append("?");
            else if (!url.endsWith("&"))
                buf.append("&");
            if (params != null && !params.isEmpty()) {
                Set<Map.Entry<String, Object>> entrys = params.entrySet();
                for (Map.Entry<String, Object> entry : entrys) {
                    buf.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"))
                            .append("&");
                }
            }
            buf.deleteCharAt(buf.length() - 1);
            System.out.println("请求url:"+buf.toString());
            HttpGet httpGet = new HttpGet(buf.toString());
            if (headers!=null) {
                for(String key:headers.keySet()){
                    System.out.println("add header:"+key+" value:"+headers.get(key).toString());
                    httpGet.setHeader(key, headers.get(key).toString());
                }
            }
            response = httpClient.execute(httpGet);
            return Response.getResponse(response);
        } finally {

        }
    }

    /**
     * @author LiuJie
     * @功能:post请求  添加请求头参数
     */
    public static Response sendPostHeaderRequest(String url,
                                                 Map<String, Object> params,LinkedHashMap<String, Object> headers, boolean sign) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null && !params.isEmpty()) {
                Set<Map.Entry<String, Object>> entrys = params.entrySet();
                for (Map.Entry<String, Object> entry : entrys) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), URLEncoder
                            .encode(entry.getValue().toString(), "utf-8")));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            if (headers!=null) {
                for(String key:headers.keySet()){
                    System.out.println("add header:"+key+" value:"+headers.get(key).toString());
                    httpPost.setHeader(key, headers.get(key).toString());
                }
            }
            response = httpClient.execute(httpPost);

            return Response.getResponse(response);
        } finally {

        }
    }



    /**
     * @author LiuJie
     * @功能: post  json参数
     */
    public static Response sendPostJsonRequest(String url,
                                               Map<String, Object> params,LinkedHashMap<String, Object> headers,String bodyString, boolean sign) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            if (headers!=null) {
                for(String key:headers.keySet()){
                    System.out.println("add header:"+key+" value:"+headers.get(key).toString());
                    httpPost.setHeader(key, headers.get(key).toString());
                }
            }
            if (bodyString!=null) {
                httpPost.setEntity(new StringEntity(bodyString,"UTF-8"));
            }
            response = httpClient.execute(httpPost);
            return Response.getResponse(response);
        } finally {

        }
    }

    public static class Response {
        private int statusCode;
        private String responseText;
        public  static CookieStore cookieStore;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getResponseText() {
            return responseText;
        }

        public void setResponseText(String responseText) {
            this.responseText = responseText;
        }


        public CookieStore getCookieStore() {
            return cookieStore;
        }

        public void setCookieStore(CookieStore cookieStore) {
            Response.cookieStore = cookieStore;
        }

        public Response() {
        }

        public Response(HttpResponse response) throws IllegalStateException,
                IOException, Exception {

            this.statusCode = response.getStatusLine().getStatusCode();
            this.responseText = HttpClientUtil.read2String(response.getEntity()
                    .getContent());
        }

        public static Response getResponse(HttpResponse response)
                throws IllegalStateException, IOException, Exception {
            if (response != null)
                return new Response(response);
            return null;
        }
    }



    public static String read2String(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        try {
            outSteam.close();
            inStream.close();
        } catch (Exception e) {

        }
        return new String(outSteam.toByteArray(), "UTF-8");
    }
}
