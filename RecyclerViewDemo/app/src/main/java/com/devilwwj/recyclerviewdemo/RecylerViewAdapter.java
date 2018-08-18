package com.devilwwj.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * com.devilwwj.recyclerviewdemo
 * Created by devilwwj on 16/2/25.
 */
public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.MyViewHolder> {
    private Context mContext;
    protected List<String> mDatas;

    public interface OnItemClickLister {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLister onItemClickLister;

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    public RecylerViewAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(mDatas.get(position));

        setUpItemEvent(holder);
    }

    protected void setUpItemEvent(final MyViewHolder holder) {
        final int layoutPosition = holder.getLayoutPosition();
        if (onItemClickLister != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLister.onItemClick(holder.itemView, layoutPosition);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickLister.onItemLongClick(holder.itemView, layoutPosition);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_alpha);
        }


    }

    public void addData(int postion) {
        mDatas.add(postion, "insert one");
        notifyItemInserted(postion);
    }

    public void deleteData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }
}
