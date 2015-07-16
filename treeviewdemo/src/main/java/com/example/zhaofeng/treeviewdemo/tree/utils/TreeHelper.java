package com.example.zhaofeng.treeviewdemo.tree.utils;

import com.example.zhaofeng.treeviewdemo.R;
import com.example.zhaofeng.treeviewdemo.tree.annotitions.TreeId;
import com.example.zhaofeng.treeviewdemo.tree.annotitions.TreeName;
import com.example.zhaofeng.treeviewdemo.tree.annotitions.TreePid;
import com.example.zhaofeng.treeviewdemo.tree.bean.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public class TreeHelper {

    /**
     * 将用户的数据集合转化成树形数据集合
     * @param list
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <T> List<Node> convertData2Nodes(List<T> list) throws IllegalAccessException {

        List<Node> nodes = new ArrayList<Node>();
        Node node = null;

        int id = -1;
        int pid = 0;
        String name = null;
        //通过反射和注解将具体的实体对象，转换成树形结构的Node节点类
        //注解：定位；反射：获取数值
        for (T t : list) {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f.getAnnotation(TreeId.class)!=null){
                    f.setAccessible(true);
                    id = f.getInt(t);
                }else if (f.getAnnotation(TreePid.class)!=null){
                    f.setAccessible(true);
                    pid = f.getInt(t);
                }else if(f.getAnnotation(TreeName.class)!=null){
                    f.setAccessible(true);
                    name = (String)f.get(t);
                }
            }
            node = new Node(id,pid,name);
            nodes.add(node);
        }

        /*
        设置节点之间的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i+1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if(n.getPid() == m.getId()){
                    n.setParent(m);
                    m.getChilds().add(n);
                }else if(n.getId() == m.getPid()){
                    m.setParent(n);
                    n.getChilds().add(m);
                }
            }
        }

        /*
        设置节点图标
         */
        for (Node nodeIcon : nodes){
            setItemExpandIcon(nodeIcon);
        }

        return nodes;
    }

    /**
     * 设置节点图标
     * @param nodeIcon
     */
    private static void setItemExpandIcon(Node nodeIcon) {
        if (nodeIcon.getChilds().size() > 0){
            //非叶子节点设置图标
            if(nodeIcon.isExpand()){
                nodeIcon.setIcon(R.mipmap.tree_ex);
            }else{
                nodeIcon.setIcon(R.mipmap.tree_ec);
            }
        }else{
            nodeIcon.setIcon(-1);
        }
    }

    /**
     * 对集合进行排序
     * @param datas
     * @param defaultExpandLevel
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas,int defaultExpandLevel) throws IllegalAccessException {

        List<Node> result = new ArrayList<Node>();
        List<Node> nodes = convertData2Nodes(datas);
        //获取所有的根节点
        List<Node> rootNodes = getRootNodes(nodes);

        for (Node node : rootNodes){
            //将节点的所有子节点都添加到该节点下面
            addNode(result,node,defaultExpandLevel,1);
        }

        return result;
    }

    /**
     *
     把一个节点的所有孩子节点都放入result
     * @param result 添加完节点的集合
     * @param node   当前节点
     * @param defaultExpandLevel 默认展开的层级-自定义设置应该展开多少层级
     * @param currentLevel 当前节点对应的层级 从1开始
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if(defaultExpandLevel >= currentLevel){
            //默认的展开的层级比当前层级大，说明当前的节点也在默认展开层级的范围之内，应该被展开
            node.setIsExpand(true);
        }
        if (node.isLeaf()){
            //当前节点是叶子节点，那么结束
            return;
        }
        for (int i = 0; i < node.getChilds().size(); i++) {
            addNode(result,node.getChilds().get(i),defaultExpandLevel,currentLevel+1);
        }
    }

    /**
     * 从所有的节点中过滤出所有的根节点
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<Node>();
        for (Node node :nodes){
            if (node.isRoot()){
                rootNodes.add(node);
            }
        }
        return rootNodes;
    }

    /**
     * 过滤出应该的显示的节点集合
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes){
        List<Node> list = new ArrayList<Node>();
        for(Node n:nodes){
            if(n.isRoot() || n.isParentExpand()){
                setItemExpandIcon(n);
                list.add(n);
            }
        }
        return list;
    }
    
}
