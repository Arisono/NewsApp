package com.news.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.news.util.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/4.
 * 新闻列表
 */
public class NewsFragment extends Fragment{

    @Bind(R.id.mlist)
    public ListView mlist;
    private NewsListAdapter adapter;
    Toolbar mMainToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news,container,false);
        ButterKnife.bind(this,view);

        initData();
        mMainToolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
        return view;
    }

   /**
    * @desc:initdata
    * @author：Administrator on 2016/1/4 16:02
    */
   public void initData(){
       List<String> data=new ArrayList<>();
       for (int i=0;i<=22;i++){
          data.add("数据"+i);
       }
       adapter=new NewsListAdapter(getActivity(),data);
       mlist.setAdapter(adapter);

   }

    public class NewsListAdapter extends BaseAdapter{

        private LayoutInflater inflater;
        private List<String> items;

        public NewsListAdapter(Context ct,List<String> data ){
             this.inflater=LayoutInflater.from(ct);
             this.items=data;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             ViewHodler hodler=null;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.adapter_item_news,parent,false);
                hodler=new ViewHodler();
                hodler.news_title= (TextView) convertView.findViewById(R.id.tv_news_title);
                convertView.setTag(hodler);
            }else{
                hodler= (ViewHodler) convertView.getTag();
            }

            hodler.news_title.setText(items.get(position));

            return convertView;
        }


        class ViewHodler{
             TextView news_title;
        }
    }
}
