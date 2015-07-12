package com.example.zhaofeng.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhaofeng on 2015/7/12.
 */
public class SimpleAdapter extends RecyclerView.Adapter<MyHolderView> {

    private List<String> mdatas ;
    private LayoutInflater inflater ;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public SimpleAdapter(List<String> mdatas, Context context) {
        this.mdatas = mdatas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.recycler_single_text,viewGroup,false);
        MyHolderView holderView = new MyHolderView(view);
        return holderView;
    }

    @Override
    public void onBindViewHolder(final MyHolderView myHolderView, final int i) {
        //�����ݺ�view���а�
        myHolderView.textView.setText(mdatas.get(i));
        if (onItemClickListener != null){
            myHolderView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //����ĵ��λ�ò���ʹ�� i ֻ��ͨ��myHolderView.getLayoutPosition();
                    int position = myHolderView.getLayoutPosition();
                    onItemClickListener.onItemClick(myHolderView,position);
                }
            });
            myHolderView.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //����ĵ��λ�ò���ʹ�� i ֻ��ͨ��myHolderView.getLayoutPosition();
                    int position = myHolderView.getLayoutPosition();
                    onItemClickListener.onItemLongClick(myHolderView,position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    public void addData(int pos){
        mdatas.add(pos,"Insert one");
        notifyItemInserted(pos);
    }
    public void deleteData(int pos){
        mdatas.remove(pos);
        notifyItemRemoved(pos);
    }
}
