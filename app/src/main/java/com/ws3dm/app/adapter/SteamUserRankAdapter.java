package com.ws3dm.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.internal.$Gson$Preconditions;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.SteamUserAchieveBean;
import com.ws3dm.app.util.glide.GlideUtil;

import org.w3c.dom.Text;

public class SteamUserRankAdapter extends BaseRecyclerAdapter<SteamUserAchieveBean.DataBean.ListBean> {
    public SteamUserRankAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, SteamUserAchieveBean.DataBean.ListBean item) {
        GlideUtil.loadImage(mContext, item.getAvatarstr(), holder.itemView.findViewById(R.id.user_img));
        TextView user_rank = holder.itemView.findViewById(R.id.user_rank);
        TextView user_Rank_percent = holder.itemView.findViewById(R.id.user_Rank_percent);
        TextView user_name = holder.itemView.findViewById(R.id.user_name);
        ProgressBar progressBar = holder.itemView.findViewById(R.id.progress);
        TextView progressBar_percent = holder.itemView.findViewById(R.id.progress_percent);
        progressBar.setProgress(item.getAchieve_percent());
        user_name.setText(item.getNickname());
        user_rank.setText(item.getRank() + "");
        progressBar_percent.setText(item.getAchieve_show());
        user_Rank_percent.setText(item.getAchieve_percent() + "%");
    }
}
