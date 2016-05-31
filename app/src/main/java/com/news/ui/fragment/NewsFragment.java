package com.news.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.adapter.SimpleAdapter;
import com.news.app.Constants;
import com.news.db.dao.NewsDao;
import com.news.model.db.ImageUrls;
import com.news.model.db.NewEntity;
import com.news.model.db.PageRootBean;
import com.news.net.R;
import com.news.service.interfac.OnItemClickListener;
import com.news.service.interfac.OnItemLongClickListener;
import com.news.ui.activity.BaseWebActivity;
import com.news.util.base.ApiUtils;
import com.news.util.base.ListUtils;
import com.news.util.base.LogUtils;
import com.news.util.imageloader.ImageLoaderFactory;
import com.news.util.imageloader.ImageLoaderWrapper;
import com.news.util.net.HttpDataCallBack;
import com.news.util.net.NetUtils;
import com.news.widget.recyclerView.EndlessRecyclerOnScrollListener;
import com.news.widget.recyclerView.HeaderAndFooterRecyclerViewAdapter;
import com.news.widget.recyclerView.divider.DividerLine;
import com.news.widget.recyclerView.footer.LoadingFooter;
import com.news.widget.recyclerView.footer.RecyclerViewStateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/4.
 * 新闻列表
 */
public class NewsFragment extends Fragment{

    private String TAG="NewsFragment";
    public int page=1;
    @Bind(R.id.mlist)
    public RecyclerView mlist;
    private SimpleAdapter adapter;
    @Bind(R.id.swipe_refresh_layout)
    public  SwipeRefreshLayout swipe_refresh_layout;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;
    private List<NewEntity> contentlists=new ArrayList<>();

    private String name;
    private String channelId;
    private Activity activity;
    private boolean isFirstLoad=true;
    public SearchView mSearchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i(TAG, name + "------onCreateView()---------");
        View view=inflater.inflate(R.layout.fragment_news,container,false);
        ButterKnife.bind(this, view);
        activity=getActivity();
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG, name + ":onCreate()");
        this.name= getArguments().getString("name");
        this.channelId= getArguments().getString("channelId");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogUtils.i("setUserVisibleHint() isFirstLoad:"+isFirstLoad);
            if (isFirstLoad){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_refresh_layout.setRefreshing(true);
                        initData(page);
                    }
                },100);
             }
        }
    }


    public void initView(){
        mlist.setLayoutManager(new LinearLayoutManager(mlist.getContext()));
        adapter=new SimpleAdapter(getActivity(),contentlists);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object object) {
                NewEntity data = (NewEntity) object;
                Intent intent = new Intent(activity, BaseWebActivity.class);
                intent.putExtra("url", data.getLink());
                intent.putExtra("title", data.getTitle());
                activity.startActivity(intent);
            }
        });

        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0x00000000);
        dividerLine.setSpace(10);
        mlist.addItemDecoration(dividerLine);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        mlist.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentlists.clear();//下拉刷新，清空列表
                // adapter.notifyDataSetChanged(); //clear this is create a bug for
                //或者禁止滑動
                swipe_refresh_layout.setRefreshing(true);
                page=1;
                loadData(page);
            }
        });

        mlist.addOnScrollListener(mOnScrollListener);
        //刷新禁止滑动
        mlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (swipe_refresh_layout.isRefreshing()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG, name + ":onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(TAG, name+":onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i(TAG, name + ":onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, name + ":onDestroy()");
    }

    /**
    * @desc:initdata
    * @author：Administrator on 2016/1/4 16:02
    */
   public void initData(int page){
       final List<NewEntity> newEntities= NewsDao.getInstance().findNewsByChannelId(channelId,page);
       if (ListUtils.isEmpty(newEntities)){
           LogUtils.i("初始化网络请求:"+page);
           loadData(page);
       }else{
           LogUtils.i("初始化数据库数据:"+page);
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  for(int i=0;i<newEntities.size();i++){
                      List<ImageUrls> imageUrlsList=NewsDao.getInstance().findImagesByNewsId(newEntities.get(i).getId());
                      newEntities.get(i).setImageurls(imageUrlsList);
                  }
                  contentlists.addAll(newEntities);
                  RecyclerViewStateUtils.setFooterViewState(mlist, LoadingFooter.State.Normal);
                  adapter.notifyDataSetChanged();
                  swipe_refresh_layout.setRefreshing(false);
              }
          },200);
       }
   }


    public void  loadData(int count){
        String url=Constants.API_NEWS;
        String datetime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final Map<String,Object> param=new HashMap<>();
        param.put("showapi_appid", Constants.NEWS_SHOWAPI_APPID);
        param.put("showapi_sign", Constants.NEWS_SHOWAPI_SIGN);
        param.put("showapi_timestamp", datetime);
        param.put("channelId", channelId);
        param.put("page",count);
        //param.put("channelName", "国内最新");
        NetUtils.httpResquest(activity, url, param, Constants.HTTP_GET, new HttpDataCallBack() {
            @Override
            public void onStart() {
                LogUtils.i(TAG, "开始加载数据：" + name);
            }

            @Override
            public void processData(Object paramObject, boolean paramBoolean) {
                Log.i(TAG, "json:" + paramObject.toString());
                displayUi(paramObject);
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailed() {
                isFirstLoad = true;
                Log.i(TAG, "http onFail...");
            }
        });
    }

    /**
     * @desc: 处理网络请求返回数据
     * @author: Arison
     * @create: 2016/4/21 21:16
     */
    private void displayUi(Object paramObject) {
        RecyclerViewStateUtils.setFooterViewState(mlist, LoadingFooter.State.Normal);
        PageRootBean<NewEntity> rootEntity= ApiUtils.parseNewsList(paramObject.toString(), NewEntity.class);
        List<NewEntity> newEntities=rootEntity.getShowapi_res_body().getPagebean().getContentlist();
        NewsDao.getInstance().saveAll(newEntities);
        if (newEntities.size()>0) {
            contentlists.addAll(newEntities);
        }else{
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mlist, 20,page, LoadingFooter.State.TheEnd, null);
            --page;
        }
        adapter.notifyDataSetChanged();
        isFirstLoad=false;
        swipe_refresh_layout.setRefreshing(false);
    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mlist);
            if(state == LoadingFooter.State.Loading) {
                return;
            }
            initData(++page);
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mlist, 20, page, LoadingFooter.State.Loading, null);
        }
    };









}
