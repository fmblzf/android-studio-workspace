package com.example.zhaofeng.treeviewdemo.tree.iterface;

import com.example.zhaofeng.treeviewdemo.tree.bean.Node;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public interface OnTreeClickListener {

    public void onTreeItemClick(Node node, int position);

    public void onTreeItemLongClick(Node node, int position);

}
