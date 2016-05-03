package com.news.ui.activity;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.news.base.activity.BaseActivity;
import com.news.net.R;

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
    private SearchView mSearchView;

    @Override
    public void initView() {
         setContentView(R.layout.activity_main_search);
         ButterKnife.bind(this);

         toolbar.setSubtitleTextColor(getResources().getColor(R.color.whitesmoke));
         toolbar.setTitleTextColor(getResources().getColor(R.color.whitesmoke));
         setSupportActionBar(toolbar);
         getSupportActionBar().setHomeButtonEnabled(true);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        onBackPressed();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}
