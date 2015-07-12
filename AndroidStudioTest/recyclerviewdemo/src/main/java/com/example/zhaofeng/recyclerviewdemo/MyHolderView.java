package com.example.zhaofeng.recyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhaofeng on 2015/7/12.
 */
public class MyHolderView extends RecyclerView.ViewHolder {
        TextView textView;
        public MyHolderView(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.id_tv);
        }

}
