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
        ����
         */
    private int id;
    /*
    ���ڵ������
     */
    private int pid = 0 ;
    private String name;
    private Node parent;
    private List<Node> childs = new ArrayList<Node>();
    private int icon ;
    /*
    ���Ĳ㼶
     */
    private int level;
    /*
    �Ƿ�չ��
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
    ��ȡ�ڵ�Ĳ㼶
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
    ���ýڵ��Ƿ�չ��
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
    �ж��Ƿ��Ǹ��ڵ�
     */
    public boolean isRoot(){
        return parent == null;
    }

    /*
    ���ڵ��Ƿ�չ��
     */
    public boolean isParentExpand(){
        if(parent == null) return false;
        return parent.isExpand();
    }
    /*
    �Ƿ���Ҷ�ӽڵ�
     */
    public boolean isLeaf(){
        return childs.size() == 0;
    }
}
