package com.example.zhaofeng.treeviewdemo.tree.utils;

import com.example.zhaofeng.treeviewdemo.annotitions.TreeId;
import com.example.zhaofeng.treeviewdemo.annotitions.TreeName;
import com.example.zhaofeng.treeviewdemo.annotitions.TreePid;
import com.example.zhaofeng.treeviewdemo.tree.bean.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public class TreeHelper {

    public static <T> List<Node> convertData2Nodes(List<T> list) throws IllegalAccessException {

        List<Node> nodes = new ArrayList<Node>();
        Node node = null;

        int id = -1;
        int pid = 0;
        String name = null;
        //ͨ�������ע�⽫�����ʵ�����ת�������νṹ��Node�ڵ���
        //ע�⣺��λ�����䣺��ȡ��ֵ
        for (T t : list) {
            Field[] fields = t.getClass().getFields();
            for (Field f : fields) {
                if (f.getAnnotation(TreeId.class)!=null){
                    id = f.getInt(t);
                }else if (f.getAnnotation(TreePid.class)!=null){
                    pid = f.getInt(t);
                }else if(f.getAnnotation(TreeName.class)!=null){
                    name = (String)f.get(t);
                }
            }
            node = new Node(id,pid,name);
            nodes.add(node);
        }

        return null;
    }
}
