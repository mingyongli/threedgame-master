package com.ws3dm.app.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的通用的RecyclerView适配器
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {


    protected List<T> mData;//数据集合
    protected Context mContext;
    private   OnItemClickListener     mClickListener;//单击事件
    private   OnItemLongClickListener mLongClickListener;//长按事件
    private   int                     mLayoutId;//布局id


    public BaseRecyclerAdapter(Context ctx, int layoutId) {
        mData = new ArrayList<T>();
        this.mContext = ctx;
        this.mLayoutId = layoutId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecyclerViewHolder.get(mContext, parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        if (holder != null) {
            if (mClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
            if (mLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mLongClickListener.onItemLongClick(holder.itemView, position);
                        return true;
                    }
                });
            }
            //测试item高度全屏的问题
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            
            bindData(holder, position, mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    abstract public void bindData(RecyclerViewHolder holder, int position, final T item);

    /**
     * 追加集合
     *
     * @param data
     */
    public void appendList(List<T> data) {
        mData.addAll(data);
         notifyDataSetChanged();
//        notifyItemRangeInserted(mData.size() - 1, data.size());
    }

    /**
     * 清除之后追加集合
     *
     * @param data
     */
    public void clearAndAddList(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 在某个位置添加一条记录
     *
     * @param position
     * @param item
     */
    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 在某个位置删除一条记录
     *
     * @param position
     */
    public void delete(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setOnLongClickListener(OnItemLongClickListener mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }

    public List<T> getmData() {
        return mData;
    }

    public T getDataByPosition(int position) {
        return mData.get(position);
    }

    public interface OnItemClickListener {

        public void onItemClick(View itemView, int position);
    }

    public interface OnItemLongClickListener {

        public void onItemLongClick(View itemView, int position);
    }


}
