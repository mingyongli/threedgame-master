package com.ws3dm.app.mvvm.adapter;

import android.content.Context;

import com.ws3dm.app.R;
import com.ws3dm.app.adapter.BaseRecyclerAdapter;
import com.ws3dm.app.adapter.RecyclerViewHolder;
import com.ws3dm.app.mvvm.bean.PlateListHeadBean;

/**
 * 置顶公告适配器
 */
public class NoticeAdapter extends BaseRecyclerAdapter<PlateListHeadBean.DataBean.HeadBean.NoticeBean> {
    public NoticeAdapter(Context ctx, int layoutId) {
        super(ctx, layoutId);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PlateListHeadBean.DataBean.HeadBean.NoticeBean item) {
        holder.setText(R.id.topicLable, item.getTopicLable());
        holder.setText(R.id.topicTitle, item.getTopicTitle());
    }
}
