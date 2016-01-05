package com.news.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
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
    public RecyclerView mlist;
    //private NewsListAdapter adapter;
    private SimpleAdapter adapter;
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
       adapter=new SimpleAdapter(getActivity(),data);
       mlist.setLayoutManager(new LinearLayoutManager(mlist.getContext()));
       DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
       dividerLine.setSize(1);
       dividerLine.setColor(0x00000000);
       dividerLine.setSpace(20);
       mlist.addItemDecoration(dividerLine);
       mlist.setAdapter(adapter);

   }

    /**
     * @desc:RecyclerView adapter
     * @author：Administrator on 2016/1/5 15:30
     */
    public class SimpleAdapter extends  RecyclerView.Adapter<SimpleAdapter.ViewHolder>{

        private List<String> mValues;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;

        SimpleAdapter(Context context,List<String> items){
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.mValues=items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_news,parent,false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mValues.get(position));
            //设置背景
            holder.mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends  RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTextView;
            public ViewHolder(View itemView) {
                super(itemView);
                mView=itemView;
                mTextView= (TextView) itemView.findViewById(R.id.tv_news_title);
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


   /**
    * @desc:listview 适配器
    * @author：Administrator on 2016/1/5 15:26
    */
  /*  public class NewsListAdapter extends BaseAdapter{

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
    }*/
}
