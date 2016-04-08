package com.news.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.news.net.R;
import com.news.widget.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/8.
 */
public class BaseWebActivity extends AppCompatActivity {
    protected ProgressWebView mWebView;
    @Bind(R.id.webview_toolbar)
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去除系统自带标题栏
        setContentView(R.layout.activity_baseweb);

        ButterKnife.bind(this);
        mWebView = (ProgressWebView) findViewById(R.id.base_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        initView();
        initData();
    }

    /**
     * @desc:initView
     * @author：Administrator on 2016/4/8 17:50
     */
    public void initView() {
        toolbar.setNavigationIcon(R.mipmap.icon_back);//设置导航栏图标
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.whitesmoke));
    }

    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title=intent.getStringExtra("title");
        toolbar.setSubtitle(title == null?"知晓新闻":title);//设置子标题
        if(url!=null){
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                    view.loadUrl(url);
                    return true;
                }
            });
            mWebView.loadUrl(url);
        }
    }
}
