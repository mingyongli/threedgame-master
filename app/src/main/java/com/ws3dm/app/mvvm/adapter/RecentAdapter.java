package com.ws3dm.app.mvvm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.MyIndexBean;
import com.ws3dm.app.util.glide.GlideUtil;

/**
 * 最近逛的版块适配器
 */
public class RecentAdapter extends BaseRecyclerAdapter<MyIndexBean.DataBean.LatelyPlateBean> {

    private ItemOnclickListener mListener;

    public RecentAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, MyIndexBean.DataBean.LatelyPlateBean item) {
        GlideUtil.loadImage(mContext, item.getPlateImage(), holder.getView(R.id.forum_img));
        holder.setText(R.id.forum_title, item.getPlateName());
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnClickListener(item);
                }
            });
        }
    }

    public void setItemOnclickListener(ItemOnclickListener Listener) {
        mListener = Listener;
    }

    public interface ItemOnclickListener {
        void OnClickListener(MyIndexBean.DataBean.LatelyPlateBean item);
    }


}
