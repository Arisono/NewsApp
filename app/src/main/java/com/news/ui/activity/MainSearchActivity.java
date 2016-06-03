package com.news.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.news.adapter.SimpleAdapter;
import com.news.app.Constants;
import com.news.base.activity.BaseActivity;
import com.news.db.dao.NewsDao;
import com.news.model.db.NewEntity;
import com.news.model.db.PageRootBean;
import com.news.net.R;
import com.news.service.interfac.OnItemClickListener;
import com.news.util.base.ApiUtils;
import com.news.util.base.KeyBoardUtils;
import com.news.util.base.StringUtils;
import com.news.util.base.ToastUtils;
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
 * @desc:查询新闻 activity
 * @author：Administrator on 2016/5/3 15:45
 */
public class MainSearchActivity extends BaseActivity {

    String TAG="MainSearchActivity";
    @Bind(R.id.toolbar)
    public Toolbar toolbar;
    @Bind(R.id.mlist)
    public RecyclerView mlist;
    @Bind(R.id.progressBar)
    public ProgressBar progressBar;

    public int page=1;
    private String value;
    private SearchView mSearchView;
    private SimpleAdapter adapter;
    private List<NewEntity> contentlists=new ArrayList<>();
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    @Override
    public void initView() {
         setContentView(R.layout.activity_main_search);
         ButterKnife.bind(this);


         Intent intent=getIntent();
         if (intent!=null){
             value=intent.getStringExtra("value");
         }
         toolbar.setSubtitleTextColor(getResources().getColor(R.color.whitesmoke));
         toolbar.setTitleTextColor(getResources().getColor(R.color.whitesmoke));
         setSupportActionBar(toolbar);
         getSupportActionBar().setHomeButtonEnabled(true);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//         toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case android.R.id.home:
//                        onBackPressed();
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        mlist.setLayoutManager(new LinearLayoutManager(mlist.getContext()));
        adapter=new SimpleAdapter(this,contentlists);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object object) {
                mSearchView.clearFocus();
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

        mlist.addOnScrollListener(mOnScrollListener);


    }

    @Override
    public void initData() {
        resqustData(page);
    }

    SearchView.SearchAutoComplete mEdit;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "MainSearchActivity:onCreateOptionsMenu:140:" + "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setIconifiedByDefault(false);
        mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mEdit.setText(value);
        mEdit.setSelection(value.length());
        mSearchView.setQueryHint("输入您感兴趣的...");

       final LinearLayout search_edit_frame= (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
       search_edit_frame.setBackgroundResource(R.drawable.shape_from_edit);
       search_edit_frame.setClickable(true);

        mEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                search_edit_frame.setPressed(hasFocus);
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edit_frame.setPressed(true);
            }
        });

        mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/

                    mSearchView.clearFocus();
                    search_edit_frame.setPressed(false);
                    KeyBoardUtils.closeKeybord(mEdit, ct);
                    //清空
                    if (!StringUtils.isEmpty(v.getText().toString())) {
                        contentlists.clear();
                        value = v.getText().toString();
                        page = 1;
                        progressBar.setVisibility(View.VISIBLE);
                        resqustData(page);
                    }
                    return true;
                }
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("Arison", "MainSearchActivity:onOptionsItemSelected:203:"+"onOptionsItemSelected()");
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String initTag() {
        return TAG;
    }

    /**
     * @功能:请求网络数据,后面统一写成接口方式
     * @author:Arisono
     * @param:
     * @return:
     */
    public void resqustData(int count){

        String url= Constants.API_NEWS;
        String datetime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final Map<String,Object> param=new HashMap<>();
        param.put("showapi_appid", Constants.NEWS_SHOWAPI_APPID);
        param.put("showapi_sign", Constants.NEWS_SHOWAPI_SIGN);
        param.put("showapi_timestamp", datetime);
        param.put("page",count);
        param.put("title", value);
        param.put("needHtml",1);
        NetUtils.httpResquest(this,Constants.API_NEWS,param,Constants.HTTP_GET, new HttpDataCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void processData(Object paramObject, boolean paramBoolean) {
              Log.i("Arison", "MainSearchActivity:processData:124:" + JSON.toJSONString(paramObject));
                displayUi(paramObject);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailed() {

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
        if (rootEntity.getShowapi_res_body()!=null) {
            List<NewEntity> newEntities = rootEntity.getShowapi_res_body().getPagebean().getContentlist();
            //  NewsDao.getInstance().saveAll(newEntities);
            if (newEntities.size() > 0) {
                contentlists.addAll(newEntities);
            } else {
            RecyclerViewStateUtils.setFooterViewState(activity, mlist, 20,page, LoadingFooter.State.TheEnd, null);
                --page;
            }
            adapter.notifyDataSetChanged();
        }else{
            ToastUtils.showLong(ct,"没有您需要的新闻数据！");
        }
    }



    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mlist);
            if(state == LoadingFooter.State.Loading) {
                return;
            }
            resqustData(++page);
            RecyclerViewStateUtils.setFooterViewState(activity, mlist, 20, page, LoadingFooter.State.Loading, null);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (mEdit!=null){
           KeyBoardUtils.closeKeybord(mEdit, ct);
        }
    }
}
