package com.ws3dm.app.adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.SteamCountRankBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class SteamCountRankAdapter extends BaseRecyclerAdapter<SteamCountRankBean.DataBean.ListBean> {


    public SteamCountRankAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bindData(RecyclerViewHolder holder, int position, SteamCountRankBean.DataBean.ListBean item) {
        if (item.getIs_me() == 1 && position == 0) {
            holder.getView(R.id.item_bg).setBackgroundColor(mContext.getColor(R.color.mygameRank));
        }else {
            holder.getView(R.id.item_bg).setBackgroundColor(0);
        }
        TextView rank = holder.itemView.findViewById(R.id.steam_ranking);
        TextView steamName = holder.itemView.findViewById(R.id.steam_name);
        TextView price = holder.itemView.findViewById(R.id.steam_price);
        TextView dmName = holder.itemView.findViewById(R.id.dm_name);
        GlideUtil.loadCircleImage(mContext, item.getSt_avatarstr(), holder.itemView.findViewById(R.id.steam_head_img));
        GlideUtil.loadCircleImage(mContext, item.getAvatarstr(), holder.itemView.findViewById(R.id.dm_head_img));
        rank.setText(item.getRank() + "");
        steamName.setText(item.getSt_nickname());
        price.setText(item.getGamecount() + "");
        dmName.setText(item.getNickname());
    }
}
