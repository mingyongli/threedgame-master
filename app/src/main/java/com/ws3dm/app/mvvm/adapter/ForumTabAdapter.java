package com.ws3dm.app.mvvm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.ForumListBean;

/**
 * 论坛版块的tab
 */
public class ForumTabAdapter extends BaseRecyclerAdapter<ForumListBean.DataBean.ListBean> {

    private ItemOnclick mListener;
    private int mCurrentSelectedPosition = 0;

    public ForumTabAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void bindData(RecyclerViewHolder holder, int position, ForumListBean.DataBean.ListBean item) {
        holder.setText(R.id.tab_name, item.getPlateTypeName());
        if (position == mCurrentSelectedPosition) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F9F9FE"));
        }
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    mListener.Onclick(item);
                    mCurrentSelectedPosition = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void setItemOnclick(ItemOnclick Listener) {
        mListener = Listener;
    }

    public interface ItemOnclick {
        void Onclick(ForumListBean.DataBean.ListBean item);
    }
}
