package com.ws3dm.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ws3dm.app.R;
import com.ws3dm.app.bean.NewsBean;
import com.ws3dm.app.bean.OriginalBean;
import com.ws3dm.app.util.TimeUtil;
import com.ws3dm.app.util.glide.GlideUtil;

/**
 * Describution :游戏库界面item  adapter
 * 
 * Author : DKjuan
 * 
 * Date : 2017/8/21 11:47
 **/
public class LastMultiAdapter extends MultiItemRecyclerAdapter<OriginalBean> {
	public LastMultiAdapter(Context ctx, MultiItemTypeSupport<OriginalBean> support) {
		super(ctx, support);
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int                layoutId;
		RecyclerViewHolder holder;
		switch (viewType) {
			case 1:// 大图模式
				layoutId = mMultiItemTypeSupport.getLayoutId(1);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
			default://正常图片
				layoutId = mMultiItemTypeSupport.getLayoutId(0);
				holder   = RecyclerViewHolder.get(mContext, parent, layoutId);
				break;
		}
		return holder;
	}

	@Override
	public void bindData(RecyclerViewHolder holder, int position, final OriginalBean item) {
		switch (item.getIs_showbig()) {
			case 1://大图模式
				holder.setImageByUrl(R.id.img_top,item.getBigpic());
				holder.setText(R.id.tv_title, item.getTitle());
//				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr, (ImageView) holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name, item.getUser().nickname);
				if(item.getLabel().size()>0){
					holder.setText(R.id.txt_label, item.getLabel().get(0).getName());
					holder.getView(R.id.txt_label).setVisibility(View.VISIBLE);
				}else{
					holder.getView(R.id.txt_label).setVisibility(View.GONE);
				}
				holder.setText(R.id.tv_time, TimeUtil.getFoolishTime2(item.getPubdate_at()+"000"));
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				break;
			default://正常模式
//				holder.setImageByUrl(R.id.img_head,item.getUser().avatarstr);
				GlideUtil.loadCircleImage(mContext,item.getUser().avatarstr, (ImageView) holder.getView(R.id.img_head));
				holder.setText(R.id.tv_name, item.getUser().nickname);
				if(item.getLabel().size()>0){
					holder.setText(R.id.txt_label, item.getLabel().get(0).getName());
					holder.getView(R.id.txt_label).setVisibility(View.VISIBLE);
				}else{
					holder.getView(R.id.txt_label).setVisibility(View.GONE);
				}
				holder.setText(R.id.tv_title, item.getTitle());
				holder.setText(R.id.txtComment, item.getTotal_ct()+"");
				GlideUtil.loadRoundImage(mContext,item.getLitpic(), (ImageView) holder.getView(R.id.img_news),5);
				holder.setText(R.id.tv_time, TimeUtil.getFoolishTime2(item.getPubdate_at()+"000"));
				break;
		}
	}
}