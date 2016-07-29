package com.news.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.model.EntryItem;
import com.news.model.interfac.Item;
import com.news.net.R;

import java.util.ArrayList;

/**
 * 左边侧滑列表适配器
 * Created by Arison on 2016/7/28.
 */
public class DrawerAdapter extends ArrayAdapter<Item>{
    
    private final Context context;
    private final ArrayList<Item> values;
    private  LayoutInflater inflater;
    private Activity activity;
    private SparseBooleanArray myChecked = new SparseBooleanArray();//选中状态
    
    public DrawerAdapter(Context context, int resource,ArrayList<Item> values,
                         Activity activity) {
        super(context, R.layout.item_draw_errow,values);
        this.context=context;
        this.values=values;
        this.activity=activity;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (values.get(position).isSection()) {
            ImageView view = new ImageView(context);

            return view;
        }else{
            View  view = inflater.inflate(R.layout.item_draw_errow, parent, false);
            final TextView txtTitle=(TextView) view.findViewById(R.id.firstline);
            final ImageView imageView=(ImageView) view.findViewById(R.id.icon);
            
            view.setOnClickListener(new View.OnClickListener() {

                public void onClick(View p1) {

                }

            });
            
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    // return true to denote no further processing
                    return true;
                }
            });


            txtTitle.setText(((EntryItem) (values.get(position))).getTitle());
            imageView.setImageDrawable(getDrawable(position));
            imageView.clearColorFilter();
            if (myChecked.get(position)) {
                
            }else{
                imageView.setColorFilter(Color.parseColor("#666666"));
                txtTitle.setTextColor(activity.getResources().getColor(android.R.color.black));
                
            }
            return  view;
        }
      
        
       
    }

    Drawable getDrawable(int position){
        Drawable drawable=((EntryItem)getItem(position)).getIcon();
        return drawable;  }
}
