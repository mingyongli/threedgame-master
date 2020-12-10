package com.ws3dm.app.mvvm.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.PlateContentBean;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

public class ForumHotAdapter extends BaseRecyclerAdapter<PlateContentBean.DataBean.ListBean> {

    private HotItemClickListener listener;

    public ForumHotAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PlateContentBean.DataBean.ListBean item) {
        GlideUtil.loadImage(mContext, item.getTopicIcon(), holder.getView(R.id.forum_img));
        holder.setText(R.id.forum_title, item.getTopicName());
        GlideUtil.loadImage(mContext, item.getTopicSendUserIcon(), holder.getView(R.id.author_img));
        holder.setText(R.id.author_name, item.getTopicSendUserName());
        holder.setText(R.id.topicLabel, item.getTopicLabel());
        holder.setText(R.id.article, "\t" + item.getTopicTitle());
        if (item.getTopicContent().trim().length() == 0) {
            holder.getView(R.id.forum_content).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.forum_content).setVisibility(View.VISIBLE);
            holder.setText(R.id.forum_content, item.getTopicContent());
        }
        holder.setText(R.id.forum_time, item.getTopicSendTime());
        holder.setText(R.id.forum_comments, item.getCommentCount() + "");
        holder.setText(R.id.forum_amount, item.getReadCount() + "");
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.ItemClickListener(item);
                }
            });
        }
    }

    public void setHotItemClickListener(HotItemClickListener Listener) {
        listener = Listener;
    }

    public interface HotItemClickListener {
        void ItemClickListener(PlateContentBean.DataBean.ListBean item);
    }
}
