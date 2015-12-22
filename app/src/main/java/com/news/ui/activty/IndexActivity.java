package com.news.ui.activty;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.news.app.Constants;
import com.news.util.HttpClientUtil;
import com.news.util.HttpDataCallBack;
import com.news.util.NetUtils;
import com.news.util.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IndexActivity extends AppCompatActivity {

    private String TAG="IndexActivity";
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tv_result= (TextView) findViewById(R.id.tv_result);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initData();
    }

    /**
     * @desc:
     * @author：Administrator on 2015/12/21 16:40
     */
    public void initData(){
        final String url=Constants.API_NEWS;
        String datetime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final Map<String,Object> param=new HashMap<>();
        param.put("showapi_appid", "12041");
        param.put("showapi_sign", "67f7892db890407f95cdf39f870b1234");
        param.put("showapi_timestamp", datetime);

        NetUtils.httpResquest(this, url, param, Constants.HTTP_POST, new HttpDataCallBack() {
            @Override
            public void onStart() {
                //Log.i(TAG, "http start...");
            }

            @Override
            public void processData(Object paramObject, boolean paramBoolean) {
                Log.i(TAG, "json:" + paramObject.toString());
                tv_result.setText(paramObject.toString());
            }

            @Override
            public void onFinish() {
               // Log.i(TAG, "http end...");
            }

            @Override
            public void onFailed() {
                Log.i(TAG, "http onFail...");
            }
        });

        
        /**
         * @desc:简单的HttpClient封装
         * @author:Administrator on 2015/12/22 11:29
         */
       /* new Thread(new Runnable() {
              @Override
              public void run() {
                  httpTask(url, param);
              }
          }).start();*/

    }

    /**
     * @desc:httpClient
     * @author：Administrator on 2015/12/22 11:17
     */
    @Deprecated
    private void httpTask(String url, Map<String, Object> param) {
        try {
         HttpClientUtil.Response response= HttpClientUtil.sendGetHeaderRequest(url, param, null);
         Log.i(TAG, "httpclient:" + response.getStatusCode());
         Log.i(TAG,"httpclient:"+response.getResponseText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
