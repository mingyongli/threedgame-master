package com.ws3dm.app.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.SteamUserAchieveBean;
import com.ws3dm.app.bean.SteamUserHourBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class SteamUserHourAdapter extends BaseRecyclerAdapter<SteamUserHourBean.DataBean.ListBean> {
    public SteamUserHourAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, SteamUserHourBean.DataBean.ListBean item) {
        GlideUtil.loadImage(mContext, item.getAvatarstr(), holder.itemView.findViewById(R.id.user_img));
        TextView user_rank = holder.itemView.findViewById(R.id.user_rank);
        TextView user_hour = holder.itemView.findViewById(R.id.user_hour);
        TextView user_name = holder.itemView.findViewById(R.id.user_name);
        user_name.setText(item.getNickname());
        user_rank.setText(item.getRank() + "");
        user_hour.setText(item.getGamehour() + "h");
    }

}
