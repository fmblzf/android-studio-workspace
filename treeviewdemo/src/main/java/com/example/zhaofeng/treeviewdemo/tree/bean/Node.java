package com.example.zhaofeng.treeviewdemo.tree.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public class Node {


    public Node(){

    }

    public Node(int id, int pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    /*
        主键
         */
    private int id;
    /*
    父节点的主键
     */
    private int pid = 0 ;
    private String name;
    private Node parent;
    private List<Node> childs = new ArrayList<Node>();
    private int icon ;
    /*
    树的层级
     */
    private int level;
    /*
    是否展开
     */
    private boolean isExpand = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public void setChilds(List<Node> childs) {
        this.childs = childs;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    /*
    获取节点的层级
     */
    public int getLevel() {
        return parent == null?0:parent.getLevel()+1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    /*
    设置节点是否展开
     */
    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if(!isExpand){
            for (Node node : childs){
                node.setIsExpand(false);
            }
        }
    }

    /*
    判断是否是跟节点
     */
    public boolean isRoot(){
        return parent == null;
    }

    /*
    父节点是否展开
     */
    public boolean isParentExpand(){
        if(parent == null) return false;
        return parent.isExpand();
    }
    /*
    是否是叶子节点
     */
    public boolean isLeaf(){
        return childs.size() == 0;
    }
}
