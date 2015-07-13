package com.example.zhaofeng.treeviewdemo.bean;

import com.example.zhaofeng.treeviewdemo.annotitions.TreeId;
import com.example.zhaofeng.treeviewdemo.annotitions.TreeName;
import com.example.zhaofeng.treeviewdemo.annotitions.TreePid;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public class OrgBean {

    @TreeId
    private int id ;

    @TreePid
    private int parentId ;

    @TreeName
    private String name;

}
