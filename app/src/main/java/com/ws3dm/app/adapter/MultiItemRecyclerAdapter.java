package com.ws3dm.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

public abstract class MultiItemRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public MultiItemRecyclerAdapter(Context ctx, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(ctx, -1);
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mData.get(position));
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int                layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        RecyclerViewHolder holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

}
