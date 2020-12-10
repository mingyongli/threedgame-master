package com.ws3dm.app.mvvm.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.MyIndexBean;
import com.ws3dm.app.util.glide.GlideUtil;

import java.util.List;

/**
 * 关注的版块
 */
public class FocusAdapter extends BaseRecyclerAdapter<MyIndexBean.DataBean.FollowPlateBean> {
    public FocusAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    private ItemOnclickListener mListener = null;

    @Override
    public void bindData(RecyclerViewHolder holder, int position, MyIndexBean.DataBean.FollowPlateBean item) {
        GlideUtil.loadImage(mContext, item.getPlateImage(), holder.getView(R.id.forum_img));
        holder.setText(R.id.forum_title, item.getPlateName());
        holder.setText(R.id.forum_heat, "今日帖量:" + String.valueOf(item.getTodayPostTopicCount()));
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnClickListener(item);
                }
            });
        }
    }


    public void setItemOnclickListener(FocusAdapter.ItemOnclickListener Listener) {
        mListener = Listener;
    }

    public interface ItemOnclickListener {
        void OnClickListener(MyIndexBean.DataBean.FollowPlateBean item);
    }

}
