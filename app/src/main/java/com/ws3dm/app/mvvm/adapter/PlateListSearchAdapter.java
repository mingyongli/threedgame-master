package com.ws3dm.app.mvvm.adapter;

import android.content.Context;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.PlateListSearchBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class PlateListSearchAdapter extends BaseRecyclerAdapter<PlateListSearchBean.DataBean.ListBean> {

    private ItemClickListener mListener;

    public PlateListSearchAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PlateListSearchBean.DataBean.ListBean item) {
        GlideUtil.loadImage(mContext, item.getPlateImage(), holder.getView(R.id.forum_img));
        holder.setText(R.id.forum_title, item.getPlateName());
        holder.setText(R.id.forum_heat, "今日贴量:" + item.getTodayPostTopicCount());
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.ItemClick(item);
                }
            });
        }
    }

    public void setItemOnClickListener(ItemClickListener Listener) {
        mListener = Listener;
    }

    public interface ItemClickListener {
        void ItemClick(PlateListSearchBean.DataBean.ListBean item);
    }
}
