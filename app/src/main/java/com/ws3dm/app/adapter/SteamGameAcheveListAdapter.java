package com.ws3dm.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.SteamGameAcheveListBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class SteamGameAcheveListAdapter extends BaseRecyclerAdapter<SteamGameAcheveListBean.DataBean.ListBean> {

    public SteamGameAcheveListAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, SteamGameAcheveListBean.DataBean.ListBean item) {
        TextView title = holder.itemView.findViewById(R.id.steam_achieve_title);
        TextView desc = holder.itemView.findViewById(R.id.steam_achieve_desc);
        TextView percent = holder.itemView.findViewById(R.id.steam_achieve_percent);
        GlideUtil.loadImage(mContext, item.getLitpic(), holder.itemView.findViewById(R.id.steam_achieve_img));
        title.setText(item.getTitle());
        desc.setText(item.getDescription());
        percent.setText(item.getPercent());
    }
}
