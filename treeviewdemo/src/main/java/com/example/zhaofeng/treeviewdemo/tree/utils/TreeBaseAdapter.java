package com.example.zhaofeng.treeviewdemo.tree.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhaofeng.treeviewdemo.R;
import com.example.zhaofeng.treeviewdemo.tree.bean.Node;
import com.example.zhaofeng.treeviewdemo.tree.iterface.OnTreeClickListener;

import java.util.List;

/**
 * Created by zhaofeng on 2015/7/13.
 */
public abstract class TreeBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected ListView mListView;
    protected List<T> mDatas;

    protected LayoutInflater mLayoutInflater;

    //树形结构的所有数据集合
    protected List<Node> mAllTreeDatas;
    //树形结构中当前显示的节点的数据集合
    protected List<Node> mVisiableDatas;
    //默认展示的层级数
    protected int mDefaultExpandLevel = 0;

    //设置接口 函数
    public void setOnTreeClickListener(OnTreeClickListener listener) {
        this.listener = listener;
    }

    //回调接口
    private OnTreeClickListener listener;

    public TreeBaseAdapter() {
        super();
    }

    public TreeBaseAdapter(Context mContext, ListView mListView, List<T> mDatas, int mDefaultExpandLevel) throws IllegalAccessException {
        this.mContext = mContext;
        this.mListView = mListView;
        this.mDatas = mDatas;
        this.mDefaultExpandLevel = mDefaultExpandLevel;

        mLayoutInflater = LayoutInflater.from(mContext);

        mAllTreeDatas = TreeHelper.getSortedNodes(mDatas, mDefaultExpandLevel);
        mVisiableDatas = TreeHelper.filterVisibleNodes(mAllTreeDatas);

        initEvent();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //初始化单击事件
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expanedOrCollpase(mVisiableDatas.get(position), position);
                if (listener != null) {
                    listener.onTreeItemClick(mVisiableDatas.get(position), position);
                }
            }
        });
        //初始化长按事件
        this.mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onTreeItemLongClick(mVisiableDatas.get(position), position);
                }
                return true;//true截断事件的向下传递,传递给onItemClick
            }
        });
    }

    /**
     * 设置结构收起还是展开
     *
     * @param node
     * @param position
     */
    private void expanedOrCollpase(Node node, int position) {
        //假如是叶子节点，就不会有展开或者收起
        if (node.isLeaf()) return;
        node.setIsExpand(!node.isExpand());
        mVisiableDatas = TreeHelper.filterVisibleNodes(mAllTreeDatas);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mVisiableDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisiableDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refreshData(List<T> newMdata){
        try {
            mAllTreeDatas = TreeHelper.getSortedNodes(newMdata, mDefaultExpandLevel);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mVisiableDatas = TreeHelper.filterVisibleNodes(mAllTreeDatas);
        notifyDataSetChanged();
    }


    public static class HolderView<E extends View>{
        private static SparseArray<View> sparseArray = null;

        public static <E extends View> E getView(View view,int id){
            sparseArray = (SparseArray<View>) view.getTag(R.string.hello_world);
            if(sparseArray == null) {
                sparseArray = new SparseArray<View>();
                view.setTag(R.string.hello_world,sparseArray);
            }
            View viewchild = sparseArray.get(id);
            if (viewchild == null){
                viewchild = view.findViewById(id);
                sparseArray.put(id,viewchild);
            }
            return (E)viewchild;
        }

    }

}
