package com.example.zhaofeng.recyclerviewdemo;

import android.view.View;

/**
 * Created by zhaofeng on 2015/7/12.
 */
public interface OnItemClickListener {
    public void onItemClick(MyHolderView view,int position);

    public void onItemLongClick(MyHolderView view,int position);
}
