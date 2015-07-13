package com.example.zhaofeng.treeviewdemo.bean;

import com.example.zhaofeng.treeviewdemo.annotitions.TreeId;
import com.example.zhaofeng.treeviewdemo.annotitions.TreeName;
import com.example.zhaofeng.treeviewdemo.annotitions.TreePid;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public class FileBean {

    @TreeId
    private int _id ;

    @TreePid
    private int pid ;

    @TreeName
    private String _name ;

}
