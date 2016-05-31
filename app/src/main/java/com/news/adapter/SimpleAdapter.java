package com.news.adapter;

/**
 * Created by Arisono on 2016/5/30.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.model.db.NewEntity;
import com.news.net.R;
import com.news.service.interfac.OnItemClickListener;
import com.news.service.interfac.OnItemLongClickListener;
import com.news.util.imageloader.ImageLoaderFactory;
import com.news.util.imageloader.ImageLoaderWrapper;

import java.util.List;

/**
 * @desc:RecyclerView adapter
 * @author：Administrator on 2016/1/5 15:30
 */
public class SimpleAdapter extends  RecyclerView.Adapter<SimpleAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener {

    List<NewEntity> contentLists;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private Context ct;
    private ImageLoaderWrapper mImageLoaderWrapper;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public SimpleAdapter(Context context,
                  List<NewEntity> items){
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mImageLoaderWrapper= ImageLoaderFactory.getLoader();
        this.contentLists=items;
        this.ct=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_news,parent,false);
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
        holder.mView.setBackgroundColor(ct.getResources().getColor(android.R.color.transparent));
        holder.mView.setOnClickListener(this);
        holder.itemView.setTag(contentLists.get(position));
    }

    @Override
    public int getItemCount() {
        return contentLists==null?0:contentLists.size();
    }

    @Override
    public void onClick(View v) {
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