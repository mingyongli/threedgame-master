package com.ws3dm.app.mvvm.adapter;

import android.content.Context;
import android.view.View;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.ForumPlateBean;
import com.ws3dm.app.util.glide.GlideUtil;

public class ForumPlateListAdapter extends BaseRecyclerAdapter<ForumPlateBean.DataBean.ListBean> {

    private PlateItemClickListener listener;

    public ForumPlateListAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, ForumPlateBean.DataBean.ListBean item) {
        holder.setText(R.id.author_name, item.getTopicSendUserName());
        GlideUtil.loadImage(mContext, item.getTopicSendUserIcon(), holder.getView(R.id.author_img));
        holder.setText(R.id.topicLabel, item.getTopicLabel());
        holder.setText(R.id.article, item.getTopicTitle());
        if (item.getTopicContent().trim().length() == 0) {
            holder.getView(R.id.topicContent).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.topicContent).setVisibility(View.VISIBLE);
            holder.setText(R.id.topicContent, item.getTopicContent());
        }
        holder.setText(R.id.forum_time, item.getTopicSendTime());
        holder.setText(R.id.forum_comments, item.getCommentCount() + "");
        holder.setText(R.id.forum_amount, item.getReadCount() + "");
        if(listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.ItemClick(item);
                }
            });
        }
    }
    public void setPlateItemClickListener(PlateItemClickListener Listener){
        listener = Listener;
    }
    public interface PlateItemClickListener{
        void ItemClick(ForumPlateBean.DataBean.ListBean item);
    }
}
