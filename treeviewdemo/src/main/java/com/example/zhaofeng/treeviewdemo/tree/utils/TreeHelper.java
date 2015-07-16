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
     * ���û������ݼ���ת�����������ݼ���
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
        //ͨ�������ע�⽫�����ʵ�����ת�������νṹ��Node�ڵ���
        //ע�⣺��λ�����䣺��ȡ��ֵ
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
        ���ýڵ�֮��Ĺ�ϵ
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
        ���ýڵ�ͼ��
         */
        for (Node nodeIcon : nodes){
            setItemExpandIcon(nodeIcon);
        }

        return nodes;
    }

    /**
     * ���ýڵ�ͼ��
     * @param nodeIcon
     */
    private static void setItemExpandIcon(Node nodeIcon) {
        if (nodeIcon.getChilds().size() > 0){
            //��Ҷ�ӽڵ�����ͼ��
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
     * �Լ��Ͻ�������
     * @param datas
     * @param defaultExpandLevel
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas,int defaultExpandLevel) throws IllegalAccessException {

        List<Node> result = new ArrayList<Node>();
        List<Node> nodes = convertData2Nodes(datas);
        //��ȡ���еĸ��ڵ�
        List<Node> rootNodes = getRootNodes(nodes);

        for (Node node : rootNodes){
            //���ڵ�������ӽڵ㶼��ӵ��ýڵ�����
            addNode(result,node,defaultExpandLevel,1);
        }

        return result;
    }

    /**
     *
     ��һ���ڵ�����к��ӽڵ㶼����result
     * @param result �����ڵ�ļ���
     * @param node   ��ǰ�ڵ�
     * @param defaultExpandLevel Ĭ��չ���Ĳ㼶-�Զ�������Ӧ��չ�����ٲ㼶
     * @param currentLevel ��ǰ�ڵ��Ӧ�Ĳ㼶 ��1��ʼ
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if(defaultExpandLevel >= currentLevel){
            //Ĭ�ϵ�չ���Ĳ㼶�ȵ�ǰ�㼶��˵����ǰ�Ľڵ�Ҳ��Ĭ��չ���㼶�ķ�Χ֮�ڣ�Ӧ�ñ�չ��
            node.setIsExpand(true);
        }
        if (node.isLeaf()){
            //��ǰ�ڵ���Ҷ�ӽڵ㣬��ô����
            return;
        }
        for (int i = 0; i < node.getChilds().size(); i++) {
            addNode(result,node.getChilds().get(i),defaultExpandLevel,currentLevel+1);
        }
    }

    /**
     * �����еĽڵ��й��˳����еĸ��ڵ�
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
     * ���˳�Ӧ�õ���ʾ�Ľڵ㼯��
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
