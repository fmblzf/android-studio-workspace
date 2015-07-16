package com.example.zhaofeng.treeviewdemo.bean;


import com.example.zhaofeng.treeviewdemo.tree.annotitions.TreeId;
import com.example.zhaofeng.treeviewdemo.tree.annotitions.TreeName;
import com.example.zhaofeng.treeviewdemo.tree.annotitions.TreePid;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public class FileBean {

    public FileBean(int _id, int pid, String _name) {
        this._id = _id;
        this.pid = pid;
        this._name = _name;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "_id=" + _id +
                ", pid=" + pid +
                ", _name='" + _name + '\'' +
                '}';
    }

    @TreeId
    private int _id ;

    @TreePid
    private int pid ;

    @TreeName
    private String _name ;

}
