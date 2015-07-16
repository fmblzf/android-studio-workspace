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

    //���νṹ���������ݼ���
    protected List<Node> mAllTreeDatas;
    //���νṹ�е�ǰ��ʾ�Ľڵ�����ݼ���
    protected List<Node> mVisiableDatas;
    //Ĭ��չʾ�Ĳ㼶��
    protected int mDefaultExpandLevel = 0;

    //���ýӿ� ����
    public void setOnTreeClickListener(OnTreeClickListener listener) {
        this.listener = listener;
    }

    //�ص��ӿ�
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
     * ��ʼ���¼�
     */
    private void initEvent() {
        //��ʼ�������¼�
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expanedOrCollpase(mVisiableDatas.get(position), position);
                if (listener != null) {
                    listener.onTreeItemClick(mVisiableDatas.get(position), position);
                }
            }
        });
        //��ʼ�������¼�
        this.mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onTreeItemLongClick(mVisiableDatas.get(position), position);
                }
                return true;//true�ض��¼������´���,���ݸ�onItemClick
            }
        });
    }

    /**
     * ���ýṹ������չ��
     *
     * @param node
     * @param position
     */
    private void expanedOrCollpase(Node node, int position) {
        //������Ҷ�ӽڵ㣬�Ͳ�����չ����������
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
