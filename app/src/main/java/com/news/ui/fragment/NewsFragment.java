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
    private ImageLoaderWrapper mImageLoaderWrapper;

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
                        page=1;
                        initData(page);
                    }
                },100);
             }
        }
    }


    public void initView(){
        mImageLoaderWrapper= ImageLoaderFactory.getLoader();

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
        param.put("showapi_appid", "12041");
        param.put("showapi_sign", "67f7892db890407f95cdf39f870b1234");
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

    /**
     * @desc:RecyclerView adapter
     * @author：Administrator on 2016/1/5 15:30
     */
    public class SimpleAdapter extends  RecyclerView.Adapter<SimpleAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener {

        List<NewEntity> contentLists;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;

        private OnItemClickListener onItemClickListener;
        private OnItemLongClickListener onItemLongClickListener;

        SimpleAdapter(Context context,
                      List<NewEntity> items){
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.contentLists=items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_news,parent,false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv_news_title.setText(contentLists.get(position).getTitle());
            holder.tv_news_source.setText(contentLists.get(position).getSource());
            holder.tv_news_time.setText(contentLists.get(position).getPubDate());
            holder.tv_news_desc.setText(contentLists.get(position).getDesc());
            int size=contentLists.get(position).getImageurls().size();
            if (size==1){
                ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
                displayOption.loadingResId = R.mipmap.img_default;
                displayOption.loadErrorResId = R.mipmap.img_error;
                mImageLoaderWrapper.displayImage(holder.iv_news_bigimage, contentLists.get(position).getImageurls().get(0).getUrl(), displayOption);
                holder.ll_image_third.setVisibility(View.GONE);
                holder.ll_image_one.setVisibility(View.VISIBLE);
                holder.iv_news_leftimage.setVisibility(View.GONE);
            }else if(size==3){
                ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
                displayOption.loadingResId = R.mipmap.img_default;
                displayOption.loadErrorResId = R.mipmap.img_error;
                mImageLoaderWrapper.displayImage(holder.iv_news_oneimage, contentLists.get(position).getImageurls().get(0).getUrl(), displayOption);
                mImageLoaderWrapper.displayImage(holder.iv_news_twoimage, contentLists.get(position).getImageurls().get(1).getUrl(), displayOption);
                mImageLoaderWrapper.displayImage(holder.iv_news_thirdimage, contentLists.get(position).getImageurls().get(2).getUrl(), displayOption);
                holder.ll_image_third.setVisibility(View.VISIBLE);
                holder.ll_image_one.setVisibility(View.GONE);
                holder.iv_news_leftimage.setVisibility(View.GONE);
            }else if (size==2){
                ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
                displayOption.loadingResId = R.mipmap.img_default;
                displayOption.loadErrorResId = R.mipmap.img_error;
                mImageLoaderWrapper.displayImage(holder.iv_news_bigimage, contentLists.get(position).getImageurls().get(0).getUrl(), displayOption);
                holder.ll_image_third.setVisibility(View.GONE);
                holder.ll_image_one.setVisibility(View.VISIBLE);
                holder.iv_news_leftimage.setVisibility(View.GONE);
            }else{
                //清空图片
                holder.ll_image_third.setVisibility(View.GONE);
                holder.ll_image_one.setVisibility(View.GONE);
                holder.iv_news_leftimage.setVisibility(View.GONE);
            }

            holder.tv_news_imageNum.setText("(" + size + ")");
            //设置背景
            holder.mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            holder.mView.setOnClickListener(this);
            holder.itemView.setTag(contentLists.get(position));
        }

        @Override
        public int getItemCount() {
            return contentLists==null?0:contentLists.size();
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG,"适配器点击调用"+onItemClickListener);
           if (onItemClickListener!=null){
               onItemClickListener.onItemClick(v,v.getTag());
           }
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.onItemClickListener=listener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener listener){
           this.onItemLongClickListener=listener;
        }

        public class ViewHolder extends  RecyclerView.ViewHolder {
            public final View mView;
            public final TextView tv_news_title;
            public final TextView tv_news_desc;
            public final TextView tv_news_time;
            public final TextView tv_news_source;
            public final TextView tv_news_imageNum;
            //third image
            public final ImageView iv_news_oneimage;
            public final ImageView iv_news_twoimage;
            public final ImageView iv_news_thirdimage;
            public final ImageView iv_news_bigimage;
            public final ImageView iv_news_leftimage;

            public final LinearLayout ll_image_third;
            public final LinearLayout ll_image_one;


            public ViewHolder(View itemView) {
                super(itemView);
                mView=itemView;
                tv_news_title= (TextView) itemView.findViewById(R.id.tv_news_title);
                tv_news_desc= (TextView) itemView.findViewById(R.id.tv_news_desc);
                tv_news_time= (TextView) itemView.findViewById(R.id.tv_news_time);
                tv_news_source= (TextView) itemView.findViewById(R.id.tv_news_source);
                tv_news_imageNum= (TextView) itemView.findViewById(R.id.tv_news_imageNum);

                iv_news_bigimage= (ImageView) itemView.findViewById(R.id.iv_big_one);
                iv_news_leftimage= (ImageView) itemView.findViewById(R.id.iv_left_image);
                iv_news_oneimage= (ImageView) itemView.findViewById(R.id.iv_third_one);
                iv_news_twoimage= (ImageView) itemView.findViewById(R.id.iv_third_two);
                iv_news_thirdimage= (ImageView) itemView.findViewById(R.id.iv_third_third);

                ll_image_third= (LinearLayout) itemView.findViewById(R.id.ll_image_third);
                ll_image_one= (LinearLayout) itemView.findViewById(R.id.ll_image_one);
            }
        }
    }





    public class DividerLine extends RecyclerView.ItemDecoration{
        /**
         * 水平方向
         */
        public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

        /**
         * 垂直方向
         */
        public static final int VERTICAL = LinearLayoutManager.VERTICAL;

        // 画笔
        private Paint paint;

        // 布局方向
        private int orientation;
        // 分割线颜色
        private int color;
        // 分割线尺寸
        private int size;

        public DividerLine() {
            this(VERTICAL);
        }
       /* public DividerLine(int space) {
            this.space = space;
        }*/
        public DividerLine(int orientation) {
            this.orientation = orientation;

            paint = new Paint();
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            if (orientation == VERTICAL) {
                drawHorizontal(c, parent);
            } else {
                drawVertical(c, parent);
            }
        }

        /**
         * 设置分割线颜色
         *
         * @param color 颜色
         */
        public void setColor(int color) {
            this.color = color;
            paint.setColor(color);
        }

        /**
         * 设置分割线尺寸
         *
         * @param size 尺寸
         */
        public void setSize(int size) {
            this.size = size;
        }

        /**
         * 设置分割线尺寸
         *
         * @param size 尺寸
         */
        public void setSpace(int height) {
            this.space = height;
        }

        // 绘制垂直分割线
        protected void drawVertical(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + size;

                c.drawRect(left, top, right, bottom, paint);
            }
        }

        // 绘制水平分割线
        protected void drawHorizontal(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + size;

                c.drawRect(left, top, right, bottom, paint);
            }
        }

        private int space;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

}
