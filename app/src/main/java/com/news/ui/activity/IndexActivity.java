package com.news.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.news.adapter.DrawerAdapter;
import com.news.adapter.NewsFragmentAdapter;
import com.news.app.Constants;
import com.news.db.dao.NewsDao;
import com.news.model.EntryItem;
import com.news.model.db.ChannelEntity;
import com.news.model.db.ListRootBean;
import com.news.model.interfac.Item;
import com.news.net.R;
import com.news.ui.fragment.NewsFragment;
import com.news.util.base.ApiUtils;
import com.news.util.base.ListUtils;
import com.news.util.base.LogUtils;
import com.news.util.base.ToastUtils;
import com.news.util.cache.SDCardUtils;
import com.news.util.net.HttpClientUtil;
import com.news.util.net.HttpDataCallBack;
import com.news.util.net.NetUtils;
import com.news.widget.navigation.ScrimInsetsRelativeLayout;
import com.news.widget.views.RoundedImageView;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndexActivity extends AppCompatActivity {
    private String TAG = "IndexActivity";


    @Bind(R.id.tabs)
    public TabLayout tabLayout;
    @Bind(R.id.viewpager)
    public ViewPager viewPager;
    @Bind(R.id.toolbar)
    public Toolbar toolbar;
    @Bind(R.id.fab)
    public FloatingActionButton fab;
    private SearchView mSearchView;

    @Bind(R.id.settingsbutton)
    public LinearLayout mSettingsbutton;

    public RoundedImageView profile_pic;


    //left Drawer
    @Bind(R.id.drawerLayout)
    public DrawerLayout mDrawerLayout;
    @Bind(R.id.left_drawer)
    public ScrimInsetsRelativeLayout mDrawerLinear;
    @Bind(R.id.menu_drawer)
    public ListView mDrawerList;

    public View drawerHeaderLayout;

    // right Drawer
    @Bind(R.id.nav_right)
    public NavigationView mRightDrawer;

    private DrawerAdapter adapter;//adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    /**
     * @desc:initView
     * @author：Administrator on 2016/4/12 11:38
     */
    public void initView() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_left_menu));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("新闻");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //init headerview
        drawerHeaderLayout = getLayoutInflater().inflate(R.layout.drawerheader, null);
        profile_pic = (RoundedImageView) drawerHeaderLayout.findViewById(R.id.profile_pic);
        //add 
        mDrawerList.addHeaderView(drawerHeaderLayout);//加头布局文件

        updateLeftDrawer();


    }

    public void initListener() {
        //右边侧滑监听
        mRightDrawer.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {

                    private MenuItem mPreMenuItem;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (mPreMenuItem != null) mPreMenuItem.setChecked(false);
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        mPreMenuItem = menuItem;
                        return true;
                    }
                });
    }


    public void updateLeftDrawer() {
        ArrayList<Item> list = new ArrayList<>();
        list.add(new EntryItem(getResources().getString(R.string.left_menu_news), "0", ContextCompat.getDrawable(this, R.drawable.ic_left_menu_music)));
        list.add(new EntryItem(getResources().getString(R.string.left_menu_music), "1", ContextCompat.getDrawable(this, R.drawable.ic_left_menu_video)));
        list.add(new EntryItem(getResources().getString(R.string.left_menu_video), "2", ContextCompat.getDrawable(this, R.drawable.ic_left_menu_music)));
        list.add(new EntryItem(getResources().getString(R.string.left_menu_image), "3", ContextCompat.getDrawable(this, R.drawable.ic_left_menu_video)));
        adapter = new DrawerAdapter(this, list, IndexActivity.this);


        //创建一个功能数组  
        // String[] str=new String[]{"功能1","功能2","功能3","功能4","功能5","功能6","功能7","功能8","功能9","功能10","功能11"};
        //给listView设置一个ArrayAdapter  
        /**
         * 第二个参数可以用Android系统提供的TextView,也可以自定一个TextView 
         */
        //  ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str);

        mDrawerList.setAdapter(adapter);
    }

    /**
     * @desc:
     * @author：Administrator on 2015/12/21 16:40
     */
    public void initData() {
        List<ChannelEntity> channelEntities = NewsDao.getInstance().findAllChannel();
        if (!ListUtils.isEmpty(channelEntities)) {
            LogUtils.i("频道取缓存...");
            setupViewPager(viewPager, channelEntities);
        } else {
            LogUtils.i("频道取网络...");
            Logger.init("IndexActivity").setLogLevel(LogLevel.FULL);
            String url = Constants.API_NEWS;
            String datetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            final Map<String, Object> param = new HashMap<>();
            param.put("showapi_appid", Constants.NEWS_SHOWAPI_APPID);
            param.put("showapi_sign", Constants.NEWS_SHOWAPI_SIGN);
            param.put("showapi_timestamp", datetime);
            url = Constants.API_NEWS_CHANNEL;
            httpResquest(Constants.API_NEWS_CHANNEL, param);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        /*final MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (StringUtils.isEmpty(newText)) {
                    tabLayout.setVisibility(View.VISIBLE);
                } else {
                    tabLayout.setVisibility(View.INVISIBLE);
                    ToastUtils.show(IndexActivity.this, newText, 2000);
                }
                return true;
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                loadToolBarSearch();
                break;
            case R.id.action_export_data:
                String msg = SDCardUtils.exprotDataBase(this);
                ToastUtils.showLong(getApplicationContext(), msg);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager, List<ChannelEntity> channelList) {
        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getSupportFragmentManager());
        if (!channelList.isEmpty()) {
            for (int i = 0; i < channelList.size(); i++) {
                NewsFragment newsFragment = new NewsFragment();
                Bundle ags = new Bundle(2);
                ags.putString("name", channelList.get(i).getName());
                ags.putString("channelId", channelList.get(i).getChannelId());
                newsFragment.setArguments(ags);
                adapter.addFragment(newsFragment, channelList.get(i).getName());
            }
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(channelList.size());//预加载
    }

    private void httpResquest(String url, final Map<String, Object> param) {
        NetUtils.httpResquest(this, url, param, Constants.HTTP_GET, new HttpDataCallBack() {
            @Override
            public void onStart() {
                //Log.i(TAG, "http start...");
            }

            @Override
            public void processData(Object paramObject, boolean paramBoolean) {
                Log.i(TAG, "json:" + paramObject.toString());
                //NewsChannelEntity newsChannelEntity=JSON.parseObject(paramObject.toString(),NewsChannelEntity.class);
                ListRootBean<ChannelEntity> rootBean = ApiUtils.parseChannelList(paramObject.toString(), ChannelEntity.class);
                if (!ListUtils.isEmpty(rootBean.getShowapi_res_body().getChannelList())) {
                    NewsDao.getInstance().saveChannel(rootBean.getShowapi_res_body().getChannelList());
                    setupViewPager(viewPager, rootBean.getShowapi_res_body().getChannelList());
                }

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
            HttpClientUtil.Response response = HttpClientUtil.sendGetHeaderRequest(url, param, null);
            Log.i(TAG, "httpclient:" + response.getStatusCode());
            Log.i(TAG, "httpclient:" + response.getResponseText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Dialog toolbarSearchDialog;

    /**
     * @desc:加载搜索框
     * @author：Administrator on 2016/4/15 15:50
     */
    public void loadToolBarSearch() {
        View view = getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        ImageView imgToolMic = (ImageView) view.findViewById(R.id.img_tool_mic);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);

        edtToolSearch.setHint("输入您感兴趣的内容...");

        if (toolbarSearchDialog == null) {
            toolbarSearchDialog = new Dialog(IndexActivity.this, R.style.MaterialSearch);
            toolbarSearchDialog.setContentView(view);
            // toolbarSearchDialog.setCancelable(false);
            toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
            toolbarSearchDialog.show();

            // toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


            imgToolBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toolbarSearchDialog.dismiss();
                }
            });

            imgToolMic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edtToolSearch.setText("");

                }
            });

            edtToolSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    switch (actionId) {
                        case EditorInfo.IME_ACTION_GO:

                            break;
                        case EditorInfo.IME_ACTION_DONE:

                            //KeyBoardUtils.closeKeybord(edtToolSearch,IndexActivity.this);
                            Intent intent = new Intent(IndexActivity.this, MainSearchActivity.class);
                            intent.putExtra("value", v.getText().toString());
                            startActivityForResult(intent, RESULT_OK);
                            toolbarSearchDialog.dismiss();
                            return true;
                        case EditorInfo.IME_ACTION_NEXT:

                            break;
                    }
                    return false;
                }
            });
        } else {
            toolbarSearchDialog.show();
        }

    }


    @OnClick(R.id.settingsbutton)
    void submit() {
        ToastUtils.show(IndexActivity.this, "设置", 3000);
    }


}
