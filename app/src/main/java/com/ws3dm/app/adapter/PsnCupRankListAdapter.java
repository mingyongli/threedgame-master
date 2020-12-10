package com.ws3dm.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.PsnCupRankBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class PsnCupRankListAdapter extends BaseRecyclerAdapter<PsnCupRankBean.DataBean.ListBean> {

    public PsnCupRankListAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PsnCupRankBean.DataBean.ListBean item) {
        GlideUtil.loadImage(mContext, item.getAvatarstr(), holder.itemView.findViewById(R.id.psn_user_img));
        TextView name = holder.itemView.findViewById(R.id.psn_user_name);
        TextView rank = holder.itemView.findViewById(R.id.psn_rank);
        TextView masonry = holder.itemView.findViewById(R.id.masonry);
        TextView silver = holder.itemView.findViewById(R.id.silver);
        TextView gold = holder.itemView.findViewById(R.id.gold);
        TextView copper = holder.itemView.findViewById(R.id.copper);
        TextView hour = holder.itemView.findViewById(R.id.game_hour);
        if (holder.getView(R.id.game_hour) != null) {
            hour.setText(item.getGamehour() + "h");
        }
        rank.setText(item.getRank() + "");
        name.setText(item.getNickname());
        masonry.setText("白金" + item.getPlatinum() + "");
        silver.setText("银" + item.getSilver() + "");
        gold.setText("金" + item.getGold() + "");
        copper.setText("铜" + item.getBronze() + "");
    }
}
