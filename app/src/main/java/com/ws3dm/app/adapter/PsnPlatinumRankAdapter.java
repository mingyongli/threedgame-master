package com.ws3dm.app.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.PsnPlatinumRankBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class PsnPlatinumRankAdapter extends BaseRecyclerAdapter<PsnPlatinumRankBean.DataBean.ListBean> {
    public PsnPlatinumRankAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PsnPlatinumRankBean.DataBean.ListBean item) {
        TextView rank = holder.itemView.findViewById(R.id.psn_ranking);
        TextView name = holder.itemView.findViewById(R.id.psn_name);
        TextView price = holder.itemView.findViewById(R.id.psn_price);
        TextView dmName = holder.itemView.findViewById(R.id.dm_name);
        GlideUtil.loadImage(mContext, item.getPsn_avatarstr(), holder.itemView.findViewById(R.id.psn_head_img));
        GlideUtil.loadImage(mContext, item.getAvatarstr(), holder.itemView.findViewById(R.id.dm_head_img));
        rank.setText(item.getRank() + "");
        name.setText(item.getPsn_nickname());
        price.setText(item.getPlatinum() + "");
        dmName.setText(item.getNickname());
    }

}