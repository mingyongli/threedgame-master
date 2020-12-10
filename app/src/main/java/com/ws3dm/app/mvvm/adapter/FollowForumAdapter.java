package com.ws3dm.app.mvvm.adapter;

import android.content.Context;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.MyFollowBean;
import com.ws3dm.app.mvvm.bean.MyIndexBean;
import com.ws3dm.app.util.glide.GlideUtil;

/**
 * 全部关注的版块
 */
public class FollowForumAdapter extends BaseRecyclerAdapter<MyFollowBean.DataBean.FollowPlateBean> {

    private ItemOnclickListener listener;

    public FollowForumAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, MyFollowBean.DataBean.FollowPlateBean item) {
        GlideUtil.loadImage(mContext, item.getPlateImage(), holder.getView(R.id.forum_img));
        holder.setText(R.id.forum_title, item.getPlateName());
        holder.setText(R.id.forum_heat, "今日帖量:" + String.valueOf(item.getTodayPostTopicCount()));
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnClickListener(item);
                }
            });
        }
    }

    public void setItemOnclickListener(FollowForumAdapter.ItemOnclickListener Listener) {
        listener = Listener;
    }

    public interface ItemOnclickListener {
        void OnClickListener(MyFollowBean.DataBean.FollowPlateBean item);
    }

}
