package com.devilwwj.recyclerviewdemo;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * com.devilwwj.recyclerviewdemo
 * Created by devilwwj on 16/2/25.
 */
public class StaggeredGridAdapter extends RecylerViewAdapter {
    private List<Integer> mHeights;


    public StaggeredGridAdapter(Context context, List<String> datas) {
        super(context, datas);

        mHeights = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int)(100 + Math.random() * 300));
        }
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height = mHeights.get(position);
        holder.itemView.setLayoutParams(lp);

        holder.tv.setText(mDatas.get(position));
        setUpItemEvent(holder);
    }

}
