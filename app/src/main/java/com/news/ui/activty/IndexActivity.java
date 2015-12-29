package com.news.ui.activty;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.news.app.Constants;
import com.news.util.HttpClientUtil;
import com.news.util.HttpDataCallBack;
import com.news.util.NetUtils;
import com.news.util.R;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IndexActivity extends AppCompatActivity {

    private String TAG="IndexActivity";
    private TextView tv_result;

    private String html_text="<div style=\"text-align: center;\"><b style=\"font-family: 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53; font-size: 16px; line-height: 32px;\">习近平会见美国总统奥巴马</b></div><p style=\"margin: 15px 0px; padding: 0px; font-size: 16px; line-height: 2em; font-family: 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53;\">       新华社巴黎11月30日电（记者 应强、霍小光、王丰丰）国家主席习近平30日在巴黎会见美国总统奥巴马，就中美关系发展及共同关心的国际和地区问题深入交换意见。双方同意继续推进中美关系向前发展，加强应对气候变化领域合作，共同维护世界和平稳定，推动世界经济稳定增长。</p><p style=\"margin: 15px 0px; padding: 0px; font-size: 16px; line-height: 2em; font-family: 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53;\">　　习近平指出，今年9月，我对美国进行了成功的国事访问，推动中美关系取得新进展，双方在经贸、两军、人文等领域以及重大国际地区问题上保持了有效协调和合作。</p><p style=\"margin: 15px 0px; padding: 0px; font-size: 16px; line-height: 2em; font-family: 'Microsoft YaHei', u5FAEu8F6Fu96C5u9ED1, Arial, SimSun, u5B8Bu4F53;\">　　习近平强调，当前形势下，中美两国要牢牢把握构建新型大国关系正确方向，坚持不冲突不对抗、相互尊重、合作共赢原则，从双边、地区、全球各个层面推进两国务实交流合作，同时以建设性方式管控分歧和敏感问题，确保两国关系持续健康稳定发展，维护和促进亚太地区乃至世界和平、稳定、繁荣。双方要保持高层和各级别交往，规划好两国机制性对话磋商并确保取得更多积极和务实成果。在当前世界经济复苏乏力情况下，各国应该重视稳增长，反对贸易保护主义，维护开放、透明、包容的多边贸易体制。双方要加强宏观经济政策和国际金融框架内的协调和合作，争取早日完成双边投资协定谈判，为两国企业到对方国家投资提供公平竞争环境。当前，国际形势复杂多变，面临一系列紧迫问题和挑战，中美应该加强战略沟通和协调，为推动重大国际和地区问题妥善解决作出更大贡献。</p>";

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
        Logger.init("IndexActivity").setLogLevel(LogLevel.FULL);
        String url=Constants.API_NEWS;
        String datetime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final Map<String,Object> param=new HashMap<>();
        param.put("showapi_appid", "12041");
        param.put("showapi_sign", "67f7892db890407f95cdf39f870b1234");
        param.put("showapi_timestamp", datetime);

       // httpResquest(url, param);
        url=Constants.API_NEWS_CHANNEL;
        httpResquest(url,param);



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

    private void httpResquest(String url, Map<String, Object> param) {
        NetUtils.httpResquest(this, url, param, Constants.HTTP_GET, new HttpDataCallBack() {
            @Override
            public void onStart() {
                //Log.i(TAG, "http start...");
            }

            @Override
            public void processData(Object paramObject, boolean paramBoolean) {
                Log.i(TAG, "json:" + paramObject.toString());
               // tv_result.setText(paramObject.toString());

                tv_result.setText(Html.fromHtml(html_text));
                tv_result.setMovementMethod(LinkMovementMethod.getInstance());//点击的时候产生超链接
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
