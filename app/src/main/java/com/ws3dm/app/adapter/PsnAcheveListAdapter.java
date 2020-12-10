package com.ws3dm.app.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.bean.PsnCupListBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class PsnAcheveListAdapter extends BaseRecyclerAdapter<PsnCupListBean.DataBean.ListBean> {

    public PsnAcheveListAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PsnCupListBean.DataBean.ListBean item) {
        TextView title = holder.itemView.findViewById(R.id.psn_achieve_title);
        TextView desc = holder.itemView.findViewById(R.id.psn_achieve_desc);
        TextView percent = holder.itemView.findViewById(R.id.psn_achieve_percent);
        GlideUtil.loadImage(mContext, item.getLitpic(), holder.itemView.findViewById(R.id.psn_achieve_img));
        title.setText(item.getTitle());
        desc.setText(item.getDescription());
        percent.setText(item.getPercent());
        if (item.getType() == 1) {
            GlideUtil.loadImage(mContext,R.drawable.jb_qiu,holder.getView(R.id.cupType));
        } else if (item.getType() == 2) {

            GlideUtil.loadImage(mContext,R.drawable.jb_jin,holder.getView(R.id.cupType));
        } else if (item.getType() == 3) {

            GlideUtil.loadImage(mContext,R.drawable.jb_na,holder.getView(R.id.cupType));
        } else if (item.getType() == 4) {

            GlideUtil.loadImage(mContext,R.drawable.jb_mj,holder.getView(R.id.cupType));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
