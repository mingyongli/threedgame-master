package com.ws3dm.app.mvvm.adapter;

import android.content.Context;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.ForumListBean;
import com.ws3dm.app.mvvm.bean.MyFollowBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class ForumTabContentAdapter extends BaseRecyclerAdapter<ForumListBean.DataBean.ListBean.PlateTypeListBean> {

    private ItemOnclickListener listener;

    public ForumTabContentAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, ForumListBean.DataBean.ListBean.PlateTypeListBean item) {
        GlideUtil.loadImage(mContext, item.getPlateImage(), holder.getView(R.id.forum_img));
        holder.setText(R.id.forum_title, item.getPlateName());
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnClickListener(item);
                }
            });
        }
    }

    public void setItemOnclickListener(ForumTabContentAdapter.ItemOnclickListener Listener) {

        listener = Listener;
    }

    public interface ItemOnclickListener {
        void OnClickListener(ForumListBean.DataBean.ListBean.PlateTypeListBean item);
    }
}
