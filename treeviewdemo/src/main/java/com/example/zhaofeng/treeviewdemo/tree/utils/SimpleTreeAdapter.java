package com.example.zhaofeng.treeviewdemo.tree.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhaofeng.treeviewdemo.R;

import java.util.List;

/**
 * Created by zhaofeng on 2015/7/15.
 */
public class SimpleTreeAdapter<T> extends TreeBaseAdapter<T> {
    public SimpleTreeAdapter(Context mContext, ListView mListView, List<T> mDatas, int mDefaultExpandLevel) throws IllegalAccessException {
        super(mContext, mListView, mDatas, mDefaultExpandLevel);
    }

    private HolderViewClass holderViewClass;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.tree_item, parent, false);
            holderViewClass = new HolderViewClass();
            holderViewClass.imageView = HolderView.getView(convertView, R.id.id_icon);
            holderViewClass.textView = HolderView.getView(convertView, R.id.id_title);
            convertView.setTag(holderViewClass);
        } else {
            holderViewClass = (HolderViewClass) convertView.getTag();
        }
        if (mVisiableDatas.get(position).getIcon() > 0) {
            holderViewClass.imageView.setVisibility(View.VISIBLE);
            holderViewClass.imageView.setImageResource(mVisiableDatas.get(position).getIcon());
        } else {
            holderViewClass.imageView.setVisibility(View.INVISIBLE);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holderViewClass.imageView.getLayoutParams();
        layoutParams.setMargins(20 * (mVisiableDatas.get(position).getLevel() + 1), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        holderViewClass.imageView.setLayoutParams(layoutParams);
        holderViewClass.textView.setText(mVisiableDatas.get(position).getName());
        return convertView;
    }

    public class HolderViewClass {
        public ImageView imageView;
        public TextView textView;
    }
}
