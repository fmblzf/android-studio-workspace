package com.example.zhaofeng.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofeng on 2015/7/12.
 */
public class StaggeredAdapter extends RecyclerView.Adapter<MyHolderView> {

    private List<String> mdatas ;
    private LayoutInflater inflater ;

    private List<Integer> randomHeight ;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public StaggeredAdapter(List<String> mdatas, Context context) {
        this.mdatas = mdatas;
        this.inflater = LayoutInflater.from(context);

        randomHeight = new ArrayList<Integer>();
        for (int i = 0; i <mdatas.size() ; i++) {
            randomHeight.add((int)(100+300*Math.random()));
        }
    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.recycler_single_text,viewGroup,false);
        MyHolderView holderView = new MyHolderView(view);
        return holderView;
    }

    @Override
    public void onBindViewHolder(final MyHolderView myHolderView,final int i) {
        //将数据和view进行绑定
        myHolderView.textView.setText(mdatas.get(i));
        ViewGroup.LayoutParams lp = myHolderView.textView.getLayoutParams();
        lp.height = randomHeight.get(i);
        myHolderView.textView.setLayoutParams(lp);
        if (onItemClickListener != null){
            myHolderView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这里的点击位置不能使用 i 只能通过myHolderView.getLayoutPosition();
                    int position = myHolderView.getLayoutPosition();
                    onItemClickListener.onItemClick(myHolderView,position);

                }
            });
            myHolderView.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //这里的点击位置不能使用 i 只能通过myHolderView.getLayoutPosition();
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
